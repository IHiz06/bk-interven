package com.codigo.sistpro.aggregates.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
}
