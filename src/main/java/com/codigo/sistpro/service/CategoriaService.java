package com.codigo.sistpro.service;

import com.codigo.sistpro.entity.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    Categoria guardar(Categoria categoria);
    List<Categoria> listar();
    Categoria obtenerPorId(Long id);
    Categoria actualizar(Long id, Categoria categoria);
    void eliminarCategoria(Long id);
}
