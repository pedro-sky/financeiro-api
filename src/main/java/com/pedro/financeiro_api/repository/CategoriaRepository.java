package com.pedro.financeiro_api.repository;

import com.pedro.financeiro_api.model.Categoria;
import com.pedro.financeiro_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByUsuario(Usuario usuario);

    Optional<Categoria> findByIdAndUsuario(Long id, Usuario usuario);

    boolean existsByNomeAndUsuario(String nome, Usuario usuario);
}