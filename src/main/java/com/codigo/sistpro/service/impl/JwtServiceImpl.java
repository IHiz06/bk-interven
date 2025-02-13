package com.codigo.sistpro.service.impl;

import com.codigo.sistpro.aggregates.constants.Constants;
import com.codigo.sistpro.entity.Usuario;
import com.codigo.sistpro.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
public class JwtServiceImpl implements JwtService {
    @Value("${key.signature}")
    private String keySignature;


    @Override
    public String generateToken(UserDetails userDetails) {
        log.info("DATO GENERATE TOKEN "+userDetails.getUsername());
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setClaims(addClaim(userDetails))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .claim("type",Constants.ACCESS)
                .claim("rol", userDetails.getAuthorities().iterator().next().getAuthority())
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        Claims claims = extraerClaims(token);
        String tokenType = claims.get("type", String.class);
        return Constants.REFRESH.equalsIgnoreCase(tokenType);
    }

    @Override
    public String extraerCorreo(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    public <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraerClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extraerClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    //METODO QUE GENERA UNA KEY PARA FIRMAR LOS TOKENS
    private Key getSignKey(){
        log.info("CLAVE CON LA QUE VAMOS A FIRMAR: " + keySignature);
        byte[] key = Decoders.BASE64.decode(keySignature);
        log.info("KEY CON LA QUE VAMOS A FIRMAR: "+ Keys.hmacShaKeyFor(key));
        return Keys.hmacShaKeyFor(key);
    }

    private Map<String, Object> addClaim(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.CLAVE_AccountNonLocked,userDetails.isAccountNonLocked());
        claims.put(Constants.CLAVE_AccountNonExpired,userDetails.isAccountNonExpired());
        claims.put(Constants.CLAVE_CredentialsNonExpired,userDetails.isCredentialsNonExpired());
        claims.put(Constants.CLAVE_Enabled,userDetails.isEnabled());
        return claims;
    }
    @Override
    public String extraerRol(String token) {
        Claims claims = extraerClaims(token);
        String rol = claims.get("rol", String.class);
        System.out.println("Rol extraído del token: " + rol);
        return claims.get("rol", String.class); // 👈 EXTRAEMOS EL ROL DEL JWT
    }


}
