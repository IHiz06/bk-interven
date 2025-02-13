package com.codigo.sistpro.aggregates.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String correo;
    private String contrasena;
}
