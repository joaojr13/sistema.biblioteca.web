package com.biblioteca.web.controllers;

import com.biblioteca.web.dtos.LivroDto;
import com.biblioteca.web.services.LivroService;
import com.biblioteca.web.services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ReservaController {
    private final ReservaService reservaService;
    private final LivroService livrosService;

    @Autowired
    public ReservaController(ReservaService reservaService, LivroService livrosService) {
        this.reservaService = reservaService;
        this.livrosService = livrosService;
    }

    @GetMapping("/reservas")
    public String getReservas(Model model) {
        List<LivroDto> livrosDisponiveis = livrosService.findAllLivrosDisponiveis();
        model.addAttribute("livrosDisponiveis", livrosDisponiveis);
        return "reservas";
    }
}
