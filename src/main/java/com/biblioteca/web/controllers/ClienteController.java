package com.biblioteca.web.controllers;

import com.biblioteca.web.dtos.UsuarioDto;
import com.biblioteca.web.models.Role;
import com.biblioteca.web.models.UserEntity;
import com.biblioteca.web.security.SecurityUtil;
import com.biblioteca.web.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ClienteController {

    private final UserService userService;

    @Autowired
    public ClienteController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/clientes")
    public String getClientes(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "status", required = false) String status,
            Model model) {

        List<UserEntity> usuarios = userService.pesquisarClientes(search, status);

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("search", search);
        model.addAttribute("status", status);

        return "clientes";
    }

    @GetMapping("/clientes/{clienteId:[0-9]+}/edit")
    public String editClienteForm(@PathVariable Long clienteId, Model model) {
        UserEntity usuario = userService.findById(clienteId);

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", userService.findAllRoles());
        return "clientes-edit";
    }

    @GetMapping("/clientes/{username}/edit-user")
    public String editClienteFormByUsername(@PathVariable String username, Model model) {
        UserEntity usuario = userService.findByUsername(username);

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", userService.findAllRoles());

        return "clientes-edit";
    }

    @PostMapping("/clientes/{clienteId}/edit")
    public String updateCliente(@PathVariable Long clienteId,
                                @Valid @ModelAttribute("usuario") UserEntity usuario,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "clientes-edit";
        }

        UserEntity existingUser = userService.findById(clienteId);

        if (existingUser.getNomeCompleto() == null) {
            if (usuario.getNomeCompleto() != null && !usuario.getNomeCompleto().isEmpty()) {
                existingUser.setNomeCompleto(usuario.getNomeCompleto());
            }
        } else if (usuario.getNomeCompleto() != null && !usuario.getNomeCompleto().isEmpty() &&
                !usuario.getNomeCompleto().equals(existingUser.getNomeCompleto())) {
            existingUser.setNomeCompleto(usuario.getNomeCompleto());
        }

        if (existingUser.getEmail() == null) {
            if (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
                existingUser.setEmail(usuario.getEmail());
            }
        } else if (usuario.getEmail() != null && !usuario.getEmail().isEmpty() &&
                !usuario.getEmail().equals(existingUser.getEmail())) {
            existingUser.setEmail(usuario.getEmail());
        }

        if (existingUser.getCpf() == null) {
            if (usuario.getCpf() != null && !usuario.getCpf().isEmpty()) {
                existingUser.setCpf(usuario.getCpf());
            }
        } else if (usuario.getCpf() != null && !usuario.getCpf().isEmpty() &&
                !usuario.getCpf().equals(existingUser.getCpf())) {
            existingUser.setCpf(usuario.getCpf());
        }

        if (existingUser.getTelefone() == null) {
            if (usuario.getTelefone() != null && !usuario.getTelefone().isEmpty()) {
                existingUser.setTelefone(usuario.getTelefone());
            }
        } else if (usuario.getTelefone() != null && !usuario.getTelefone().isEmpty() &&
                !usuario.getTelefone().equals(existingUser.getTelefone())) {
            existingUser.setTelefone(usuario.getTelefone());
        }



        String usernameSession = SecurityUtil.getSessionUser();
        UserEntity userSession = userService.findByUsername(usernameSession);

        if (userSession.getRoles().stream().anyMatch(Role::isAdmin) || userSession.getRoles().stream().anyMatch(Role::isFuncionario)) {
            if (existingUser.isAtivo() != usuario.isAtivo()) {
                existingUser.setAtivo(usuario.isAtivo());
            }
        }

        if (existingUser.getRoles() == null) {
            if (usuario.getRoles() != null && !usuario.getRoles().isEmpty()) {
                existingUser.setRoles(usuario.getRoles());
            }
        } else if (usuario.getRoles() != null && !usuario.getRoles().isEmpty() &&
                !existingUser.getRoles().equals(usuario.getRoles())) {
            existingUser.setRoles(usuario.getRoles());
        }


        userService.updateUser(existingUser);

        if (existingUser.getRoles().stream().allMatch(role -> role.getName().equals("CLIENTE"))) {
            return "redirect:/livros";
        }

        return "redirect:/clientes";
    }

    @GetMapping("/clientes/{clienteId}/delete")
    public String deleteCliente(@PathVariable Long clienteId) {
        userService.deleteById(clienteId);

        return "redirect:/clientes";
    }
}
