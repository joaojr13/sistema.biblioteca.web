package com.biblioteca.web.services.impl;

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
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAtivo(true);

        Role role = roleRepository.findByName("CLIENTE");

        user.setRoles(Collections.singletonList(role));

        userRepository.save(user);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void updateUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    @Override
    public void deleteById(Long clienteId) {
        userRepository.deleteById(clienteId);
    }

    @Override
    public List<UserEntity> pesquisarClientes(String search, String status) {
        if ((search == null || search.isEmpty()) && (status == null || status.isEmpty())) {
            return userRepository.findAll();
        } else if (status != null && !status.isEmpty()) {
            boolean isAtivo = status.equalsIgnoreCase("ativo");
            if (search != null && !search.isEmpty()) {
                return userRepository.searchByNomeOrEmailAndStatus(search, isAtivo);
            } else {
                return userRepository.findByAtivo(isAtivo);
            }
        } else {
            return userRepository.findByNomeCompletoContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search);
        }
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public List<UserEntity> findAllClientesAtivos() {
        List<UserEntity> usuarios = userRepository.findAll();

        return usuarios
                .stream()
                .filter(user -> user.getRoles().stream().allMatch(role -> role.getName().equalsIgnoreCase("CLIENTE")))
                .filter(UserEntity::isAtivo)
                .collect(Collectors.toList());
    }
}
