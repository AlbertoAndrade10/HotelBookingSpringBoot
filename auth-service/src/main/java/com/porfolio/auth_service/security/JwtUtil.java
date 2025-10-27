package com.porfolio.auth_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails; // Importar UserDetails
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final Long expiration = 86400000L; // 24 horas en milisegundos

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // ✅ Método para obtener los roles del token
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        // Asumiendo que los roles están en un claim llamado "roles"
        // Spring Security los almacena como "authorities", pero puedes mapearlos al claim "roles"
        // Si guardas el rol como "ROLE_USER", puedes extraerlo así:
        Object authorities = getClaimFromToken(token, t -> t.get("authorities"));
        if (authorities instanceof List) {
            return (List<String>) authorities;
        }
        return List.of(); // Devuelve una lista vacía si no hay roles
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) { // Cambiado a UserDetails
        Map<String, Object> claims = new HashMap<>();
        // ✅ Añadir el rol (o roles) al token como claim "authorities"
        claims.put("authorities", userDetails.getAuthorities().stream()
                                               .map(auth -> auth.getAuthority())
                                               .toList()); // Mapea las authorities a strings
        return createToken(claims, userDetails.getUsername()); // Usa el username de UserDetails
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
