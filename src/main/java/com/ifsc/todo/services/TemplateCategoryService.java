package com.ifsc.todo.services;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ifsc.todo.model.Category;
import com.ifsc.todo.repository.CategoryRepository;

@Controller
@RequestMapping("/templatesCategory")
public class TemplateCategoryService {
    private final CategoryRepository categoryRepository;

    public TemplateCategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/listCategory")
    String listCategories(Model model){
        model.addAttribute("categories", categoryRepository.findAll());
        return "listCategory";
    }

    @GetMapping("/createCategory")
    String newCategory(Model model){
        model.addAttribute("createCategory", new Category());
        return "createCategory";
    }

    //@PostMapping("/{id}/deleteCategory")
    //String deleteCategory(@PathVariable Long id){
    //    this.categoryRepository.deleteById(id);
    //    return "redirect:/templates/list";
    //}

    
}