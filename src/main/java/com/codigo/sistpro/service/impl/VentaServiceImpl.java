package com.codigo.sistpro.service.impl;

import com.codigo.sistpro.entity.Producto;
import com.codigo.sistpro.entity.Usuario;
import com.codigo.sistpro.entity.Venta;
import com.codigo.sistpro.repository.ProductoRepository;
import com.codigo.sistpro.repository.UsuarioRepository;
import com.codigo.sistpro.repository.VentaRepository;
import com.codigo.sistpro.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {
    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public void registrarVenta(Venta venta) {
        Producto producto = productoRepository.findById(venta.getProducto().getId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (producto.getStock() < venta.getCantidad()) {
            throw new IllegalArgumentException("Stock insuficiente");
        }

        Usuario usuario = usuarioRepository.findById(venta.getUsuario().getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        producto.setStock(producto.getStock() - venta.getCantidad());
        productoRepository.save(producto);

        venta.setFecha(LocalDateTime.now());
        venta.setUsuario(usuario);
        ventaRepository.save(venta);
    }
}
