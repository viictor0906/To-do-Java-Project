package com.ifsc.todo.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifsc.todo.model.Task;
import com.ifsc.todo.repository.TaskRepository;

@RestController //This notation indicate wich this class is a service.
@RequestMapping("/tasks") //Define URL pattern.
public class TaskService {
    //Injecting the task repo to use on service and search things in db.
    private final TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository){
        this.taskRepository=taskRepository;
    }

    //Notation to create a GET.
    //To call this api, i use /tasks/search-all
    @GetMapping("/search-all")
    public ResponseEntity<?> searchAll(){
        //Use the repo to search all tasks.
        return ResponseEntity.ok(taskRepository.findAll());
    }

    //API to create a new task.
    //Notation to post --> /tasks/insert.
    //Is needed to inform a notation @RequestBody to inform which send a body.
    @PostMapping("/insert")
    public ResponseEntity<Task> createNewTask(@RequestBody Task task){
        return ResponseEntity.ok(taskRepository.save(task));
    }
}
