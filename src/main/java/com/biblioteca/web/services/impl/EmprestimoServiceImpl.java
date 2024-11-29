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

        UserEntity cliente = userService.findByUsername(emprestimoDto.getUsernameCliente());
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

        Reserva reserva = reservaService.findById(emprestimoDto.getIdReserva());
        emprestimo.setReserva(reserva);

        emprestimoRepository.save(emprestimo);

        reservaService.finalizarReserva(reserva);
    }

    @Override
    public List<Emprestimo> findAllEmprestimosAtivos() {
        return emprestimoRepository.findAllByStatusNomeContaining("Ativo");
    }

    @Override
    public List<Emprestimo> findEmprestimosByParams(UserEntity user, String search, String status) {
        boolean userCanViewEverything = user.getRoles().stream().anyMatch(Role::isAdmin) || user.getRoles().stream().anyMatch(Role::isFuncionario);

        if ((search == null || search.isEmpty()) && (status == null || status.isEmpty()) && userCanViewEverything) {
            return emprestimoRepository.findAllByStatusNomeNotIgnoreCase("Cancelada");
        } else if ((search == null || search.isEmpty()) && (status == null || status.isEmpty())) {
            return emprestimoRepository.findAllByClienteIdAndStatusNomeNotIgnoreCase(user.getId(), "Cancelada");
        } else if (status != null && !status.isEmpty() && (search == null || search.isEmpty())) {
            if (userCanViewEverything) {
                if(status.equalsIgnoreCase("todos")) return emprestimoRepository.findAll();
                return emprestimoRepository.findAllByStatusNomeContainingIgnoreCase(status);
            } else {
                if(status.equalsIgnoreCase("todos")) return emprestimoRepository.findAllByClienteId(user.getId());
                return emprestimoRepository.findAllByClienteIdAndStatusNomeContainingIgnoreCase(user.getId(), status);
            }
        } else if (status != null && !status.isEmpty()) {
            if (userCanViewEverything) {
                if(status.equalsIgnoreCase("todos")) return emprestimoRepository.findByClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCase(search, search);
                return emprestimoRepository.findByStatusNomeContainingIgnoreCaseAndClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCase(status, search, search);
            } else {
                if(status.equalsIgnoreCase("todos")) return emprestimoRepository.findByClienteIdAndClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCase(user.getId(), search, search);
                return emprestimoRepository.findByClienteIdAndClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCaseAndStatusNomeContainingIgnoreCase(user.getId(), search, search, status);
            }
        } else {
            if (userCanViewEverything) {
                return emprestimoRepository.findAllByClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCase(search, search);
            } else {
                return emprestimoRepository.findByClienteIdAndClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCase(user.getId(), search, search);
            }
        }
    }

    @Override
    public List<Emprestimo> findAllEmprestimosAtivosByUser(UserEntity user) {
        return emprestimoRepository.findAllByClienteIdAndStatusNomeContainingIgnoreCase(user.getId(), "Ativo");
    }

    @Override
    public Emprestimo findById(Long emprestimoId) {
        return emprestimoRepository.findById(emprestimoId).orElse(null);
    }

    @Override
    public void devolver(Emprestimo emprestimo) {
        EmprestimoStatus status = statusRepository.findByNomeIgnoreCase("FINALIZADO");
        emprestimo.setStatus(status);
        emprestimoRepository.save(emprestimo);
    }

    @Override
    public void cancelar(Emprestimo emprestimo) {
        EmprestimoStatus status = statusRepository.findByNomeIgnoreCase("CANCELADO");
        emprestimo.setStatus(status);
        emprestimoRepository.save(emprestimo);
    }
}
