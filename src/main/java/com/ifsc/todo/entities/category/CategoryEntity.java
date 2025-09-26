package com.ifsc.todo.entities.category;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ifsc.todo.entities.task.TaskEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class CategoryEntity
{
    private String categoryTitle;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long categoryId;

    @JsonIgnore
    @ManyToMany(mappedBy = "categories") //mudar depois
    private List<TaskEntity> tasksList;

        public Long getCategoryId()
        {
            return categoryId;
        }
        public void setId(Long categoryId)
        {
            this.categoryId = categoryId;
        }
        

        public String getCategoryTitle()
        {
            return categoryTitle;
        }
        public void setName(String categoryTitle)
        {
            this.categoryTitle = categoryTitle;
        }
        

        public List<TaskEntity> getTasksList()
        {
            return tasksList;
        }
        public void setTasksList(List<TaskEntity> tasksList)
        {
            this.tasksList = tasksList;
        }
}