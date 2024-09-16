package com.prueba.loyalty_program.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil{

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Llave secreta para firmar el token

    // Generar el token JWT
    public String generateToken(String userName) {
        return Jwts.builder()
                .setSubject(userName) // El subject puede ser el username
                .setIssuedAt(new Date()) // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expira en 10 horas
                .signWith(key) // Firmar el token con la llave secreta
                .compact();
    }

    // Método para validar el token si lo necesitas
    public boolean validateToken(String token, String userName) {
        String extractedUserName = extractUserName(token);
        return (extractedUserName.equals(userName) && !isTokenExpired(token));
    }

    // Extraer el nombre de usuario del token
    public String extractUserName(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Verificar si el token ha expirado
    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration().before(new Date());
    }
}
