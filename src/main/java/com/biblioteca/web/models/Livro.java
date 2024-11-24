package com.biblioteca.web.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "livro_autores", joinColumns = @JoinColumn(name = "livro_id"))
    @Column(name = "autor")
    private List<String> autores = new ArrayList<>();

    private String editora;
    private String isbn;

    @Column(length = 2000)
    private String descricao;
    private String imageUrl;
    private String tipo;
    private int qtdExemplares;
    @CreationTimestamp
    private LocalDateTime createdOn;
    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @Column(nullable = false, columnDefinition = "BIT DEFAULT 1")
    private boolean ativo = true;

    @ManyToMany(mappedBy = "livros", fetch = FetchType.EAGER)
    private List<Reserva> reservas = new ArrayList<>();

    @ManyToMany(mappedBy = "livros", fetch = FetchType.EAGER)
    private List<Emprestimo> emprestimos = new ArrayList<>();

    public int getQuantidadeDisponivel() {
        long emprestimosAtivos = emprestimos.stream()
                .filter(emprestimo -> !emprestimo.isFinalizado())
                .count();

        long reservasAtivas = reservas.stream()
                .filter(Reserva::isAtivo)
                .count();

        return (int) (qtdExemplares - (emprestimosAtivos + reservasAtivas));
    }

    /**
     * Verifica se o livro está disponível para empréstimo.
     * Retorna `true` se a quantidade de exemplares disponíveis for maior que o número de exemplares emprestados.
     */
    public boolean isDisponivel() {
        long emprestimosAtivos = emprestimos.stream()
                .filter(emprestimo -> !emprestimo.isFinalizado())
                .count();

        long reservasAtivas = reservas.stream()
                .filter(Reserva::isAtivo)
                .count();

        return (qtdExemplares > (emprestimosAtivos + reservasAtivas));
    }
}

