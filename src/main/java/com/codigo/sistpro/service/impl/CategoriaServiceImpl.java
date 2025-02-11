package com.codigo.sistpro.service.impl;

import com.codigo.sistpro.entity.Categoria;
import com.codigo.sistpro.exception.exp.NombreDuplicadoException;
import com.codigo.sistpro.exception.exp.RecursoNoEncontradoException;
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
    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }


    @Override
    public void eliminarCategoria(Long id) {
        if (productoRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar la categoría. Hay productos asociados.");
        }
        categoriaRepository.deleteById(id);
    }

    @Override
    public Categoria guardar(Categoria categoria) {
        validarCategoria(categoria);
        categoria.setNombre(categoria.getNombre().toUpperCase());
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria actualizar(Long id, Categoria categoria) {
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada."));

        validarCategoria(categoria);

        String nuevoNombre = categoria.getNombre().toUpperCase();
        if (!categoriaExistente.getNombre().equalsIgnoreCase(nuevoNombre)) {
            if (categoriaRepository.existsByNombre(nuevoNombre)) {
                throw new NombreDuplicadoException("Ya existe una categoría con este nombre.");
            }
            categoriaExistente.setNombre(nuevoNombre);
        }

        return categoriaRepository.save(categoriaExistente);
    }

    /**
     * Método para validar que la categoría no tenga nombre vacío y no esté repetida.
     */
    private void validarCategoria(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío.");
        }

        String nombreUpper = categoria.getNombre().toUpperCase();
        if (categoriaRepository.existsByNombre(nombreUpper)) {
            throw new NombreDuplicadoException("Ya existe una categoría con este nombre.");
        }
    }


}
