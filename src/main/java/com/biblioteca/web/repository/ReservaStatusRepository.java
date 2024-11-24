package com.biblioteca.web.repository;

import com.biblioteca.web.models.ReservaStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaStatusRepository extends JpaRepository<ReservaStatus, Long> {
    ReservaStatus findByNomeIgnoreCase(String nome);
}
