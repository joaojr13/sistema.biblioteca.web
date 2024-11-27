package com.biblioteca.web.repository;

import com.biblioteca.web.models.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findAllByStatusNomeContaining(String statusNome);
}
