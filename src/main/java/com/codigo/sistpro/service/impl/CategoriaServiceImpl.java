package com.codigo.sistpro.service.impl;

import com.codigo.sistpro.entity.Categoria;
import com.codigo.sistpro.repository.CategoriaRepository;
import com.codigo.sistpro.repository.ProductoRepository;
import com.codigo.sistpro.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    @Override
    public Categoria guardar(Categoria categoria) {
        String categoriaNueva = categoria.getNombre().toUpperCase();

        if (categoriaRepository.existsByNombre(categoriaNueva)) {
            throw new RuntimeException("Ya existe una categoría con este nombre.");
        }

        categoria.setNombre(categoriaNueva);
        return categoriaRepository.save(categoria);
    }

    @Override
    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    @Override
    public Categoria actualizar(Long id, Categoria categoria) {
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada."));

        String nuevoNombre = categoria.getNombre().toUpperCase();

        // Verificar si ya existe otra categoría con el mismo nombre
        if (categoriaRepository.existsByNombre(nuevoNombre) && !categoriaExistente.getNombre().equalsIgnoreCase(nuevoNombre)) {
            throw new RuntimeException("Ya existe una categoría con este nombre.");
        }

        categoriaExistente.setNombre(nuevoNombre);
        return categoriaRepository.save(categoriaExistente);
    }

    @Override
    public void eliminarCategoria(Long id) {
        if (productoRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar la categoría. Hay productos asociados.");
        }
        categoriaRepository.deleteById(id);
    }


}
