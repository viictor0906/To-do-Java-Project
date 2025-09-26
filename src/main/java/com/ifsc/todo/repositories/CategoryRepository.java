package com.ifsc.todo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifsc.todo.entities.category.CategoryEntity;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity,Long>
{
    Optional<CategoryEntity> findByName(String categoryTitle);
    Boolean existsByName(String categoryTitle);
}