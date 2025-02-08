package com.codigo.sistpro.service.impl;


import com.codigo.sistpro.entity.Categoria;
import com.codigo.sistpro.entity.Producto;
import com.codigo.sistpro.repository.CategoriaRepository;
import com.codigo.sistpro.repository.ProductoRepository;
import com.codigo.sistpro.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    @Override
    public Producto guardar(Producto producto) {

        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            throw new RuntimeException("Debe proporcionar un ID de categoría válido.");
        }

        Categoria categoria = categoriaRepository.findById(producto.getCategoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada."));

        producto.setCategoria(categoria);

        if (producto.getCodigo() == null || producto.getCodigo().isEmpty()) {
            producto.setCodigo(generarCodigo(producto));
        }

        return productoRepository.save(producto);

    }


    @Override
    public List<Producto> listartodos() {
        return productoRepository.findAll();
    }

    @Override
    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getStock() > 0) {
            throw new RuntimeException("No se puede eliminar el producto porque aún tiene stock disponible.");
        }

        productoRepository.deleteById(id);
    }

    @Override
    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto actualizar(Long id, Producto productoActualizado) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (productoActualizado.getCategoria() == null) {
            throw new RuntimeException("Debe proporcionar una categoría válida.");
        }

        Categoria categoria = categoriaRepository.findById(productoActualizado.getCategoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada."));

        producto.setNombre(productoActualizado.getNombre());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setPrecio(productoActualizado.getPrecio());
        producto.setStock(productoActualizado.getStock());
        producto.setCategoria(categoria);

        // Generar un nuevo código
        producto.setCodigo(generarCodigo(producto));

        return productoRepository.save(producto);
    }


    private String generarCodigo(Producto producto) {
        String aActual = String.valueOf(LocalDate.now().getYear());
        String ultimosNumeros = aActual.substring(aActual.length() - 2);

        String baseCodigo = producto.getNombre().replaceAll("\\s+", "").substring(0, Math.min(3, producto.getNombre().length())).toUpperCase();
        String prefijoCodigo = baseCodigo + ultimosNumeros;

        Long count = productoRepository.countByCodigoStartingWith(prefijoCodigo);
        return prefijoCodigo + "-" + String.format("%03d", count + 1);
    }

}
