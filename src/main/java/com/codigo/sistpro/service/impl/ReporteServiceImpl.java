package com.codigo.sistpro.service.impl;

import com.codigo.sistpro.entity.Producto;
import com.codigo.sistpro.repository.ProductoRepository;
import com.codigo.sistpro.repository.UsuarioRepository;
import com.codigo.sistpro.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final JdbcTemplate jdbcTemplate;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Override
    public List<Map<String, Object>> obtenerProductosMasVendidos() {
        String sql = "SELECT p.nombre AS producto, u.nombre AS usuario, SUM(v.cantidad) AS total_vendido FROM venta v JOIN producto p ON v.producto_id = p.id JOIN usuario u ON v.usuario_id = u.id GROUP BY p.nombre, u.nombre ORDER BY total_vendido DESC";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public Map<String, Long> obtenerUsuarioPorRol() {
        return usuarioRepository.findAll().stream()
                .collect(Collectors.groupingBy(usuario -> usuario.getRol().getNombre(), Collectors.counting()));

    }

    @Override
    public List<Producto> obtenerProductosConStockBajo() {
        return productoRepository.findAll().stream()
                .filter(producto -> producto.getStock() <= producto.getStockMinimo())
                .collect(Collectors.toList());
    }
}
