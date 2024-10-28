package com.biblioteca.web.services;

import com.biblioteca.web.dtos.LivroDto;
import com.biblioteca.web.models.Livro;

import java.util.List;

public interface LivroService {
    List<LivroDto> findAllLivros();

    void salvarLivro(Livro livro);
}
