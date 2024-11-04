package com.biblioteca.web.services;

import com.biblioteca.web.dtos.LivroDto;

import java.util.List;

public interface LivroService {
    List<LivroDto> findAllLivros();

    void salvarLivro(LivroDto livro);

    LivroDto findLivroById(Long livroId);

    void updateLivro(LivroDto livro);

    List<LivroDto> searchLivros(String query);
}
