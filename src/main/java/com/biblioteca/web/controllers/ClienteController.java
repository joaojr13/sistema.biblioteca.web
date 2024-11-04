package com.biblioteca.web.controllers;

import com.biblioteca.web.dtos.LivroDto;
import com.biblioteca.web.dtos.UsuarioDto;
import com.biblioteca.web.models.UserEntity;
import com.biblioteca.web.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class ClienteController {

    private final UserService userService;

    @Autowired
    public ClienteController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/clientes")
    public String clientes(Model model) {
        List<UserEntity> usuarios = userService.findAll();
        model.addAttribute("usuarios", usuarios);
        return "clientes";
    }

    @GetMapping("/clientes/{clienteId}/edit")
    public String editClienteForm(@PathVariable Long clienteId, Model model) {
        UserEntity usuario = userService.findById(clienteId);

        UsuarioDto usuarioDto = UsuarioDto.builder()
                .id(usuario.getId())
                .nomeCompleto(usuario.getNomeCompleto())
                .email(usuario.getEmail())
                .cpf(usuario.getCpf())
                .ativo(usuario.isAtivo())
                .telefone(usuario.getTelefone())
                .build();

        model.addAttribute("usuario", usuarioDto);
        return "clientes-edit";
    }

    @PostMapping("/clientes/{clienteId}/edit")
    public String updateLivro(@PathVariable Long clienteId,
                              @Valid @ModelAttribute("usuario") UsuarioDto usuario,
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
        existingUser.setAtivo(usuario.isAtivo());
        existingUser.setTelefone(usuario.getTelefone());

        userService.updateUser(existingUser);
        return "redirect:/clientes";
    }

    @GetMapping("/clientes/{clienteId}/delete")
    public String deleteCliente(@PathVariable Long clienteId) {
        userService.deleteById(clienteId);

        return "redirect:/clientes";
    }
}
