package com.ifsc.todo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifsc.todo.entities.task.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity,Long> {}