package com.biblioteca.web.services;

import com.biblioteca.web.dtos.LivroDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface LivroService {
    List<LivroDto> findAllLivros();
}
