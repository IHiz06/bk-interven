package com.codigo.sistpro.service;

import com.codigo.sistpro.aggregates.request.AuthRequest;
import com.codigo.sistpro.aggregates.request.SignUpRequest;
import com.codigo.sistpro.aggregates.response.AuthResponse;
import com.codigo.sistpro.entity.Usuario;

public interface AuthenticationService {
    Usuario signUpUser(SignUpRequest signUpRequest);
    Usuario signUpAdmin(SignUpRequest signUpRequest);

    AuthResponse singIn(AuthRequest authRequest);
}
