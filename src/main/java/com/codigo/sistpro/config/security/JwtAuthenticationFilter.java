package com.codigo.sistpro.config.security;

import com.codigo.sistpro.service.JwtService;
import com.codigo.sistpro.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String tokenExtraidoHeader = request.getHeader("Authorization");

        if (!StringUtils.hasText(tokenExtraidoHeader) || !StringUtils.startsWithIgnoreCase(tokenExtraidoHeader, "Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String tokenLimpio = tokenExtraidoHeader.substring(7); // Extraemos el token sin "Bearer "
        String userEmail = jwtService.extraerCorreo(tokenLimpio); // Extraemos el usuario

        if (Objects.nonNull(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = usuarioService.userDetailsService().loadUserByUsername(userEmail);

            if (jwtService.validateToken(tokenLimpio, userDetails)) { // Eliminamos el refresh token check
                String rol = jwtService.extraerRol(tokenLimpio); // 👈 EXTRAEMOS EL ROL
                System.out.println("Rol obtenido del JWT: " + rol);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
