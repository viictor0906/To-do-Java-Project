package com.ifsc.todo.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ifsc.todo.entities.user.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String>
{
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUserName(String username);
}