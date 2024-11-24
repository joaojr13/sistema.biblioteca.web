package com.biblioteca.web.services.impl;

import com.biblioteca.web.dtos.ReservaDto;
import com.biblioteca.web.models.*;
import com.biblioteca.web.repository.LivroRepository;
import com.biblioteca.web.repository.ReservaRepository;
import com.biblioteca.web.repository.ReservaStatusRepository;
import com.biblioteca.web.services.ReservaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaServiceImpl implements ReservaService {
    private final ReservaRepository reservaRepository;
    private final LivroRepository livroRepository;
    private final ReservaStatusRepository reservaStatusRepository;

    public ReservaServiceImpl(ReservaRepository reservaRepository, LivroRepository livroRepository, ReservaStatusRepository reservaStatusRepository) {
        this.reservaRepository = reservaRepository;
        this.livroRepository = livroRepository;
        this.reservaStatusRepository = reservaStatusRepository;
    }

    @Override
    public void criarReserva(UserEntity cliente, ReservaDto reservaDto) {
        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setDataRetirada(reservaDto.dataRetirada);

        for (int i = 0; i < (long) reservaDto.getLivros().size(); i++) {
            Long livroId = reservaDto.getLivros().get(i);

            Livro livro = livroRepository.findById(livroId).get();

            reserva.getLivros().add(livro);
        }

        ReservaStatus status = reservaStatusRepository.findByNomeIgnoreCase("ATIVA");

        reserva.setStatus(status);

        reservaRepository.save(reserva);
    }

    @Override
    public Reserva findById(Long id) {
        return reservaRepository.findById(id).get();
    }

    @Override
    public void deleteReserva(Reserva reserva) {
        ReservaStatus status = reservaStatusRepository.findByNomeIgnoreCase("CANCELADA");
        reserva.setStatus(status);
        reservaRepository.save(reserva);
    }

    @Override
    public List<Reserva> findReservasByParams(UserEntity user, String search, String status) {
        boolean userCanViewEverything = user.getRoles().stream().anyMatch(Role::isAdmin) || user.getRoles().stream().anyMatch(Role::isFuncionario);

        if ((search == null || search.isEmpty()) && (status == null || status.isEmpty()) && userCanViewEverything) {
            return reservaRepository.findAllByStatusNomeNotIgnoreCase("Cancelada");
        } else if ((search == null || search.isEmpty()) && (status == null || status.isEmpty())) {
            return reservaRepository.findAllByClienteIdAndStatusNomeNotIgnoreCase(user.getId(), "Cancelada");
        } else if (status != null && !status.isEmpty() && (search == null || search.isEmpty())) {
            if (userCanViewEverything) {
                if(status.equalsIgnoreCase("todos")) return reservaRepository.findAll();
                return reservaRepository.findAllByStatusNomeContainingIgnoreCase(status);
            } else {
                return reservaRepository.findAllByClienteIdAndStatusNomeContainingIgnoreCase(user.getId(), status);
            }
        } else if (status != null && !status.isEmpty()) {
            if (userCanViewEverything) {
                if(status.equalsIgnoreCase("todos")) return reservaRepository.findByClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCase(search, search);
                return reservaRepository.findByStatusNomeContainingIgnoreCaseAndClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCase(status, search, search);
            } else {
                if(status.equalsIgnoreCase("todos")) return reservaRepository.findByClienteIdAndClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCase(user.getId(), search, search);
                return reservaRepository.findByClienteIdAndClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCaseAndStatusNomeContainingIgnoreCase(user.getId(), search, search, status);
            }
        } else {
            if (userCanViewEverything) {
                return reservaRepository.findAllByClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCase(search, search);
            } else {
                return reservaRepository.findByClienteIdAndClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCase(user.getId(), search, search);
            }
        }
    }

}
