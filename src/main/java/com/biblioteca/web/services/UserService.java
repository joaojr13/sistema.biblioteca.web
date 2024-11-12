package com.biblioteca.web.services;

import com.biblioteca.web.models.Role;
import com.biblioteca.web.models.UserEntity;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public interface UserService {
    void saveUser(UserEntity registrationDto);

    UserEntity findByEmail(@NotEmpty String email);

    UserEntity findByName(@NotEmpty String username);

    List<UserEntity> findAll();

    UserEntity findById(@NotEmpty Long id);

    void updateUser(UserEntity usuario);

    void deleteById(Long clienteId);

    List<UserEntity> pesquisarClientes(String search, String status);

    List<Role> findAllRoles();
}
