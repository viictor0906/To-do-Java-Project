package com.ifsc.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifsc.todo.model.Category;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long>{
    Optional<Category> findByName(String name);
    Boolean existsByName(String name);
}