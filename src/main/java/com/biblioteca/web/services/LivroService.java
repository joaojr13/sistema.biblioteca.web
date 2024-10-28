package com.biblioteca.web.services;

import com.biblioteca.web.dtos.LivroDto;

import java.util.List;

public interface LivroService {
    List<LivroDto> findAllLivros();

    void salvarLivro(LivroDto livro);

    LivroDto findClubById(Long livroId);

    void updateLivro(LivroDto livro);
}
