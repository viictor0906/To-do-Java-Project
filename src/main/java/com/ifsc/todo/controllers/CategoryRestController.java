package com.ifsc.todo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifsc.todo.entities.category.CategoryEntity;
import com.ifsc.todo.repositories.CategoryRepository;

@RestController
@RequestMapping("/categories")
public class CategoryRestController 
{
    private final CategoryRepository categoryRepository;

    public CategoryRestController(CategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }


    @PostMapping("/createCategory")
    public ResponseEntity<CategoryEntity> createCategory(@RequestBody CategoryEntity newCategory)
    {
        return ResponseEntity.ok(categoryRepository.save(newCategory));
    }


    @GetMapping("/listCategories")
    public ResponseEntity<List<CategoryEntity>> listCategories()
    {
        List<CategoryEntity> allCategories = categoryRepository.findAll();
        return ResponseEntity.ok(allCategories);
    }


    @GetMapping("/searchCategory/{categoryId}")
    public ResponseEntity<CategoryEntity> searchCategory(@PathVariable Long categoryId)
    {
        return categoryRepository.findById(categoryId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/updateCategory/{categoryId}")
    public ResponseEntity<CategoryEntity> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryEntity dto)
    {
        if(!categoryRepository.existsById(categoryId))
        {
            return ResponseEntity.notFound().build();
        }

        CategoryEntity foundCategory = categoryRepository.findById(categoryId).orElse(null);
        if(foundCategory == null)
        {
            return ResponseEntity.badRequest().build();
        }

        foundCategory.setName(dto.getCategoryTitle());

        CategoryEntity savedCategory = categoryRepository.save(foundCategory);
        return ResponseEntity.ok(savedCategory);
    }


    @DeleteMapping("/deleteCategory/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) 
    {
        if(!categoryRepository.existsById(categoryId))
        {
            return ResponseEntity.notFound().build();
        }
        
        CategoryEntity foundCategory = categoryRepository.findById(categoryId).orElse(null);
        if(foundCategory != null && !foundCategory.getTasksList().isEmpty())
        {
            return ResponseEntity.badRequest().build();
        }
        
        categoryRepository.deleteById(categoryId);
        return ResponseEntity.noContent().build();
    }
}
