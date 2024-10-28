package com.biblioteca.web.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LivroDto {
    private Long id;
    private String titulo;
    private String autor;
    private String editora;
    private String isbn;
    private String imageUrl;
    private String tipo;
    private String descricao;
    private int qtdExamplares;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
