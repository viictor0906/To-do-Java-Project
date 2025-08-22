package com.ifsc.todo.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifsc.todo.model.Category;
import com.ifsc.todo.repository.CategoryRepository;

@RestController
@RequestMapping("/categories")
public class CategoryService {

    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository=categoryRepository;
    }

    @GetMapping("/search-all")
    public ResponseEntity<?> searchAll(){
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @PostMapping("/insert")
    public ResponseEntity<Category> createNewCategory(@RequestBody Category category){
        return ResponseEntity.ok(categoryRepository.save(category));
    }
}
