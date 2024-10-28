package com.biblioteca.web.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LivroDto {
    private Long id;
    @NotEmpty(message = "O titulo do livro nao pode ser vazio")
    private String titulo;
    @NotEmpty(message = "O autor do livro nao pode ser vazio")
    private String autor;
    @NotEmpty(message = "A editora do livro nao pode ser vazio")
    private String editora;
    private String isbn;
    private String imageUrl;
    @NotEmpty(message = "O tipo do livro nao pode ser vazio")
    private String tipo;
    private String descricao;

    @Min(value = 1, message = "A quantidade de exemplares deve ser maior que 1")
    private int qtdExemplares;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
