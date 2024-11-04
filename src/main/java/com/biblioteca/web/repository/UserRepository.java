package com.biblioteca.web.repository;

import com.biblioteca.web.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);

    UserEntity findFistByUsername(String username);

    List<UserEntity> findAllByAtivoTrue();
}