package com.ifsc.tarefas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ifsc.tarefas.model.Tarefa;
import com.ifsc.tarefas.model.Status;
import com.ifsc.tarefas.model.Prioridade;

import java.time.LocalDate;
import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByResponsavel(String responsavel);
    List<Tarefa> findByResponsavelAndStatus(String responsavel, Status status);
    List<Tarefa> findByResponsavelAndPrioridade(String responsavel, Prioridade prioridade);
    List<Tarefa> findByResponsavelAndDataLimiteBefore(String responsavel, LocalDate data);
    List<Tarefa> findByResponsavelContainingIgnoreCase(String responsavel);
    List<Tarefa> findByResponsavelAndStatusAndPrioridade(String responsavel, Status status, Prioridade prioridade);

    @Query("SELECT t FROM Tarefa t WHERE t.responsavel = :responsavel AND LOWER(t.taskColor) = LOWER(:taskColor)")
    List<Tarefa> findByResponsavelAndTaskColorIgnoreCase(@Param("responsavel") String responsavel, @Param("taskColor") String taskColor);
}