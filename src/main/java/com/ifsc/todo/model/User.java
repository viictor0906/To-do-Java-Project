package com.ifsc.todo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// anotações
@Entity
@Table(name = "users")
public class User {
    
    // nome do usuario
    @Id
    @Column(name = "username", nullable = false)
    @NotBlank(message = "O campo username é obrigatorio")
    @Size(min = 3, max = 100, message = "O campo username deve ter entre 3 e 100 caracteres")
    private String username;

    // senha
    @Column(name = "password", nullable = false)
    @NotBlank(message = "O campo password é obrigatorio")
    @Size(min = 3, max = 100, message = "O campo password deve ter entre 3 e 100 caracteres")
    private String password;

    // PAPEL DE USUARIO
    @Column(name = "role", nullable = false)
    @NotNull(message = "O campo 'role' é obrigatorio")
    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return role;
    }

    public void setRoles(String role) {
        this.role = role;
    }

    

}