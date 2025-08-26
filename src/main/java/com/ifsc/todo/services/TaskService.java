package com.ifsc.todo.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifsc.todo.model.Task;
import com.ifsc.todo.repository.CategoryRepository;
import com.ifsc.todo.repository.TaskRepository;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController //This notation indicate wich this class is a service.
@RequestMapping("/tasks") //Define URL pattern.
public class TaskService {

    private final CategoryRepository categoryRepository;
    //Injecting the task repo to use on service and search things in db.
    private final TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository, CategoryRepository categoryRepository){
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
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

    @PutMapping("edit/{id}")
    public ResponseEntity<Task> editTask(@PathVariable Long id,@RequestBody Task newTask){
        return taskRepository.findById(id).map(
            task->{
                task.setTitle(newTask.getTitle());
                task.setDesc(newTask.getDesc());
                task.setResponsible(newTask.getResponsible());
                task.setLimitDate(newTask.getLimitDate());
                task.setStatus(newTask.getStatus());
                task.setPriority(newTask.getPriority());
                
                //Confirm which everthing went well and return the task saved.
                return ResponseEntity.ok(taskRepository.save(task));
            }
            //In contrary case, return which dont find the response.
        ) .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable Long id){
        if(!taskRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        taskRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{taskID}/associateCategory/{categoryID}")
    @Transactional
    public ResponseEntity<Void> associateForCategory(
        @PathVariable Long taskId,
        @PathVariable Long categoryID
    ){
        var task = taskRepository.findById(taskId);
        var category = categoryRepository.findById(categoryID);
        
        if(task.isEmpty() || category.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        task.get().getCategories().add(category.get());
        taskRepository.save(task.get());
        return ResponseEntity.ok().build();
    }
}