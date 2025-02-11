package com.codigo.sistpro.controller;

import com.codigo.sistpro.entity.Producto;
import com.codigo.sistpro.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    @GetMapping
    public List<Producto> obtenerProductos(){
        return productoService.listartodos();
    }

    @PostMapping("/crear")
    public Producto crearProducto(@RequestBody Producto producto){
        return  productoService.guardar(producto);
    }


    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id){
        productoService.eliminarProducto(id);
    }

    @GetMapping("/obtenerid")
    public Producto obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id);
    }

    @PutMapping("/actualizar/{id}")
    public Producto actualizarProducto(@PathVariable Long id, @RequestBody Producto producto){
        return productoService.actualizar(id, producto);
    }
}
