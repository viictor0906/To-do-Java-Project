package com.ifsc.todo.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifsc.todo.model.Category;

public interface CategoryRepository extends JpaRepository<Category,Long>{
    
}