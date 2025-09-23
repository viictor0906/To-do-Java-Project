package com.ifsc.todo.repository;

import java.util.Optional;

import com.ifsc.todo.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String>{
    Optional<User> findByUsername(String username);
    boolean existsByUserName(String username);
}
