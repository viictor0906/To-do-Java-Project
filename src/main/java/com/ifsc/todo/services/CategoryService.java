package com.ifsc.todo.services;

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

import com.ifsc.todo.model.Category;
import com.ifsc.todo.repository.CategoryRepository;

@RestController
@RequestMapping("/categories")
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository=categoryRepository;
    }

    @PostMapping
    public ResponseEntity<Category> createNewCategory(@RequestBody Category category){
        return ResponseEntity.ok(categoryRepository.save(category));
    }

    @GetMapping
    public ResponseEntity<List<Category>> list(){
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> search(@PathVariable Long id){
        return categoryRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Long id, @RequestBody Category dto){
        if(!categoryRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null){
            return ResponseEntity.badRequest().build();
        }

        category.setName(dto.getName());

        Category save = categoryRepository.save(category);
        return ResponseEntity.ok(save);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null && !category.getTasks().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
