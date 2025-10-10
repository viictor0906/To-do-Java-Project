package com.ifsc.tarefas.services;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ifsc.tarefas.model.Tarefa;
import com.ifsc.tarefas.repository.TarefaRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import com.ifsc.tarefas.repository.ArquivoRepository;
import com.ifsc.tarefas.repository.CategoriaRepository;
import com.ifsc.tarefas.auth.RequestAuth;
import com.ifsc.tarefas.model.Arquivo;
import com.ifsc.tarefas.model.Prioridade;
import com.ifsc.tarefas.model.Status;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaServices {

  private final ArquivoRepository arquivoRepository;
  private final TarefaRepository repo;
  private final CategoriaRepository categoriaRepo;

  public TarefaServices(TarefaRepository repo, CategoriaRepository categoriaRepo, ArquivoRepository arquivoRepository) {
    this.repo = repo;
    this.categoriaRepo = categoriaRepo;
    this.arquivoRepository = arquivoRepository;
  }

  @PostMapping
  public ResponseEntity<Tarefa> criarNovaTarefa(@Valid @RequestBody Tarefa tarefa, jakarta.servlet.http.HttpServletRequest request){
      // Vincula a tarefa ao usuário autenticado usando o campo responsavel
      String user = RequestAuth.getUser(request);
      tarefa.setResponsavel(user);
      return ResponseEntity.ok(repo.save(tarefa));
  }

  @GetMapping
  public ResponseEntity<?> listar(jakarta.servlet.http.HttpServletRequest request) {
    String user = RequestAuth.getUser(request);
    String role = RequestAuth.getRole(request);
    return ResponseEntity.ok("ADMIN".equals(role) ? repo.findAll() : repo.findByResponsavel(user));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Tarefa> buscar(@PathVariable Long id, jakarta.servlet.http.HttpServletRequest request) {
    String user = RequestAuth.getUser(request);
    String role = RequestAuth.getRole(request);
    return repo.findById(id)
        .map(ResponseEntity::ok)
        .filter(resp -> "ADMIN".equals(role) || user.equals(resp.getBody().getResponsavel()))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Tarefa> atualizar(@PathVariable Long id,  @RequestBody Tarefa dto, jakarta.servlet.http.HttpServletRequest request) {
    String user = RequestAuth.getUser(request);
    String role = RequestAuth.getRole(request);
    var optionalTarefa = repo.findById(id);
    if (optionalTarefa.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    
    Tarefa existing = optionalTarefa.get();
    if (!"ADMIN".equals(role) && !user.equals(existing.getResponsavel())) {
      return ResponseEntity.notFound().build();
    }
    existing.setTitulo(dto.getTitulo());
    existing.setDescricao(dto.getDescricao());
    existing.setPrioridade(dto.getPrioridade());
    existing.setStatus(dto.getStatus());
    existing.setDataLimite(dto.getDataLimite());
    
    Tarefa salvo = repo.save(existing);
    return ResponseEntity.ok(salvo);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> remover(@PathVariable Long id, jakarta.servlet.http.HttpServletRequest request) {
    String user = RequestAuth.getUser(request);
    String role = RequestAuth.getRole(request);
    var optional = repo.findById(id);
    if (optional.isEmpty() || (!"ADMIN".equals(role) && !user.equals(optional.get().getResponsavel()))) return ResponseEntity.notFound().build();
    repo.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  // Listar tarefas por categoria
  @GetMapping("/por-categoria/{categoriaId}")
  public ResponseEntity<?> tarefasPorCategoria(@PathVariable Long categoriaId) {
    return categoriaRepo.findById(categoriaId)
        .map(categoria -> ResponseEntity.ok(categoria.getTarefas()))
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/por-status/{status}")
  public ResponseEntity<List<Tarefa>> tarefasPorStatus(@PathVariable Status status, jakarta.servlet.http.HttpServletRequest request) {
    String user = RequestAuth.getUser(request);
    return ResponseEntity.ok(repo.findByResponsavelAndStatus(user, status));
  }

  @GetMapping("/por-prioridade/{prioridade}")
  public ResponseEntity<List<Tarefa>> tarefasPorPrioridade(@PathVariable Prioridade prioridade, jakarta.servlet.http.HttpServletRequest request) {
    String user = RequestAuth.getUser(request);
    return ResponseEntity.ok(repo.findByResponsavelAndPrioridade(user, prioridade));
  }

  @GetMapping("/por-responsavel/{responsavel}")
  public ResponseEntity<List<Tarefa>> tarefasPorResponsavel(@PathVariable String responsavel, jakarta.servlet.http.HttpServletRequest request) {
    String user = RequestAuth.getUser(request);
    if (!user.equals(responsavel)) return ResponseEntity.ok(List.of());
    return ResponseEntity.ok(repo.findByResponsavel(responsavel));
  }

  @GetMapping("/vencidas")
  public ResponseEntity<List<Tarefa>> tarefasVencidas(jakarta.servlet.http.HttpServletRequest request) {
    String user = RequestAuth.getUser(request);
    return ResponseEntity.ok(repo.findByResponsavelAndDataLimiteBefore(user, LocalDate.now()));
  }

  @PostMapping("/{tarefaId}/categorias/{categoriaId}")
  @Transactional
  public ResponseEntity<Void> adicionarCategoria(
          @PathVariable Long tarefaId,
          @PathVariable Long categoriaId,
          jakarta.servlet.http.HttpServletRequest request) {

      String user = RequestAuth.getUser(request);
      var tarefa = repo.findById(tarefaId);
      var categoria = categoriaRepo.findById(categoriaId);
      
      if (tarefa.isEmpty() || categoria.isEmpty()) {
          return ResponseEntity.notFound().build();
      }
      if (!user.equals(tarefa.get().getResponsavel())) {
          return ResponseEntity.notFound().build();
      }

      tarefa.get().adicionarCategoria(categoria.get());
      repo.save(tarefa.get());

      return ResponseEntity.noContent().build();
  }

  @PostMapping("/uploadFile")
  public ResponseEntity<String> handleUpload
  (
    @RequestParam("fileDescription") String fileDescription,
    @RequestParam("objectFile") MultipartFile objectFile
  )
  {
    if(objectFile.isEmpty())
    {
      return ResponseEntity.badRequest().body("File is empty");
    }

    Arquivo arquivo = new Arquivo();
    String fileName = objectFile.getOriginalFilename();
    long fileSize = objectFile.getSize();
    var fileConcatenated = ("Got file " + fileName + " (" + fileSize + " bytes) with description: " + fileDescription);
    String key = UUID.randomUUID().toString() + "_" + arquivo.getFileName();

    arquivo.setFileName(fileName);
    arquivo.setFileDescription(fileDescription);
    arquivo.setFileSize(fileConcatenated);
    arquivo.setFileConcatenated(null);
    

    arquivoRepository.save(arquivo);

    return ResponseEntity.ok(fileConcatenated);
  }
}