package com.ifsc.tarefas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifsc.tarefas.model.Categoria;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNome(String nome);
    boolean existsByNome(String nome);
}
