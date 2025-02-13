package com.codigo.sistpro.service;

import com.codigo.sistpro.entity.Producto;

import java.util.List;
import java.util.Map;

public interface ReporteService {
    List<Map<String, Object>> obtenerProductosMasVendidos();
    Map<String, Long> obtenerUsuarioPorRol();
    List<Producto> obtenerProductosConStockBajo();
}
