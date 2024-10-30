package com.biblioteca.web.services;

import com.biblioteca.web.dtos.RegistrationDto;
import com.biblioteca.web.models.UserEntity;
import jakarta.validation.constraints.NotEmpty;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);

    UserEntity findByEmail(@NotEmpty String email);

    UserEntity findByName(@NotEmpty String username);
}
