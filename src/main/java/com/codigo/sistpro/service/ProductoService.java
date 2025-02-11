package com.codigo.sistpro.service;

import com.codigo.sistpro.entity.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    Producto guardar(Producto producto);
    List<Producto> listartodos();
    void eliminarProducto(Long id);
    Producto obtenerPorId(Long id);
    Producto actualizar(Long id, Producto productoActualizado);
}
