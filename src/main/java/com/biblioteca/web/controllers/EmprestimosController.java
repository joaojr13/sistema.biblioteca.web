package com.biblioteca.web.controllers;

import com.biblioteca.web.models.Reserva;
import com.biblioteca.web.models.UserEntity;
import com.biblioteca.web.security.SecurityUtil;
import com.biblioteca.web.services.EmprestimoService;
import com.biblioteca.web.services.LivroService;
import com.biblioteca.web.services.ReservaService;
import com.biblioteca.web.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmprestimosController {
    private final ReservaService reservaService;
    private final UserService userService;
    private final LivroService livroService;
    private final EmprestimoService emprestimoService;

    public EmprestimosController(ReservaService reservaService, UserService userService, LivroService livroService, EmprestimoService emprestimoService) {
        this.reservaService = reservaService;
        this.userService = userService;
        this.livroService = livroService;
        this.emprestimoService = emprestimoService;
    }


    @GetMapping("/emprestimos")
    public String emprestimos(Model model) {
        return "emprestimos";
    }

    @GetMapping("/emprestimos/create")
    public String create(Model model) {
        String usernameSession = SecurityUtil.getSessionUser();

        UserEntity usuario = userService.findByUsername(usernameSession);

        model.addAttribute("reserva", null);
        model.addAttribute("funcionario", usuario);
        model.addAttribute("livrosDisponiveis", livroService.findAllLivrosDisponiveis());
        model.addAttribute("clientes", userService.findAllClientes());

        return "emprestimos-create";
    }

    @GetMapping("/emprestimos/create/search")
    public String searchReserva(@RequestParam(value = "reserva") Long reservaId, Model model ) {
        Reserva reserva = reservaService.findById(reservaId);

        if(reserva == null) {
            model.addAttribute("reservaEncontrada", false);
            return "redirect:/emprestimos-create";
        }

        model.addAttribute("reserva", reserva);
        model.addAttribute("cliente", reserva.getCliente());
        model.addAttribute("livrosDisponiveis", livroService.findAllLivrosDisponiveis());

        String usernameSession = SecurityUtil.getSessionUser();

        UserEntity usuario = userService.findByUsername(usernameSession);

        model.addAttribute("funcionario", usuario);

        return "emprestimos-create";
    }
}
