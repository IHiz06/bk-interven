package com.codigo.sistpro.service;

import com.codigo.sistpro.entity.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UsuarioService {
    UserDetailsService userDetailsService();
    Usuario crearAdministrador(Usuario usuario);
    Usuario crearEmpleado(Usuario usuario);
    Usuario buscarPorEmail(String email);

}
