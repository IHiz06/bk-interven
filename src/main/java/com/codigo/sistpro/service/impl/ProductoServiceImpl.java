package com.codigo.sistpro.service.impl;


import com.codigo.sistpro.entity.Categoria;
import com.codigo.sistpro.entity.Producto;
import com.codigo.sistpro.exception.exp.NombreDuplicadoException;
import com.codigo.sistpro.exception.exp.RecursoNoEncontradoException;
import com.codigo.sistpro.repository.CategoriaRepository;
import com.codigo.sistpro.repository.ProductoRepository;
import com.codigo.sistpro.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public Producto guardar(Producto producto, MultipartFile imagen) throws IOException {
        if (imagen != null && !imagen.isEmpty()) {
            String imagenUrl = cloudinaryService.subirImagen(imagen);
            producto.setImagenUrl(imagenUrl);
        }

        validarProducto(producto);

        producto.setCategoria(categoriaRepository.findById(producto.getCategoria().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada.")));

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
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto con ID " + id + " no encontrado."));

        if (producto.getStock() > 0) {
            throw new RuntimeException("No se puede eliminar el producto porque aún tiene stock disponible.");
        }

        productoRepository.deleteById(id);
    }

    @Override
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No hay coincidencias con aquel ID."));
    }

    @Override
    public Producto actualizar(Long id, Producto productoActualizado, MultipartFile nuevaImagen, Boolean eliminarImagen) throws IOException {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto con ID " + id + " no encontrado."));

        if (Boolean.TRUE.equals(eliminarImagen)) {
            producto.setImagenUrl(null);
        } else if (nuevaImagen != null && !nuevaImagen.isEmpty()) {
            String nuevaImagenUrl = cloudinaryService.subirImagen(nuevaImagen);
            producto.setImagenUrl(nuevaImagenUrl);
        }

        if (productoActualizado == null) {
            return productoRepository.save(producto);
        }

        if (productoActualizado.getNombre() != null) {
            String nuevoNombre = productoActualizado.getNombre().trim();
            if (!StringUtils.hasText(nuevoNombre)) {
                throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
            }
            if (!nuevoNombre.equalsIgnoreCase(producto.getNombre()) && productoRepository.existsByNombre(nuevoNombre)) {
                throw new NombreDuplicadoException("El nombre '" + nuevoNombre + "' ya está en uso.");
            }
            producto.setNombre(nuevoNombre);
        }

        if (productoActualizado.getDescripcion() != null) {
            String nuevaDescripcion = productoActualizado.getDescripcion().trim();
            if (!StringUtils.hasText(nuevaDescripcion)) {
                throw new IllegalArgumentException("La descripción del producto no puede estar vacía.");
            }
            producto.setDescripcion(nuevaDescripcion);
        }

        if (productoActualizado.getPrecio() != null) {
            producto.setPrecio(productoActualizado.getPrecio());
        }
        if (productoActualizado.getStock() != null) {
            producto.setStock(productoActualizado.getStock());
        }
        if (productoActualizado.getCategoria() != null && productoActualizado.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(productoActualizado.getCategoria().getId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Categoría con ID " + productoActualizado.getCategoria().getId() + " no encontrada."));
            producto.setCategoria(categoria);
        }

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

    private void validarProducto(Producto producto) {
        if (producto == null || !StringUtils.hasText(producto.getNombre()) || !StringUtils.hasText(producto.getDescripcion())) {
            throw new IllegalArgumentException("El producto no puede estar vacío y debe contener un nombre y una descripción.");
        }

        if (productoRepository.existsByNombre(producto.getNombre())) {
            throw new NombreDuplicadoException("El nombre '" + producto.getNombre() + "' ya está en uso.");
        }

        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            throw new RecursoNoEncontradoException("Debe proporcionar un ID de categoría válido.");
        }
    }

}
