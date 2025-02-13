package com.codigo.sistpro.service.impl;

import com.codigo.sistpro.aggregates.constants.Constants;
import com.codigo.sistpro.aggregates.request.AuthRequest;
import com.codigo.sistpro.aggregates.request.SignUpRequest;
import com.codigo.sistpro.aggregates.response.AuthResponse;
import com.codigo.sistpro.entity.Rol;
import com.codigo.sistpro.entity.RolNombre;
import com.codigo.sistpro.entity.Usuario;
import com.codigo.sistpro.repository.RolRepository;
import com.codigo.sistpro.repository.UsuarioRepository;
import com.codigo.sistpro.service.AuthenticationService;
import com.codigo.sistpro.service.JwtService;
import com.codigo.sistpro.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public Usuario signUpUser(SignUpRequest signUpRequest) {

        Usuario usuario = getUsuarioEntity(signUpRequest);
        usuario.setRol(getRoles(RolNombre.EMPLEADO));
        return usuarioRepository.save(usuario);
    }

    private Rol getRoles(RolNombre rolBuscado){
        return rolRepository.findByNombre(rolBuscado.name())
                .orElseThrow(
                        () -> new RuntimeException(
                                "ERROR EL ROL NO EXISTE :" + rolBuscado.name()));
    }

    private Usuario getUsuarioEntity(SignUpRequest signUpRequest){
        return Usuario.builder()
                .nombre(signUpRequest.getNombre())
                .apellido(signUpRequest.getApellido())
                .correo(signUpRequest.getCorreo())
                .contrasena(signUpRequest.getContrasena())
                .contrasena(new BCryptPasswordEncoder().encode(signUpRequest.getContrasena()))
                .activo(Constants.STATUS_ACTIVE)
                .creadoEn(LocalDateTime.now())
                .actualizadoEn(LocalDateTime.now())
                .isAccountNonExpired(Constants.STATUS_ACTIVE)
                .isAccountNonLocked(Constants.STATUS_ACTIVE)
                .isCredentialsNonExpired(Constants.STATUS_ACTIVE)
                .isEnabled(Constants.STATUS_ACTIVE)
                .build();
    }

    @Override
    public Usuario signUpAdmin(SignUpRequest signUpRequest) {
        Usuario usuario = getUsuarioEntity(signUpRequest);
        usuario.setRol(getRoles(RolNombre.ADMINISTRADOR));
        return usuarioRepository.save(usuario);
    }

    @Override
    public AuthResponse singIn(AuthRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getCorreo(),authRequest.getContrasena()
        ));
        var user = usuarioRepository.findByCorreo(authRequest.getCorreo()).orElseThrow(
                () -> new UsernameNotFoundException("Error Usuario no encontrado en base de datos"));
        var token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }
}
