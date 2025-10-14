package com.ifsc.tarefas.services;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

import com.ifsc.tarefas.auth.AuthRepository;
import com.ifsc.tarefas.auth.RequestAuth;
import com.ifsc.tarefas.model.Prioridade;
import com.ifsc.tarefas.model.Status;
import com.ifsc.tarefas.model.Tarefa;
import com.ifsc.tarefas.repository.CategoriaRepository;
import com.ifsc.tarefas.repository.TarefaRepository;

@Controller
@RequestMapping("/templates")
public class TemplateService {

  private final TarefaRepository repo;
  private final CategoriaRepository categoriaRepo;
  private final AuthRepository authRepository;

  public TemplateService(TarefaRepository repo, CategoriaRepository categoriaRepo, AuthRepository authRepository) {
    this.repo = repo;
    this.categoriaRepo = categoriaRepo;
    this.authRepository = authRepository;
  }

  @GetMapping
  String listar(Model model, 
    @RequestParam(required = false) String titulo,
    @RequestParam(required = false) String responsavel,
    @RequestParam(required = false) Status status,
    @RequestParam(required = false) Prioridade prioridade,
    @RequestParam(required = false) String taskColor,
    jakarta.servlet.http.HttpServletRequest request) 
{
    var user = RequestAuth.getUser(request);
    var role = RequestAuth.getRole(request);
    var tarefas = "ADMIN".equals(role) ? repo.findAll() : repo.findByResponsavel(user);

    if (titulo != null && !titulo.trim().isEmpty()) {
      tarefas = tarefas.stream()
          .filter(t -> t.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
          .collect(Collectors.toList());
    }

    if (responsavel != null && !responsavel.trim().isEmpty()) {
      tarefas = tarefas.stream()
          .filter(t -> t.getResponsavel().toLowerCase().contains(responsavel.toLowerCase()))
          .collect(Collectors.toList());
    }

    if (status != null) {
      tarefas = tarefas.stream()
          .filter(t -> t.getStatus() == status)
          .collect(Collectors.toList());
    }

    if (prioridade != null) {
      tarefas = tarefas.stream()
          .filter(t -> t.getPrioridade() == prioridade)
          .collect(Collectors.toList());
    }

    if (taskColor != null && !taskColor.trim().isEmpty()) {
      String corFiltro = taskColor.trim().toLowerCase();
      tarefas = tarefas.stream()
          .filter(t -> t.getTaskColor() != null && t.getTaskColor().toLowerCase().equals(corFiltro))
          .collect(Collectors.toList());
    }

    model.addAttribute("tarefas", tarefas);
    model.addAttribute("totalTarefas", tarefas.size());
    model.addAttribute("titulo", titulo);
    model.addAttribute("responsavel", responsavel);
    model.addAttribute("status", status);
    model.addAttribute("prioridade", prioridade);
    model.addAttribute("statusList", Status.values());
    model.addAttribute("prioridades", Prioridade.values());
    model.addAttribute("taskColor", taskColor);
    return "lista";
}

  @GetMapping("/nova")
  String nova(Model model, jakarta.servlet.http.HttpServletRequest request) {
    var user = RequestAuth.getUser(request);
    var role = RequestAuth.getRole(request);
    var t = new Tarefa();
    // Usuário comum: preenche responsável com o próprio usuário
    if (!"ADMIN".equals(role)) {
      t.setResponsavel(user);
    }
    model.addAttribute("tarefa", t);
    model.addAttribute("prioridades", Prioridade.values());
    model.addAttribute("statusList", Status.values());
    model.addAttribute("categorias", categoriaRepo.findAll());
    
    if("ADMIN".equals(role)) 
    {
      model.addAttribute("usuarios", authRepository.getAllUsernames());
    }

    return "formulario";
  }

  @GetMapping("/{id}/editar")
  String editar(@PathVariable Long id, Model model, jakarta.servlet.http.HttpServletRequest request) {
    var user = RequestAuth.getUser(request);
    var role = RequestAuth.getRole(request);
    var tarefa = repo.findById(id);
    if (tarefa.isEmpty()) {
      return "redirect:/templates";
    }
    if (!"ADMIN".equals(role) && !user.equals(tarefa.get().getResponsavel())) {
      return "redirect:/templates";
    }
    
    model.addAttribute("tarefa", tarefa.get());
    model.addAttribute("prioridades", Prioridade.values());
    model.addAttribute("statusList", Status.values());
    model.addAttribute("categorias", categoriaRepo.findAll());
    return "formulario";
  }

  @PostMapping
  String salvar(@Valid @ModelAttribute("tarefa") Tarefa tarefa,
      BindingResult br,
      Model model,
      RedirectAttributes redirectAttributes,
      jakarta.servlet.http.HttpServletRequest request) {
    
    if (br.hasErrors()) {
      model.addAttribute("prioridades", Prioridade.values());
      model.addAttribute("statusList", Status.values());
      model.addAttribute("categorias", categoriaRepo.findAll());
      return "formulario";
    }

    var user = RequestAuth.getUser(request);
    var role = RequestAuth.getRole(request);
    // Se for criação, garante responsavel; se for edição, valida ownership
    if (tarefa.getId() == null) {
      if ("ADMIN".equals(role)) {
        // ADMIN pode definir o responsável pelo formulário
        if (tarefa.getResponsavel() == null || tarefa.getResponsavel().isBlank()) {
          tarefa.setResponsavel(user);
        }
      } else {
        tarefa.setResponsavel(user);
      }
    } else {
      var existente = repo.findById(tarefa.getId());
      if (existente.isEmpty() || (!"ADMIN".equals(role) && !user.equals(existente.get().getResponsavel()))) {
        redirectAttributes.addFlashAttribute("erro", "Acesso negado à tarefa");
        return "redirect:/templates";
      }
      if (!"ADMIN".equals(role)) {
        tarefa.setResponsavel(user);
      }
    }

    repo.save(tarefa); 
    redirectAttributes.addFlashAttribute("success", 
        tarefa.getId() != null ? "Tarefa atualizada com sucesso!" : "Tarefa criada com sucesso!");
    
    return "redirect:/templates";
  }

  @PostMapping("/{id}/excluir")
  String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes, jakarta.servlet.http.HttpServletRequest request) {
    var user = (String) request.getAttribute("AUTH_USER");
    var role = (String) request.getAttribute("AUTH_ROLE");
    var tarefa = repo.findById(id);
    if (tarefa.isPresent()) {
      if (!"ADMIN".equals(role) && !user.equals(tarefa.get().getResponsavel())) {
        redirectAttributes.addFlashAttribute("erro", "Acesso negado à tarefa");
      } else {
        repo.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Tarefa excluída com sucesso!");
      }
    } else {
      redirectAttributes.addFlashAttribute("erro", "Tarefa não encontrada!");
    }
    return "redirect:/templates";
  }

  public String getPrioridadeClass(Prioridade prioridade) {
    return switch (prioridade) {
      case ALTA -> "bg-danger";
      case MEDIA -> "bg-warning";
      case BAIXA -> "bg-success";
    };
  }

  public String getStatusClass(Status status) {
    return switch (status) {
      case CONCLUIDA -> "bg-success";
      case PENDENTE -> "bg-secondary";
    };
  }
}