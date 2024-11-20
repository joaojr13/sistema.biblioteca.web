package com.biblioteca.web.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private UserEntity cliente;

    private LocalDateTime dataRetirada;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @UpdateTimestamp
    private LocalDateTime finishedOn;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private ReservaStatus status;

    @ManyToMany
    @JoinTable(
            name = "reserva_livro",
            joinColumns = @JoinColumn(name = "reserva_id"),
            inverseJoinColumns = @JoinColumn(name = "livro_id")
    )
    private Set<Livro> livros = new HashSet<>();

    public boolean isAtivo(){
        return status.getNome().equalsIgnoreCase("ATIVA");
    }
}
