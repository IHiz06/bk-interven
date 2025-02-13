package com.codigo.sistpro.controller;

import com.codigo.sistpro.entity.Usuario;
import com.codigo.sistpro.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping("/registrarAdmin")
    public ResponseEntity<Usuario> registrarAdministrador(@RequestBody Usuario usuario){
        Usuario nuevoAdmin = usuarioService.crearAdministrador(usuario);
        return ResponseEntity.ok(nuevoAdmin);
    }

    @PostMapping("/registrarEmpleado")
    public ResponseEntity<Usuario> registrarEmpleado(@RequestBody Usuario usuario){
        Usuario nuevoEmpleado = usuarioService.crearEmpleado(usuario);
        return ResponseEntity.ok(nuevoEmpleado);
    }
}
