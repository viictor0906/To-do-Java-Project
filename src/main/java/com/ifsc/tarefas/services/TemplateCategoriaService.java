package com.ifsc.tarefas.services;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ifsc.tarefas.model.Categoria;
import com.ifsc.tarefas.repository.CategoriaRepository;

@Controller
@RequestMapping("/templates-categoria")
public class TemplateCategoriaService {
    
  private final CategoriaRepository categoriaRepo;

  public TemplateCategoriaService(CategoriaRepository categoriaRepo) {
    this.categoriaRepo = categoriaRepo;
  }

  @GetMapping
  String listarCategorias(Model model) {
    var categorias = categoriaRepo.findAll();
    model.addAttribute("categorias", categorias);
    model.addAttribute("categoria", new Categoria());
    model.addAttribute("totalCategorias", categorias.size());
    return "categorias";
  }

  @PostMapping
  String criar(@ModelAttribute Categoria categoria, Model model, RedirectAttributes redirectAttributes) {
    if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
      model.addAttribute("erro", "Nome da categoria é obrigatório");
      model.addAttribute("categorias", categoriaRepo.findAll());
      model.addAttribute("categoria", categoria);
      return "categorias";
    }

    String nomeNormalizado = categoria.getNome().trim();
    if (nomeNormalizado.length() < 2) {
      model.addAttribute("erro", "Nome da categoria deve ter pelo menos 2 caracteres");
      model.addAttribute("categorias", categoriaRepo.findAll());
      model.addAttribute("categoria", categoria);
      return "categorias";
    }

    if (categoriaRepo.existsByNome(nomeNormalizado)) {
      model.addAttribute("erro", "Categoria '" + nomeNormalizado + "' já existe");
      model.addAttribute("categorias", categoriaRepo.findAll());
      model.addAttribute("categoria", categoria);
      return "categorias";
    }

    categoria.setNome(nomeNormalizado);
    categoriaRepo.save(categoria);
    redirectAttributes.addFlashAttribute("success", "Categoria '" + nomeNormalizado + "' criada com sucesso!");
    
    return "redirect:/templates-categoria";
  }

  @PostMapping("/{id}/excluir")
  String excluirCategoria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    var categoria = categoriaRepo.findById(id);
    if (categoria.isEmpty()) {
      redirectAttributes.addFlashAttribute("erro", "Categoria não encontrada!");
      return "redirect:/templates-categoria";
    }

    var nomeCategoria = categoria.get().getNome();
    var tarefasAssociadas = categoria.get().getTarefas().size();

    if (tarefasAssociadas > 0) {
      redirectAttributes.addFlashAttribute("erro", 
          "Não é possível excluir a categoria '" + nomeCategoria + "' pois ela está associada a " + 
          tarefasAssociadas + " tarefa(s)");
      return "redirect:/templates-categoria";
    }

    categoriaRepo.deleteById(id);
    redirectAttributes.addFlashAttribute("success", "Categoria '" + nomeCategoria + "' excluída com sucesso!");
    
    return "redirect:/templates-categoria";
  }
}
