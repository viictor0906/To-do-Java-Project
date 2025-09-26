package com.ifsc.todo.entities.task;

import java.time.LocalDate;
import java.util.List;

import com.ifsc.todo.entities.category.CategoryEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class TaskEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @NotBlank(message = "Title field is obrigatory")
    @Size(min = 3, max = 100, message = "Between 3 and 100")
    private String taskTitle;
    
    @Size(max = 500, message = "taskDescription max 500")
    private String taskDescription;

    @NotBlank(message = "Responsible is necessary")
    @Size(min = 3, max = 100, message = "Between 3 and 100")
    private String taskResponsible;

    private LocalDate taskCreationDate = LocalDate.now();

    @FutureOrPresent(message = "In future or present")
    private LocalDate taskLimitDate;

    @ManyToMany
    @JoinTable
    (
        name = "categoryTask",
        joinColumns = @JoinColumn(name = "taskID"),
        inverseJoinColumns = @JoinColumn(name = "categoryID")
    )
    private List<CategoryEntity> taskCategories;

    @Enumerated(EnumType.STRING)
    private TaskEnumStatus taskStatus;

    @Enumerated(EnumType.STRING)
    private TaskEnumPriority taskPriority;


    public Long getTaskId() 
    {
        return taskId;
    }
    public void setTaskId(Long taskId) 
    {
        this.taskId = taskId;
    }
    

    public String getTaskTitle() 
    {
        return taskTitle;
    }
    public void setTaskTitle(String taskTitle) 
    {
        this.taskTitle = taskTitle;
    }
    

    public String getTaskDescription() 
    {
        return taskDescription;
    }
    public void setTaskDescription(String taskDescription) 
    {
        this.taskDescription = taskDescription;
    }
    

    public String getTaskResponsible() 
    {
        return taskResponsible;
    }
    public void setTaskResponsible(String taskResponsible) 
    {
        this.taskResponsible = taskResponsible;
    }
    

    public LocalDate getTaskCreationDate() 
    {
        return taskCreationDate;
    }
    public void setTaskCreationDate(LocalDate taskCreationDate) 
    {
        this.taskCreationDate = taskCreationDate;
    }
    

    public LocalDate getTaskLimitDate() 
    {
        return taskLimitDate;
    }
    public void setTaskLimitDate(LocalDate taskLimitDate) 
    {
        this.taskLimitDate = taskLimitDate;
    }
    

    public TaskEnumStatus getTaskStatus() 
    {
        return taskStatus;
    }
    public void setTaskStatus(TaskEnumStatus taskStatus) 
    {
        this.taskStatus = taskStatus;
    }
    

    public TaskEnumPriority getTaskPriority() 
    {
        return taskPriority;
    }
    public void setTaskPriority(TaskEnumPriority taskPriority) 
    {
        this.taskPriority = taskPriority;
    }
    
    
    public List<CategoryEntity> getTaskCategories() 
    {
        return taskCategories;
    }
    public void setCategories(List<CategoryEntity> taskCategories) 
    {
        this.taskCategories = taskCategories;
    }
}