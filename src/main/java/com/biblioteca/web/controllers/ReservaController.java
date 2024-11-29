package com.biblioteca.web.controllers;

import com.biblioteca.web.dtos.LivroDto;
import com.biblioteca.web.dtos.ReservaDto;
import com.biblioteca.web.models.Reserva;
import com.biblioteca.web.models.Role;
import com.biblioteca.web.models.UserEntity;
import com.biblioteca.web.security.SecurityUtil;
import com.biblioteca.web.services.LivroService;
import com.biblioteca.web.services.ReservaService;
import com.biblioteca.web.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
public class ReservaController {
    private final ReservaService reservaService;
    private final LivroService livrosService;
    private final UserService userService;

    @Autowired
    public ReservaController(ReservaService reservaService, LivroService livrosService, UserService userService) {
        this.reservaService = reservaService;
        this.livrosService = livrosService;
        this.userService = userService;
    }

    @GetMapping("/reservas")
    public String getReservas(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value =  "status", required = false) String status,
            Model model) {
        String username = SecurityUtil.getSessionUser();

        if (username == null) {
            return "redirect:/livros";
        }

        UserEntity user = userService.findByUsername(username);
        boolean userCanViewEverything = user.getRoles().stream().anyMatch(Role::isAdmin) || user.getRoles().stream().anyMatch(Role::isFuncionario);

        if (userCanViewEverything) {
            List<Reserva> reservas = reservaService.findReservasByParams(user, search, status);

            model.addAttribute("reservas", reservas);

            return "reservas";
        }

        List<Reserva> reservas = reservaService.findReservasByParams(user, search, status).stream().filter(e -> Objects.equals(e.getCliente().getId(), user.getId())).toList();

        model.addAttribute("reservas", reservas);

        return "reservas";
    }

    @GetMapping("/reservas/new")
    public String createReservaForm(Model model) {
        List<LivroDto> livrosDisponiveis = livrosService.findAllLivrosDisponiveis();

        String username = SecurityUtil.getSessionUser();

        if (username == null) {
            return "redirect:/reservas";
        }

        UserEntity user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/reservas";
        }

        model.addAttribute("user", user);
        model.addAttribute("livrosDisponiveis", livrosDisponiveis);

        ReservaDto reservaDto = new ReservaDto();
        model.addAttribute("reserva", reservaDto);

        return "reservas-create";
    }

    @PostMapping("/reservas/new")
    public String createReserva(@Valid @ModelAttribute("reserva") ReservaDto reservaDto,
                                BindingResult bindingResult,
                                Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("reserva", reservaDto);
            return "reservas-create";
        }

        String username = SecurityUtil.getSessionUser();

        if (username == null) {
            return "redirect:/reservas";
        }

        UserEntity user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/reservas";
        }

        reservaService.criarReserva(user, reservaDto);

        return "redirect:/reservas";

    }

    @GetMapping("/reservas/{reservaId}/delete")
    public String deleteReserva(@PathVariable Long reservaId) {
        Reserva reserva = reservaService.findById(reservaId);

        if(reserva == null) {
            return "redirect:/reservas";
        }

        reservaService.deleteReserva(reserva);

        return "redirect:/reservas";
    }
}
