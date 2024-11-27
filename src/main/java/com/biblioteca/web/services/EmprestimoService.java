package com.biblioteca.web.services;

import com.biblioteca.web.dtos.EmprestimoComReservaDto;
import com.biblioteca.web.dtos.EmprestimoSemReservaDto;
import com.biblioteca.web.models.Emprestimo;

import java.util.List;

public interface EmprestimoService {
    void criarEmprestimoSemReserva(EmprestimoSemReservaDto emprestimoDto);
    void criarEmprestimoComReserva(EmprestimoComReservaDto emprestimoDto);

    List<Emprestimo> findAllEmprestimosAtivos();
}
