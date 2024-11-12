package com.biblioteca.web.repository;

import com.biblioteca.web.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);

    UserEntity findFistByUsername(String username);

    List<UserEntity> findByNomeCompletoContainingIgnoreCaseOrEmailContainingIgnoreCase(String nome, String email);

    @Query("SELECT u FROM usuarios u WHERE (upper(u.nomeCompleto) LIKE upper(concat('%', :search, '%')) " +
            "OR upper(u.email) LIKE upper(concat('%', :search, '%'))) " +
            "AND u.ativo = :ativo")
    List<UserEntity> searchByNomeOrEmailAndStatus(String search, boolean ativo);

    List<UserEntity> findByAtivo(boolean ativo);
}
