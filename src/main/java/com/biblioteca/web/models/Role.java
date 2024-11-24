package com.biblioteca.web.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "roles")
@EqualsAndHashCode
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "roles")
    private List<UserEntity> usuarios = new ArrayList<>();

    public boolean isAdmin() {
        return name.equalsIgnoreCase("ADMIN");
    }

    public boolean isFuncionario() {
        return name.equalsIgnoreCase("FUNCIONARIO");
    }
}
