package com.biblioteca.web.repository;

import com.biblioteca.web.models.EmprestimoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmprestimoStatusRepository extends JpaRepository<EmprestimoStatus, Long> {
    EmprestimoStatus findByNomeIgnoreCase(String nome);
}
