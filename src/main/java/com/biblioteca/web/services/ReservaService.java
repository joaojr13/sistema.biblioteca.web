package com.biblioteca.web.services;

import com.biblioteca.web.dtos.ReservaDto;
import com.biblioteca.web.models.Reserva;
import com.biblioteca.web.models.UserEntity;
import jakarta.validation.Valid;

import java.util.List;

public interface ReservaService {
    void criarReserva(UserEntity user, @Valid ReservaDto reservaDto);

    Reserva findById(Long id);

    void deleteReserva(Reserva reserva);

    List<Reserva> findReservasByParams(UserEntity user, String search, String status);
}
