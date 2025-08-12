package com.ifsc.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifsc.todo.model.Task;

//This is a subclass based in 'task' class.
//Communication directly with database.
//Initial CRUD maked by create the JpaRepository.
public interface TaskRepository extends JpaRepository<Task,Long> {
    
}
