package com.biblioteca.web.services;

import com.biblioteca.web.dtos.EmprestimoComReservaDto;
import com.biblioteca.web.dtos.EmprestimoSemReservaDto;
import com.biblioteca.web.models.Emprestimo;
import com.biblioteca.web.models.UserEntity;

import java.util.List;

public interface EmprestimoService {
    void criarEmprestimoSemReserva(EmprestimoSemReservaDto emprestimoDto);
    void criarEmprestimoComReserva(EmprestimoComReservaDto emprestimoDto);

    List<Emprestimo> findAllEmprestimosAtivos();

    List<Emprestimo> findEmprestimosByParams(UserEntity user, String search, String status);
}
