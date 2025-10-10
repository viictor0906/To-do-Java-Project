package com.ifsc.tarefas.model;

import java.sql.Blob;

import org.aspectj.weaver.ast.Var;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Arquivo 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;
    
    @Column(nullable = false, length = 60, unique = true)
    private String fileName;

    private String fileDescription;
    private String fileSize;
    private Var fileConcatenated;
    private Blob fileBlob;


    public Blob getFileBlob()
    {
        return fileBlob;
    }
    public Blob setFileBlob(Blob newFileBlob)
    {
        this.fileBlob = newFileBlob;
        return fileBlob;
    }
    

    public Long getFileId()
    {
        return fileId;
    }


    public String getFileName() 
    {
        return fileName;
    }
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }


    public String getFileDescription() 
    {
        return fileDescription;
    }
    public void setFileDescription(String fileDescription) 
    {
        this.fileDescription = fileDescription;
    }


    public String getFileSize() 
    {
        return fileSize;
    }
    public void setFileSize(String fileSize) 
    {
        this.fileSize = fileSize;
    }


    public Var getFileConcatenated() 
    {
        return fileConcatenated;
    }
    public void setFileConcatenated(Var fileConcatenated) 
    {
        this.fileConcatenated = fileConcatenated;
    }
}