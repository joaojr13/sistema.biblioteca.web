package com.biblioteca.web.controllers;

import com.biblioteca.web.dtos.LivroDto;
import com.biblioteca.web.models.Livro;
import com.biblioteca.web.services.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LivroController {
    private LivroService livroService;

    @Autowired
    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping("/livros")
    public String listLivros(Model model) {
        List<LivroDto> livros = livroService.findAllLivros();
        model.addAttribute("livros", livros);
        return "livros";
    }

    @GetMapping("/livros/new")
    public String createLivroForm(Model model) {
        Livro livro = new Livro();
        model.addAttribute("livro", livro);
        return "livros-create";
    }

    @PostMapping("/livros/new")
    public String saveLivro(@ModelAttribute("livro") LivroDto livroDto, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()){
            model.addAttribute("livro", livroDto);
            return "livros-create";
        }

        livroService.salvarLivro(livroDto);
        return "redirect:/livros";
    }

    @GetMapping("/livros/{livroId}/edit")
    public String editLivroForm(@PathVariable Long livroId, Model model) {
        Livro livro = livroService.findLivroById(livroId);
        model.addAttribute("livro", livro);
        return "livros-edit";
    }

    @PostMapping("/livros/{livroId}/edit")
    public String updateLivro(@PathVariable Long livroId,
                              @Valid @ModelAttribute("livro") Livro livro,
                              BindingResult bindingResult, Model model)
    {

        if(bindingResult.hasErrors()) {
            model.addAttribute("livro", livro);
            return "livros-edit";
        }

        livro.setId(livroId);

        livroService.updateLivro(livro);
        return "redirect:/livros";
    }

    @GetMapping("/livros/{livroId}/details")
    public String detailsLivro(@PathVariable Long livroId, Model model) {
        Livro livro = livroService.findLivroById(livroId);
        model.addAttribute("livro", livro);
        return "livros-details";
    }

    @GetMapping("/livros/search")
    public String searchLivros(@RequestParam(value = "query") String query, Model model) {
        List<LivroDto> livrosDto = livroService.searchLivros(query);
        model.addAttribute("livros", livrosDto);
        return "livros";
    }
}
