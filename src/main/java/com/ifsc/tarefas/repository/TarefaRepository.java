package com.ifsc.tarefas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifsc.tarefas.model.Tarefa;
import com.ifsc.tarefas.model.Status;
import com.ifsc.tarefas.model.Prioridade;

import java.time.LocalDate;
import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    // Consultas filtradas pelo usuário (campo responsavel)
    List<Tarefa> findByResponsavel(String responsavel);
    List<Tarefa> findByResponsavelAndStatus(String responsavel, Status status);
    List<Tarefa> findByResponsavelAndPrioridade(String responsavel, Prioridade prioridade);
    List<Tarefa> findByResponsavelAndDataLimiteBefore(String responsavel, LocalDate data);
    List<Tarefa> findByResponsavelContainingIgnoreCase(String responsavel);
    List<Tarefa> findByResponsavelAndStatusAndPrioridade(String responsavel, Status status, Prioridade prioridade);
}

