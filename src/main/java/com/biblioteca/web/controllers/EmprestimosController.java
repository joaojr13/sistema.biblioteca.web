package com.biblioteca.web.controllers;

import com.biblioteca.web.dtos.EmprestimoComReservaDto;
import com.biblioteca.web.dtos.EmprestimoSemReservaDto;
import com.biblioteca.web.models.Emprestimo;
import com.biblioteca.web.models.Reserva;
import com.biblioteca.web.models.UserEntity;
import com.biblioteca.web.security.SecurityUtil;
import com.biblioteca.web.services.EmprestimoService;
import com.biblioteca.web.services.LivroService;
import com.biblioteca.web.services.ReservaService;
import com.biblioteca.web.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
        List<Emprestimo> emprestimos = emprestimoService.findAllEmprestimosAtivos();
        model.addAttribute("emprestimos", emprestimos);
        return "emprestimos";
    }

    @GetMapping("/emprestimos/search")
    public String emprestimosSearch( @RequestParam(value = "search", required = false) String search,
                                     @RequestParam(value =  "status", required = false) String status,
                                     Model model) {
        String username = SecurityUtil.getSessionUser();

        if (username == null) {
            return "redirect:/emprestimos";
        }

        UserEntity user = userService.findByUsername(username);

        List<Emprestimo> emprestimos = emprestimoService.findEmprestimosByParams(user, search, status);

        model.addAttribute("emprestimos", emprestimos);

        return "emprestimos";
    }

    @GetMapping("/emprestimos/create")
    public String create(Model model) {
        String usernameSession = SecurityUtil.getSessionUser();

        UserEntity usuario = userService.findByUsername(usernameSession);

        model.addAttribute("funcionario", usuario);
        model.addAttribute("livrosDisponiveis", livroService.findAllLivrosDisponiveis());
        model.addAttribute("clientes", userService.findAllClientesAtivos());
        model.addAttribute("emprestimo", new EmprestimoSemReservaDto());

        return "emprestimos-create";
    }

    @GetMapping("/emprestimos/create/c-search")
    public String createEmprestimoWithCliente(
            @RequestParam(value = "cliente", required = false) Long clienteId,
            Model model) {

        if (clienteId != null) {
            model.addAttribute("cliente", userService.findById(clienteId));
        }

        model.addAttribute("livrosDisponiveis", livroService.findAllLivrosDisponiveis());
        String usernameSession = SecurityUtil.getSessionUser();
        UserEntity usuario = userService.findByUsername(usernameSession);
        model.addAttribute("funcionario", usuario);
        model.addAttribute("emprestimo", new EmprestimoSemReservaDto());

        return "emprestimos-create";
    }

    @PostMapping("/emprestimos/create")
    public String createEmprestimoSemReserva(@Valid @ModelAttribute("emprestimo") EmprestimoSemReservaDto emprestimoDto,
                                             BindingResult bindingResult,
                                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("emprestimo", emprestimoDto);
            return "emprestimos-create";
        }

        if (emprestimoDto.getIdCliente() == null || emprestimoDto.getIdFuncionario() == null) {
            return "redirect:/emprestimos";
        }

        if (emprestimoDto.getLivros() == null || emprestimoDto.getLivros().isEmpty()) {
            return "redirect:/emprestimos";
        }

        return "redirect:/emprestimos";
    }

    @GetMapping("/emprestimos/create-with-reserva")
    public String createWithReserva(Model model) {
        String usernameSession = SecurityUtil.getSessionUser();
        UserEntity usuario = userService.findByUsername(usernameSession);
        model.addAttribute("funcionario", usuario);
        model.addAttribute("livrosDisponiveis", livroService.findAllLivrosDisponiveis());
        model.addAttribute("clientes", userService.findAllClientesAtivos());
        model.addAttribute("emprestimo", new EmprestimoComReservaDto());
        return "emprestimos-create-with-reserva";
    }

    @GetMapping("/emprestimos/create/r-search")
    public String createEmprestimoWithReserva(
            @RequestParam(value = "reserva", required = false) Long reservaId,
            Model model) {

        if (reservaId != null) {
            model.addAttribute("reserva", reservaService.findById(reservaId));
        }

        model.addAttribute("livrosDisponiveis", livroService.findAllLivrosDisponiveis());
        String usernameSession = SecurityUtil.getSessionUser();
        UserEntity usuario = userService.findByUsername(usernameSession);
        model.addAttribute("funcionario", usuario);
        model.addAttribute("emprestimo", new EmprestimoComReservaDto());

        return "emprestimos-create-with-reserva";
    }

    @PostMapping("/emprestimos/create-with-reserva")
    public String createEmprestimoComReserva(@Valid @ModelAttribute("emprestimo") EmprestimoComReservaDto emprestimoDto,
                                             BindingResult bindingResult,
                                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("emprestimo", emprestimoDto);
            return "emprestimos-create";
        }

        if (emprestimoDto.getIdCliente() == null || emprestimoDto.getIdFuncionario() == null) {
            return "redirect:/emprestimos";
        }

        if (emprestimoDto.getLivros() == null || emprestimoDto.getLivros().isEmpty()) {
            return "redirect:/emprestimos";
        }

        emprestimoService.criarEmprestimoComReserva(emprestimoDto);

        return "redirect:/emprestimos";
    }
}
