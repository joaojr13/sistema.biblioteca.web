package com.biblioteca.web.services.impl;

import com.biblioteca.web.dtos.LivroDto;
import com.biblioteca.web.models.Livro;
import com.biblioteca.web.repository.LivroRepository;
import com.biblioteca.web.services.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LivroServiceImpl implements LivroService {
    private LivroRepository livroRepository;

    @Autowired
    public LivroServiceImpl(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Override
    public List<LivroDto> findAllLivros() {
        List<Livro> livros = livroRepository.findAll();
        return livros.stream().map(this::mapToLivroDto).collect(Collectors.toList());
    }

    private LivroDto mapToLivroDto(Livro livro) {
        return LivroDto.builder()
                .id(livro.getId())
                .titulo(livro.getTitulo())
                .autor(livro.getAutor())
                .isbn(livro.getIsbn())
                .editora(livro.getEditora())
                .qtdExamplares(livro.getQtdExamplares())
                .imageUrl(livro.getImageUrl())
                .tipo(livro.getTipo())
                .descricao(livro.getDescricao())
                .createdOn(livro.getCreatedOn())
                .updatedOn(livro.getUpdatedOn())
                .build();
    }
}
