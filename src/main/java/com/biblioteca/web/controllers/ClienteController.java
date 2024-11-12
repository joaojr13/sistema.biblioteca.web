package com.biblioteca.web.controllers;

import com.biblioteca.web.dtos.UsuarioDto;
import com.biblioteca.web.models.UserEntity;
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
            @RequestParam(value =  "status", required = false) String status,
            Model model) {

        List<UserEntity> usuarios = userService.pesquisarClientes(search, status);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("search", search);
        model.addAttribute("status", status);

        return "clientes";
    }

    @GetMapping("/clientes/{clienteId}/edit")
    public String editClienteForm(@PathVariable Long clienteId, Model model) {
        UserEntity usuario = userService.findById(clienteId);

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", userService.findAllRoles());
        return "clientes-edit";
    }

    @PostMapping("/clientes/{clienteId}/edit")
    public String updateCliente(@PathVariable Long clienteId,
                              @Valid @ModelAttribute("usuario") UserEntity usuario,
                              BindingResult bindingResult, Model model)
    {
        if(bindingResult.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "clientes-edit";
        }

        UserEntity existingUser = userService.findById(clienteId);

        existingUser.setNomeCompleto(usuario.getNomeCompleto());
        existingUser.setEmail(usuario.getEmail());
        existingUser.setCpf(usuario.getCpf());
        existingUser.setTelefone(usuario.getTelefone());
        existingUser.setAtivo(usuario.isAtivo());
        existingUser.setRoles(usuario.getRoles());

        userService.updateUser(existingUser);
        return "redirect:/clientes";
    }

    @GetMapping("/clientes/{clienteId}/delete")
    public String deleteCliente(@PathVariable Long clienteId) {
        userService.deleteById(clienteId);

        return "redirect:/clientes";
    }
}
