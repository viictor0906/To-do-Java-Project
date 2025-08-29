package com.ifsc.todo.services;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ifsc.todo.model.Priorities;
import com.ifsc.todo.model.Status;
import com.ifsc.todo.model.Task;
import com.ifsc.todo.repository.TaskRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/templates")
public class TemplateServices{
    private final TaskRepository taskRepository;

    public TemplateServices(TaskRepository taskRepository){
        this.taskRepository=taskRepository;
    }

    @GetMapping("/listTasks")
    String listTasks(Model model){
        model.addAttribute("listTasks",taskRepository.findAll());
        return "listTasks";
    }

    @GetMapping("/addTask")
    String newTask(Model model){
        model.addAttribute("addTask", new Task());
        model.addAttribute("priorities", Priorities.values());
        model.addAttribute("statusList", Status.values());
        return "addTask";
    }

    @PostMapping("/saveTask")
    String saveTask(@ModelAttribute("Task") Task task){
        taskRepository.save(task);
        return "redirect:/templates/listTasks";
    }

    @PostMapping("/{id}/delete")
    String delete(@PathVariable Long id){
        taskRepository.deleteById(id);
        return "redirect:/templates/listTasks";
    }

    @GetMapping("/{id}/edit")
    String edit(@PathVariable Long id, Model model){
        var task = taskRepository.findById(id).orElse(null);
        if(task==null){
            return "redirect:/templates/listTasks";
        }
        model.addAttribute("task", task);
        model.addAttribute("priorities", Priorities.values());
        model.addAttribute("status", Status.values());
        return "editTask";
    }
}