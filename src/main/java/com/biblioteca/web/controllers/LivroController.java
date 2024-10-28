package com.biblioteca.web.controllers;

import com.biblioteca.web.dtos.LivroDto;
import com.biblioteca.web.models.Livro;
import com.biblioteca.web.services.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LivroController {
    private LivroService livroService;

    @Autowired
    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping("/livros")
    public String listLivros(Model model){
        List<LivroDto> livros = livroService.findAllLivros();
        model.addAttribute("livros", livros);
        return "livros-list";
    }

    @GetMapping("/livros/new")
    public String createLivroForm(Model model){
        Livro livro = new Livro();
        model.addAttribute("livro", livro);
        return "livros-create";
    }
}
