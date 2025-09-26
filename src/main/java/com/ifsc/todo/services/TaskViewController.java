package com.ifsc.todo.services;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ifsc.todo.controllers.CategoryRestController;
import com.ifsc.todo.entities.category.CategoryEntity;
import com.ifsc.todo.entities.task.TaskEntity;
import com.ifsc.todo.entities.task.TaskEnumPriority;
import com.ifsc.todo.entities.task.TaskEnumStatus;
import com.ifsc.todo.repositories.CategoryRepository;
import com.ifsc.todo.repositories.TaskRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/taskTemplate")
public class TaskViewController
{
    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;

    public TaskViewController
    (
        TaskRepository taskRepository,
        CategoryRepository categoryRepository,
        CategoryRestController categoryRestController
    )
    {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }


    @GetMapping("/listTasks")
    String listTasks
    (
        Model model,
        @RequestParam(required = false) String taskTitle,
        @RequestParam(required = false) TaskEnumPriority taskPriority,
        @RequestParam(required = false) TaskEnumStatus taskStatus,
        @RequestParam(required = false) String taskResponsible,
        HttpServletRequest request
    )
    {
        var allTasks = taskRepository.findAll();

        if(taskResponsible != null && !taskResponsible.trim().isEmpty())
        {
            allTasks = allTasks.stream().filter(taskFilter -> taskFilter
            .getTaskResponsible()
            .toLowerCase()
            .contains(taskResponsible.toLowerCase()))
            .toList();
        }

        if(taskStatus != null)
        {
            allTasks = allTasks
            .stream()
            .filter(taskFilter -> taskFilter.getTaskStatus().equals(taskStatus))
            .toList();
        }

        if(taskPriority != null)
        {
            allTasks = allTasks
            .stream()
            .filter(taskFilter -> taskFilter
            .getTaskPriority()
            .equals(taskPriority))
            .toList();
        }

        if(taskTitle != null && !taskTitle.trim().isEmpty())
        {
            allTasks = allTasks
            .stream()
            .filter(taskFilter -> taskFilter
            .getTaskTitle()
            .toLowerCase()
            .contains(taskTitle.toLowerCase()))
            .toList();
        }

        model.addAttribute("listTasks", allTasks);
        model.addAttribute("priorities", TaskEnumPriority.values());
        model.addAttribute("statusList", TaskEnumStatus.values());
        return "listTasks";
    }


    @GetMapping("/addTask")
    String newTask(Model model)
    {
        model.addAttribute("addTask", new TaskEntity());
        model.addAttribute("priorities", TaskEnumPriority.values());
        model.addAttribute("statusList", TaskEnumStatus.values());
        model.addAttribute("listCategories", categoryRepository.findAll());
        return "addTask";
    }


    @PostMapping("/saveTask")
    String saveTask
    (
        @Valid @ModelAttribute("Task") TaskEntity submittedTask, 
        BindingResult bindResult, 
        Model model, 
        RedirectAttributes redirectAttribute
    )
    
    {
        taskRepository.save(submittedTask);
        return "redirect:/templates/listTasks";
    }


    @PostMapping("/{id}/delete")
    String delete(@PathVariable Long taskId)
    {
        taskRepository.deleteById(taskId);
        return "redirect:/templates/listTasks";
    }


    @GetMapping("/{id}/edit")
    String edit(@PathVariable Long taskId, Model model)
    {
        var foundTask = taskRepository.findById(taskId).orElse(null);
        if(foundTask == null)
        {
            return "redirect:/templates/listTasks";
        }

        model.addAttribute("task", foundTask);
        model.addAttribute("priorities", TaskEnumPriority.values());
        model.addAttribute("status", TaskEnumStatus.values());
        model.addAttribute("listCategories", categoryRepository.findAll());
        return "editTask";
    }

    
    @GetMapping("/{taskID}/associateCategory")
    String associateToCategory(Model model, @PathVariable Long taskID)
    {
        List<CategoryEntity> foundCategories = categoryRepository.findAll();
        model.addAttribute("categories", foundCategories);

        for(CategoryEntity category:foundCategories)
        {
            System.out.println("All categories" 
            + category.getCategoryTitle() 
            + " - " 
            + category.getCategoryId());
        }

        var foundTask = taskRepository.findById(taskID);
        model.addAttribute("task", foundTask.get());

        return "manageCategory";
    }


    @PostMapping("/{taskID}/associateToCategory/{categoryID}")
    String associateToCategory(Model model, @PathVariable Long taskID, @PathVariable Long categoryID)
    {
        var foundTask = taskRepository.findById(taskID);
        var foundCategory = categoryRepository.findById(categoryID);

        if(foundTask.isEmpty() || foundCategory.isEmpty())
        {
            return "redirect:/templates/listTasks";
        }
        
        foundTask.get().getTaskCategories().add(foundCategory.get());
        taskRepository.save(foundTask.get());
        return "redirect:/templates/listTasks";
    }
}