package com.biblioteca.web.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioDto {
    private Long id;
    private String nomeCompleto;
    private String email;
    private String telefone;
    private String cpf;
    private boolean ativo;
}
