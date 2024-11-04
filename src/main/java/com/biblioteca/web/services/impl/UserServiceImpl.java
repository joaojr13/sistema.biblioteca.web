package com.biblioteca.web.services.impl;

import com.biblioteca.web.dtos.RegistrationDto;
import com.biblioteca.web.dtos.UsuarioDto;
import com.biblioteca.web.models.Role;
import com.biblioteca.web.models.UserEntity;
import com.biblioteca.web.repository.RoleRepository;
import com.biblioteca.web.repository.UserRepository;
import com.biblioteca.web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registrationDto.getUsername());
        userEntity.setEmail(registrationDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        userEntity.setAtivo(true);

        Role role = roleRepository.findByName("CLIENTE");
        userEntity.setRoles(Collections.singletonList(role));
        userRepository.save(userEntity);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity findByName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity findById(Long id) {
        UserEntity usuario = userRepository.findById(id).get();
        return usuario;
    }

    @Override
    public void updateUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    @Override
    public void deleteById(Long clienteId) {
        userRepository.deleteById(clienteId);
    }
}
