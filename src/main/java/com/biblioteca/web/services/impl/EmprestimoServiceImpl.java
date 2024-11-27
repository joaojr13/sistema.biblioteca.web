package com.biblioteca.web.services.impl;

import com.biblioteca.web.dtos.EmprestimoComReservaDto;
import com.biblioteca.web.dtos.EmprestimoSemReservaDto;
import com.biblioteca.web.models.*;
import com.biblioteca.web.repository.EmprestimoRepository;
import com.biblioteca.web.repository.EmprestimoStatusRepository;
import com.biblioteca.web.services.EmprestimoService;
import com.biblioteca.web.services.LivroService;
import com.biblioteca.web.services.ReservaService;
import com.biblioteca.web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmprestimoServiceImpl implements EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final EmprestimoStatusRepository statusRepository;
    private final UserService userService;
    private final ReservaService reservaService;
    private final LivroService livroService;

    @Autowired
    public EmprestimoServiceImpl(EmprestimoRepository emprestimoRepository, EmprestimoStatusRepository statusRepository, UserService userService, ReservaService reservaService, LivroService livroService) {
        this.emprestimoRepository = emprestimoRepository;
        this.statusRepository = statusRepository;
        this.userService = userService;
        this.reservaService = reservaService;
        this.livroService = livroService;
    }

    @Override
    public void criarEmprestimoSemReserva(EmprestimoSemReservaDto emprestimoDto) {
        Emprestimo emprestimo = new Emprestimo();

        UserEntity cliente = userService.findById(emprestimoDto.getIdCliente());
        UserEntity funcionario = userService.findById(emprestimoDto.getIdFuncionario());

        emprestimo.setCliente(cliente);
        emprestimo.setFuncionario(funcionario);

        for (int i = 0; i < (long) emprestimoDto.getLivros().size(); i++) {
            Long livroId = emprestimoDto.getLivros().get(i);

            Livro livro = livroService.findLivroById(livroId);

            emprestimo.getLivros().add(livro);
        }

        EmprestimoStatus status = statusRepository.findByNomeIgnoreCase("ATIVO");

        emprestimo.setStatus(status);

        emprestimo.setDataDevolucao(emprestimoDto.getDataDevolucao());

        emprestimoRepository.save(emprestimo);
    }

    @Override
    public void criarEmprestimoComReserva(EmprestimoComReservaDto emprestimoDto) {
        Emprestimo emprestimo = new Emprestimo();

        UserEntity cliente = userService.findById(emprestimoDto.getIdCliente());
        UserEntity funcionario = userService.findById(emprestimoDto.getIdFuncionario());

        if(emprestimoDto.getIdReserva() != null) {
            Reserva reserva = reservaService.findById(emprestimoDto.getIdReserva());
            emprestimo.setReserva(reserva);
        }

        emprestimo.setCliente(cliente);
        emprestimo.setFuncionario(funcionario);

        for (int i = 0; i < (long) emprestimoDto.getLivros().size(); i++) {
            Long livroId = emprestimoDto.getLivros().get(i);

            Livro livro = livroService.findLivroById(livroId);

            emprestimo.getLivros().add(livro);
        }

        EmprestimoStatus status = statusRepository.findByNomeIgnoreCase("ATIVO");

        emprestimo.setStatus(status);

        emprestimo.setDataDevolucao(emprestimoDto.getDataDevolucao());

        emprestimoRepository.save(emprestimo);


    }

    @Override
    public List<Emprestimo> findAllEmprestimosAtivos() {
        return emprestimoRepository.findAllByStatusNomeContaining("Ativo");
    }
}
