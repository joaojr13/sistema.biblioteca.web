package com.biblioteca.web.repository;

import com.biblioteca.web.models.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findAllByClienteIdAndStatusNomeNotIgnoreCase(Long clienteId, String statusNome);

    List<Reserva> findAllByStatusNomeNotIgnoreCase(String statusNome);

    List<Reserva> findAllByStatusNomeContainingIgnoreCase(String statusNome);

    List<Reserva> findAllByClienteIdAndStatusNomeContainingIgnoreCase(Long clienteId, String statusNome);

    List<Reserva> findByClienteIdAndClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCaseAndStatusNomeContainingIgnoreCase(Long clienteId, String nomeCompleto, String livrosTitulo, String statusNome);

    List<Reserva> findByStatusNomeContainingIgnoreCaseAndClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCase(String statusNome, String nomeCompleto, String livrosTitulo);

    List<Reserva> findByClienteIdAndClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCase(Long clienteId, String nomeCompleto, String livrosTitulo);

    List<Reserva> findAllByClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCase(String nomeCompleto, String livrosTitulo);

    List<Reserva> findByClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCase(String nomeCompleto, String livrosTitulo);
}
