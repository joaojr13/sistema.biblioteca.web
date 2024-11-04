package com.biblioteca.web.services;

import com.biblioteca.web.dtos.RegistrationDto;
import com.biblioteca.web.dtos.UsuarioDto;
import com.biblioteca.web.models.UserEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);

    UserEntity findByEmail(@NotEmpty String email);

    UserEntity findByName(@NotEmpty String username);

    List<UserEntity> findAll();

    UserEntity findById(@NotEmpty Long id);

    void updateUser(UserEntity usuario);

    void deleteById(Long clienteId);
}
