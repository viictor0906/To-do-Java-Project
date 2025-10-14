package com.ifsc.tarefas.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.FutureOrPresent;

@Entity
public class Tarefa {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    private String titulo;
    
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;
    
    @NotBlank(message = "O responsável é obrigatório")
    @Size(min = 2, max = 50, message = "O responsável deve ter entre 2 e 50 caracteres")
    private String responsavel;

    @NotNull(message = "A prioridade é obrigatória")
    @Enumerated(EnumType.STRING)
    private Prioridade prioridade = Prioridade.MEDIA;

    @NotNull(message = "O status é obrigatório")
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDENTE;

    private LocalDate dataCriacao = LocalDate.now();
    @FutureOrPresent(message = "A data limite deve ser hoje ou uma data futura")
    private LocalDate dataLimite;

    private String taskColor;

    @ManyToMany
    @JoinTable
    (
        name = "tarefa_categoria",
        joinColumns = @JoinColumn(name = "tarefa_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias = new HashSet<>();

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao; 
    }
    public String getResponsavel() {
        return responsavel;
    }
    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }
    public Prioridade getPrioridade() {
        return prioridade;
    }
    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public LocalDate getDataCriacao() {
        return dataCriacao;
    }
    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    public LocalDate getDataLimite() {
        return dataLimite;
    }
    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }
    public Set<Categoria> getCategorias() {
        return categorias;
    }
    public void setCategorias(Set<Categoria> categorias) {
        this.categorias = categorias;
    }

    public String getTaskColor()
    {
        return taskColor;
    }
    public void setTaskColor(String newTaskColor)
    {
        this.taskColor = newTaskColor;
    }
    
    
    // Métodos helper para gerenciar associações bidirecionais
    public void adicionarCategoria(Categoria categoria) {
        this.categorias.add(categoria);
        categoria.getTarefas().add(this);
    }
    
    public void removerCategoria(Categoria categoria) {
        this.categorias.remove(categoria);
        categoria.getTarefas().remove(this);
    }
    
    public void limparCategorias() {
        for (Categoria categoria : new HashSet<>(this.categorias)) {
            removerCategoria(categoria);
        }
    }
}
