package com.ifsc.todo.services;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ifsc.todo.model.Category;
import com.ifsc.todo.model.Priorities;
import com.ifsc.todo.model.Status;
import com.ifsc.todo.model.Task;
import com.ifsc.todo.repository.CategoryRepository;
import com.ifsc.todo.repository.TaskRepository;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/templates")
public class TemplateServices{

    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;

    public TemplateServices(TaskRepository taskRepository, CategoryRepository categoryRepository, CategoryService categoryService){
        this.taskRepository=taskRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/listTasks")
    String listTasks(Model model, @RequestParam(required = false) String title, Priorities priority, Status status, String responsible){
        var tasks = taskRepository.findAll();

        if(responsible != null && !responsible.trim().isEmpty()){
            tasks = tasks.stream().filter(t -> t.getResponsible().toLowerCase().contains(responsible.toLowerCase())).toList();
        }

        if(status != null){
            tasks = tasks.stream().filter(t -> t.getStatus().equals(status)).toList();
        }

        if(priority != null){
            tasks = tasks.stream().filter(t -> t.getPriority().equals(priority)).toList();
        }

        if(title != null && !title.trim().isEmpty()){
            tasks = tasks.stream().filter(t -> t.getTitle().toLowerCase().contains(title.toLowerCase())).toList();
        }
        model.addAttribute("listTasks", tasks);
        model.addAttribute("priorities", Priorities.values());
        model.addAttribute("statusList", Status.values());
        return "listTasks";
    }

    @GetMapping("/addTask")
    String newTask(Model model){
        model.addAttribute("addTask", new Task());
        model.addAttribute("priorities", Priorities.values());
        model.addAttribute("statusList", Status.values());
        model.addAttribute("listCategories", categoryRepository.findAll());
        return "addTask";
    }

    @PostMapping("/saveTask")
    String saveTask(@Valid @ModelAttribute("Task") Task task, BindingResult br, Model model, RedirectAttributes ra){
        
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
        model.addAttribute("listCategories", categoryRepository.findAll());
        return "editTask";
    }

    @GetMapping("/{taskID}/associateCategory")
    String associateToCategory(Model model, @PathVariable Long taskID){
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        for(Category category:categories){
            System.out.println("All categories" + category.getName() + " - " + category.getId());
        }

        var task = taskRepository.findById(taskID);
        model.addAttribute("task", task.get());

        return "manageCategory";
    }

    @PostMapping("/{taskID}/associateToCategory/{categoryID}")
    String associateToCategory(Model model, @PathVariable Long taskID, @PathVariable Long categoryID){
        var task = taskRepository.findById(taskID);
        var category = categoryRepository.findById(categoryID);

        if(task.isEmpty() || category.isEmpty()){
            return "redirect:/templates/listTasks";
        }
        
        task.get().getCategories().add(category.get());
        taskRepository.save(task.get());
        return "redirect:/templates/listTasks";
    }
}