package com.codigo.sistpro.controller;

import com.codigo.sistpro.aggregates.request.AuthRequest;
import com.codigo.sistpro.aggregates.request.SignUpRequest;
import com.codigo.sistpro.aggregates.response.AuthResponse;
import com.codigo.sistpro.entity.Usuario;
import com.codigo.sistpro.service.AuthenticationService;
import com.codigo.sistpro.service.JwtService;
import com.codigo.sistpro.service.UsuarioService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.Base64;

@RestController
@RequestMapping("/api/auth/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signupuser")
    public ResponseEntity<Usuario> signUpUser(
            @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authenticationService
                .signUpUser(signUpRequest));
    }

    @PostMapping("/signupadmin")
    public ResponseEntity<Usuario> signUpAdmin(
            @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authenticationService
                .signUpAdmin(signUpRequest));
    }
    //OBTENER UN ACCESS TOKEN PO MEDIO DE REFRESHTOKEN


    //OBTENER UN ACCESS TOKEN POR MEDIO DE LOGIN
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(
            @RequestBody AuthRequest signInRequest) {
        return ResponseEntity.ok(authenticationService
                .singIn(signInRequest));

    }
    @GetMapping("/clave")
    public ResponseEntity<String> getClaveFirma(){
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String dato = Base64.getEncoder().encodeToString(key.getEncoded());
        return ResponseEntity.ok(dato);
    }
}
