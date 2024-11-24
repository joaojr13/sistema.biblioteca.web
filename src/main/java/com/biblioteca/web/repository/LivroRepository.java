package com.biblioteca.web.repository;

import com.biblioteca.web.models.Livro;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    Optional<Livro> findByTitulo(String titulo);
    @Query("SELECT l FROM Livro l WHERE l.titulo LIKE concat('%', :query, '%')")
    List<Livro> searchLivros(String query);

    @EntityGraph(attributePaths = {"reservas", "emprestimos"})
    Optional<Livro> findById(Long id);

    List<Livro> findAllByAtivoTrue();

    @Query("SELECT l FROM Livro l WHERE l.ativo = True AND l.titulo LIKE concat('%', :query, '%')")
    List<Livro> searchLivrosAtivos(String query);
}
