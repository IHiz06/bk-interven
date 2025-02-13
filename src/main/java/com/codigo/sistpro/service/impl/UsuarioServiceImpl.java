package com.codigo.sistpro.service.impl;

import com.codigo.sistpro.aggregates.constants.Constants;
import com.codigo.sistpro.entity.Rol;
import com.codigo.sistpro.entity.RolNombre;
import com.codigo.sistpro.entity.Usuario;
import com.codigo.sistpro.repository.RolRepository;
import com.codigo.sistpro.repository.UsuarioRepository;
import com.codigo.sistpro.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;


    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                return usuarioRepository.findByCorreo(email)
                        .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));
            }
        };
    }

    @Override
    public Usuario crearAdministrador(Usuario usuario) {
        if (usuario.getContrasena() == null || usuario.getContrasena().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacia o nula");
        }

        if (usuario.getRol() == null) { // Si el rol es nulo, asignar EMPLEADO por defecto
            usuario.setRol(getRoles(RolNombre.ADMINISTRADOR));
        }
        System.out.println("STATUS_ACTIVE: " + Constants.STATUS_ACTIVE);

        usuario.setContrasena(new BCryptPasswordEncoder().encode(usuario.getPassword()));
        usuario.setActivo(Constants.STATUS_ACTIVE);
        usuario.setCreadoEn(LocalDateTime.now());
        usuario.setActualizadoEn(LocalDateTime.now());
        usuario.setIsAccountNonExpired(Constants.STATUS_ACTIVE);
        usuario.setIsAccountNonLocked(Constants.STATUS_ACTIVE);
        usuario.setIsCredentialsNonExpired(Constants.STATUS_ACTIVE);
        usuario.setIsEnabled(Constants.STATUS_ACTIVE);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario crearEmpleado(Usuario usuario) {
        if (usuario.getContrasena() == null || usuario.getContrasena().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacia o nula");
        }

        if (usuario.getRol() == null) { // Si el rol es nulo, asignar EMPLEADO por defecto
            usuario.setRol(getRoles(RolNombre.EMPLEADO));
        }

        usuario.setContrasena(new BCryptPasswordEncoder().encode(usuario.getPassword()));
        usuario.setActivo(Constants.STATUS_ACTIVE);
        usuario.setCreadoEn(LocalDateTime.now());
        usuario.setActualizadoEn(LocalDateTime.now());
        usuario.setIsAccountNonExpired(Constants.STATUS_ACTIVE);
        usuario.setIsAccountNonLocked(Constants.STATUS_ACTIVE);
        usuario.setIsCredentialsNonExpired(Constants.STATUS_ACTIVE);
        usuario.setIsEnabled(Constants.STATUS_ACTIVE);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    private Rol getRoles(RolNombre rolBuscado){
        return rolRepository.findByNombre(rolBuscado.name())
                .orElseThrow(
                        () -> new RuntimeException(
                                "ERROR EL ROL NO EXISTE :" + rolBuscado.name()));
    }


}
