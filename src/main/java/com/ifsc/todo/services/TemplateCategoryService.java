package com.ifsc.todo.services;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/addCategory")
    String newCategory(Model model){
        model.addAttribute("addCategory", new Category());
        return "addCategory";
    }

    @PostMapping("/saveCategory")
    String saveCategory(@ModelAttribute("category") Category category){
        categoryRepository.save(category);
        return "redirect:/templatesCategory/listCategory";
    }

    @PostMapping("/{id}/deleteCategory")
    String deleteCategory(@PathVariable Long id){
        this.categoryRepository.deleteById(id);
        return "redirect:/templatesCategory/listCategory";
    }

    @GetMapping("/editCategory/{id}")
    public String updateCategory(@PathVariable Long id, Model model){
        var category = categoryRepository.findById(id).orElse(null);
        if(category == null){
            return "redirect:/templatesCategory/listCategory";
        }
        model.addAttribute("category", category);
        return "editCategory";
    }

    @PostMapping("/editCategory/{id}")
    public String updateCategoryPost(@PathVariable Long id, @ModelAttribute Category category){
        if (!categoryRepository.existsById(id)) {
            return "redirect:/templatesCategory/listCategory";
        }
        category.setId(id);
        categoryRepository.save(category);
        return "redirect:/templatesCategory/listCategory";
    }
}