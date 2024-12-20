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
    private final LivroRepository livroRepository;

    public LivroServiceImpl(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Override
    public List<LivroDto> findAllLivros() {
        List<Livro> livros = livroRepository.findAllByAtivoTrue();
        return livros.stream().map(this::mapToLivroDto).collect(Collectors.toList());
    }

    @Override
    public void salvarLivro(LivroDto livroDto) {
        Livro livro = mapToLivro(livroDto);
        livroRepository.save(livro);
    }

    @Override
    public Livro findLivroById(Long livroId) {
        Livro livro = livroRepository.findById(livroId).get();
        return livro;
    }

    @Override
    public void updateLivro(Livro livro) {
        livroRepository.save(livro);
    }

    @Override
    public List<LivroDto> searchLivros(String query) {
        List<Livro> livros = livroRepository.searchLivros(query);
        return livros.stream().map(this::mapToLivroDto).collect(Collectors.toList());
    }

    @Override
    public List<LivroDto> searchLivrosAtivos(String query) {
        List<Livro> livros = livroRepository.searchLivrosAtivos(query);
        return livros.stream().map(this::mapToLivroDto).collect(Collectors.toList());
    }

    @Override
    public List<LivroDto> findAllLivrosDisponiveis() {
        return livroRepository.findAll()
                .stream()
                .filter(Livro::isAtivo)
                .filter(Livro::isDisponivel)
                .map(this::mapToLivroDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteLivro(Long livroId) {
        Livro livro = livroRepository.findById(livroId).isPresent() ? livroRepository.findById(livroId).get() : null;

        if(livro == null) { return; }

        livro.setAtivo(false);
        livroRepository.save(livro);
    }

    private Livro mapToLivro(LivroDto livro) {
        return Livro.builder()
                .id(livro.getId())
                .titulo(livro.getTitulo())
                .autores(livro.getAutores())
                .isbn(livro.getIsbn())
                .editora(livro.getEditora())
                .qtdExemplares(livro.getQtdExemplares())
                .imageUrl(livro.getImageUrl())
                .tipo(livro.getTipo())
                .descricao(livro.getDescricao())
                .build();
    }

    private LivroDto mapToLivroDto(Livro livro) {
        return LivroDto.builder()
                .id(livro.getId())
                .titulo(livro.getTitulo())
                .autores(livro.getAutores())
                .isbn(livro.getIsbn())
                .editora(livro.getEditora())
                .qtdExemplares(livro.getQtdExemplares())
                .imageUrl(livro.getImageUrl())
                .tipo(livro.getTipo())
                .descricao(livro.getDescricao())
                .createdOn(livro.getCreatedOn())
                .updatedOn(livro.getUpdatedOn())
                .qtdDisponivel(livro.getQuantidadeDisponivel())
                .build();
    }
}
