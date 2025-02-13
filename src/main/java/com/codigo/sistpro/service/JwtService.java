package com.codigo.sistpro.service;

import com.codigo.sistpro.entity.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

public interface JwtService {
    String extraerCorreo(String token);
    String generateToken(UserDetails userDetails);
    boolean validateToken(String token, UserDetails userDetails);
    String extraerRol(String token);
}
