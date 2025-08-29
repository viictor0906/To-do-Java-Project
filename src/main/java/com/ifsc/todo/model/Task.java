package com.ifsc.todo.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

//Notation is a method to define what this class does.
@Entity
public class Task {

    @Id //Define database id rules to atribute.
    @GeneratedValue(strategy=GenerationType.IDENTITY) //Create new ids automatically.
    private Long id;

    private String title;
    private String desc;
    private String responsible;

    private LocalDate creationDate=LocalDate.now();
    private LocalDate limitDate;

    @ManyToMany
    @JoinTable(
        name = "categoryTask",
        joinColumns = @JoinColumn(name = "taskID"),
        inverseJoinColumns = @JoinColumn(name = "categoryID")
    )
    private List<Category> categories;

    // /-Calling a object of other class file(status and priorities).
    @Enumerated(EnumType.STRING) //
    private Status status;
    @Enumerated(EnumType.STRING)
    private Priorities priority;
    // -\

    //-- Getters and Setters. --
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id=id;
    }
    //
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title=title;
    }
    //
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc=desc;
    }
    //
    public String getResponsible() {
        return responsible;
    }
    public void setResponsible(String responsible) {
        this.responsible=responsible;
    }
    //
    public LocalDate getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate=creationDate;
    }
    //
    public LocalDate getLimitDate() {
        return limitDate;
    }
    public void setLimitDate(LocalDate limitDate) {
        this.limitDate=limitDate;
    }
    //
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status=status;
    }
    //
    public Priorities getPriority() {
        return priority;
    }
    public void setPriority(Priorities priority) {
        this.priority=priority;
    }
    //
    public List<Category> getCategories() {
        return categories;
    }
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    //-- Getters and Setters. --   
}