package com.codigo.sistpro.repository;

import com.codigo.sistpro.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByNombre(String nombre);
    boolean existsById(Long id);
}
