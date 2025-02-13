package com.codigo.sistpro.service;

import com.codigo.sistpro.entity.Producto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    Producto guardar(Producto producto, MultipartFile imagen) throws IOException;
    List<Producto> listartodos();
    void eliminarProducto(Long id);
    Producto obtenerPorId(Long id);
    List<Producto> obtenerProductosConStockBajo();
    Producto actualizar(Long id, Producto productoActualizado, MultipartFile nuevaImagen, Boolean eliminarImagen) throws IOException;
}
