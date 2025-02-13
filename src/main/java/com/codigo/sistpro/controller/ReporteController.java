package com.codigo.sistpro.controller;

import com.codigo.sistpro.entity.Producto;
import com.codigo.sistpro.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {
    private final ReporteService reporteService;

    @GetMapping("/productos-mas-vendidos")
    public ResponseEntity<List<Map<String, Object>>> productosMasVendidos() {
        return ResponseEntity.ok(reporteService.obtenerProductosMasVendidos());
    }

    @GetMapping("/usuarios-por-rol")
    public ResponseEntity<Map<String, Long>> usuariosPorRol() {
        return ResponseEntity.ok(reporteService.obtenerUsuarioPorRol());
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<List<Producto>> productosConStockBajo() {
        return ResponseEntity.ok(reporteService.obtenerProductosConStockBajo());
    }
}
