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

    @GetMapping("/list")
    String listTasks(Model model){
        model.addAttribute("task",taskRepository.findAll());
        return "list";
    }

    @GetMapping("/tasks")
    String newTask(Model model){
        model.addAttribute("task", new Task());
        model.addAttribute("priorities", Priorities.values());
        model.addAttribute("statusList", Status.values());
        return "tasks";
    }

    @PostMapping("/saveTask")
    String saveTask(@ModelAttribute("Task") Task task){
        taskRepository.save(task);
        return "redirect:/templates/list";
    }

    @PostMapping("/save")
    String save(@ModelAttribute("task") Task task){
        taskRepository.save(task);
        return "redirect:/templates/list";
    }

    @PostMapping("/{id}/delete")
    String delete(@PathVariable Long id){
        taskRepository.deleteById(id);
        return "redirect:/templates/list";
    }

    @GetMapping("/{id}/edit")
    String edit(@PathVariable Long id, Model model){
        var task = taskRepository.findById(id).orElse(null);
        if(task==null){
            return "redirect:/templates/list";
        }
        model.addAttribute("task", task);
        model.addAttribute("priorities", Priorities.values());
        model.addAttribute("status", Status.values());
        return "tarefa";
    }
}