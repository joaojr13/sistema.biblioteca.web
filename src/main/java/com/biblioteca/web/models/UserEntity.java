package com.biblioteca.web.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "usuarios")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "O username é obrigatório!")
    private String username;

    @NotEmpty(message = "O nome é obrigatório!")
    private String nomeCompleto;

    @NotEmpty(message = "O e-mail é obrigatório!")
    @Email(message = "O formato do e-mail está inválido!")
    private String email;

    @NotEmpty(message = "Favor informar uma senha!")
    private String password;

    private String telefone;

    private String cpf;

    @Column(nullable = false, columnDefinition = "BIT DEFAULT 1")
    private boolean ativo;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "usuarios_roles",
            joinColumns = {@JoinColumn(name = "usuario_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private List<Role> roles = new ArrayList<>();

    @OneToMany(
            mappedBy = "cliente",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Reserva> reservas = new ArrayList<>();
}
