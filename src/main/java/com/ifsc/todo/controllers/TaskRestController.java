package com.ifsc.todo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifsc.todo.entities.task.TaskEntity;
import com.ifsc.todo.repositories.CategoryRepository;
import com.ifsc.todo.repositories.TaskRepository;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/tasks")
public class TaskRestController 
{
    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;

    public TaskRestController(TaskRepository taskRepository, CategoryRepository categoryRepository)
    {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }


    @GetMapping("/searchAllTasks")
    public ResponseEntity<?> searchAllTasks()
    {
        return ResponseEntity.ok(taskRepository.findAll());
    }


    @PostMapping("/createTask")
    public ResponseEntity<TaskEntity> createTask(@RequestBody TaskEntity task)
    {
        return ResponseEntity.ok(taskRepository.save(task));
    }


    @PutMapping("updateTask/{taskId}")
    public ResponseEntity<TaskEntity> updateTask(@PathVariable Long taskId, @RequestBody TaskEntity actualTask)
    {
        return taskRepository.findById(taskId).map(
            updateTask ->
            {
                updateTask.setTaskTitle(actualTask.getTaskTitle());
                updateTask.setTaskDescription(actualTask.getTaskDescription());
                updateTask.setTaskResponsible(actualTask.getTaskResponsible());
                updateTask.setTaskLimitDate(actualTask.getTaskLimitDate());
                updateTask.setTaskStatus(actualTask.getTaskStatus());
                updateTask.setTaskPriority(actualTask.getTaskPriority());
                
                return ResponseEntity.ok(taskRepository.save(updateTask));
            }
        )
        .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("deleteTask/{id}")
    public ResponseEntity<TaskEntity> deleteTask(@PathVariable Long taskId)
    {
        if(!taskRepository.existsById(taskId))
        {
            return ResponseEntity.notFound().build();
        }

        taskRepository.deleteById(taskId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{taskID}/kToCategory/{categoryID}")
    @Transactional
    public ResponseEntity<Void> TaskToCategory
    (
        @PathVariable Long taskID,
        @PathVariable Long categoryID
    )
    {
        var foundTask = taskRepository.findById(taskID);
        var foundCategory = categoryRepository.findById(categoryID);
        
        if(foundTask.isEmpty() || foundCategory.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        foundTask.get().getTaskCategories().add(foundCategory.get());
        taskRepository.save(foundTask.get());
        return ResponseEntity.ok().build();
    }
}