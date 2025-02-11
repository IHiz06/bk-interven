package com.codigo.sistpro.repository;

import com.codigo.sistpro.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Long countByCategoriaId(Long categoriaId);
    Optional<Producto> findByCodigo(String codigo);
    Long countByCodigoStartingWith(String prefijo);
    boolean existsByNombre(String nombre);
}
