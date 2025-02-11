package com.codigo.sistpro.controller;

import com.codigo.sistpro.entity.Producto;
import com.codigo.sistpro.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public Producto crearProducto(
            @RequestPart("producto") String productoJson,  // Recibir como String primero
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws IOException {

        // Convertir el JSON en un objeto Producto usando Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        Producto producto = objectMapper.readValue(productoJson, Producto.class);

        return productoService.guardar(producto, imagen);
    }


    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id){
        productoService.eliminarProducto(id);
    }

    @GetMapping("/obtenerid")
    public Producto obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id);
    }

    @PutMapping(value = "/actualizar/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Producto actualizarProducto(
            @PathVariable Long id,
            @RequestPart(value = "producto", required = false) String productoJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            @RequestParam(value = "eliminarImagen", required = false) Boolean eliminarImagen
    ) throws IOException {
        Producto producto = null;
        if (productoJson != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            producto = objectMapper.readValue(productoJson, Producto.class);
        }

        return productoService.actualizar(id, producto, imagen, eliminarImagen);
    }





}
