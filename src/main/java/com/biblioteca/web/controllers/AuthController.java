package com.biblioteca.web.controllers;

import com.biblioteca.web.models.UserEntity;
import com.biblioteca.web.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        UserEntity userDto = new UserEntity();
        model.addAttribute("user", userDto);
        return "register";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") UserEntity registrationDto, BindingResult bindingResult, Model model) {
        UserEntity existingUserEntityEmail = userService.findByEmail(registrationDto.getEmail());

        if (existingUserEntityEmail != null && existingUserEntityEmail.getEmail() != null && !existingUserEntityEmail.getEmail().isEmpty()) {
            bindingResult.rejectValue("email", "error.user", "Já existe um usuario com esse email/username");
        }

        UserEntity existingUserEntityName = userService.findByUsername(registrationDto.getUsername());

        if (existingUserEntityName != null && existingUserEntityName.getUsername() != null && !existingUserEntityName.getUsername().isEmpty()) {
            bindingResult.rejectValue("username", "error.user","Já existe um usuario com esse email/username");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", registrationDto);
            return "register";
        }

        userService.saveUser(registrationDto);
        return "redirect:/login?accountCreated=true";
    }
}
