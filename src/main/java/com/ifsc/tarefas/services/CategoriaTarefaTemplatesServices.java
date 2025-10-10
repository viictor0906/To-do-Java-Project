package com.ifsc.tarefas.services;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ifsc.tarefas.model.Categoria;
import com.ifsc.tarefas.repository.CategoriaRepository;
import com.ifsc.tarefas.repository.TarefaRepository;

@Controller
@RequestMapping("/tarefas/{id}/categorias")
public class CategoriaTarefaTemplatesServices {
    
    private final TarefaRepository tarefaRepo;
    private final CategoriaRepository categoriaRepo;

    public CategoriaTarefaTemplatesServices(TarefaRepository tarefaRepo, CategoriaRepository categoriaRepo) {
        this.tarefaRepo = tarefaRepo;
        this.categoriaRepo = categoriaRepo;
    }

    @GetMapping
    String tela(@PathVariable Long id, Model model, jakarta.servlet.http.HttpServletRequest request) {
        var user = com.ifsc.tarefas.auth.RequestAuth.getUser(request);
        var role = com.ifsc.tarefas.auth.RequestAuth.getRole(request);
        var tarefa = tarefaRepo.findById(id);
        if (tarefa.isEmpty()) {
            return "redirect:/templates";
        }
        if (!"ADMIN".equals(role) && !user.equals(tarefa.get().getResponsavel())) {
            return "redirect:/templates";
        }

        var todasCategorias = categoriaRepo.findAll();
        var categoriasSelecionadas = tarefa.get().getCategorias().stream()
                .map(Categoria::getId)
                .toList();

        model.addAttribute("tarefa", tarefa.get());
        model.addAttribute("todasCategorias", todasCategorias);
        model.addAttribute("selecionadas", categoriasSelecionadas);
        model.addAttribute("totalCategorias", todasCategorias.size());
        model.addAttribute("categoriasSelecionadas", categoriasSelecionadas.size());

        return "gerenciar-categoria-tarefa";
    }

    @PostMapping
    @Transactional
    String salvarAssociacoes(@PathVariable Long id,
                             @RequestParam(name = "categoriaIds", required = false) List<Long> categoriaIds,
                             RedirectAttributes redirectAttributes,
                             jakarta.servlet.http.HttpServletRequest request) {
        
        var user = com.ifsc.tarefas.auth.RequestAuth.getUser(request);
        var role = com.ifsc.tarefas.auth.RequestAuth.getRole(request);
        var tarefa = tarefaRepo.findById(id);
        if (tarefa.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Tarefa não encontrada!");
            return "redirect:/templates";
        }
        
        if (!"ADMIN".equals(role) && !user.equals(tarefa.get().getResponsavel())) {
            redirectAttributes.addFlashAttribute("erro", "Acesso negado à tarefa");
            return "redirect:/templates";
        }

        var ids = (categoriaIds == null) ? List.<Long>of() : categoriaIds;
        var categorias = new HashSet<>(categoriaRepo.findAllById(ids));

        var tarefaAtual = tarefa.get();
        
        tarefaAtual.setCategorias(categorias);
        tarefaRepo.save(tarefaAtual);

        var mensagem = String.format("Categorias da tarefa '%s' atualizadas com sucesso! (%d categoria(s) associada(s))", 
                                   tarefaAtual.getTitulo(), categorias.size());
        redirectAttributes.addFlashAttribute("success", mensagem);

        return "redirect:/templates";
    }
}
