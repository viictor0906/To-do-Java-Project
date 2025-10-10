package com.ifsc.tarefas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifsc.tarefas.model.Arquivo;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {
    Optional<Arquivo> findByNome(String fileName);
    boolean existsByNome(String fileName);
}