package com.ifsc.todo.services;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ifsc.todo.entities.category.CategoryEntity;
import com.ifsc.todo.repositories.CategoryRepository;

@Controller
@RequestMapping("/categoryTemplate")
public class CategoryViewController 
{
    private final CategoryRepository categoryRepository;

    public CategoryViewController(CategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }


    @GetMapping("/listCategories")
    String listCategories(Model model)
    {
        model.addAttribute("categories", categoryRepository.findAll());
        return "listCategory";
    }


    @GetMapping("/createCategory")
    String createCategory(Model model)
    {
        model.addAttribute("addCategory", new CategoryEntity());
        return "addCategory";
    }


    @PostMapping("/saveCategory")
    String saveCategory(@ModelAttribute("category") CategoryEntity formCategory)
    {
        categoryRepository.save(formCategory);
        return "redirect:/templatesCategory/listCategory";
    }


    @PostMapping("/{categoryId}/deleteCategory")
    String deleteCategory(@PathVariable Long categoryId)
    {
        this.categoryRepository.deleteById(categoryId);
        return "redirect:/templatesCategory/listCategory";
    }


    @GetMapping("/editCategory/{categoryId}")
    public String showEditCategory(@PathVariable Long categoryId, Model model)
    {
        var foundCategory = categoryRepository.findById(categoryId).orElse(null);

        if(foundCategory == null)
        {
            return "redirect:/templatesCategory/listCategory";
        }
        model.addAttribute("category", foundCategory);
        return "editCategory";
    }


    @PostMapping("/editCategory/{categoryId}")
    public String updateCategory(@PathVariable Long categoryId, @ModelAttribute CategoryEntity formCategory)
    {
        if(!categoryRepository.existsById(categoryId)) 
        {
            return "redirect:/templatesCategory/listCategory";
        }

        formCategory.setId(categoryId);
        categoryRepository.save(formCategory);
        return "redirect:/templatesCategory/listCategory";
    }
}