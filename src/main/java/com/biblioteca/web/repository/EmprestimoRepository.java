package com.biblioteca.web.repository;

import com.biblioteca.web.models.Emprestimo;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findAllByStatusNomeContaining(String statusNome);

    List<Emprestimo> findAllByStatusNomeNotIgnoreCase(String statusNome);

    List<Emprestimo> findAllByClienteIdAndStatusNomeNotIgnoreCase(Long clienteId, String statusNome);

    List<Emprestimo> findAllByStatusNomeContainingIgnoreCase(String statusNome);

    List<Emprestimo> findAllByClienteIdAndStatusNomeContainingIgnoreCase(Long clienteId, String statusNome);

    List<Emprestimo> findByClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCase(@NotEmpty(message = "O nome é obrigatório!") String clienteNomeCompleto, String livrosTitulo);

    List<Emprestimo> findByStatusNomeContainingIgnoreCaseAndClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCase(String statusNome, @NotEmpty(message = "O nome é obrigatório!") String clienteNomeCompleto, String livrosTitulo);

    List<Emprestimo> findByClienteIdAndClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCase(Long clienteId, @NotEmpty(message = "O nome é obrigatório!") String clienteNomeCompleto, String livrosTitulo);

    List<Emprestimo> findByClienteIdAndClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCaseAndStatusNomeContainingIgnoreCase(Long clienteId, @NotEmpty(message = "O nome é obrigatório!") String clienteNomeCompleto, String livrosTitulo, String statusNome);

    List<Emprestimo> findAllByClienteNomeCompletoContainingIgnoreCaseOrLivrosTituloContainingIgnoreCase(@NotEmpty(message = "O nome é obrigatório!") String clienteNomeCompleto, String livrosTitulo);

    List<Emprestimo> findAllByClienteId(Long id);
}
