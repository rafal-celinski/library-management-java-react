package pl.rafalcelinski.librarymanagment.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.rafalcelinski.librarymanagment.dto.TokenDTO;
import pl.rafalcelinski.librarymanagment.entity.User;
import pl.rafalcelinski.librarymanagment.exception.InvalidTokenException;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class TokenService {
    private final SecretKey signingKey;
    private final long expirationSecs;

    public TokenService(@Value("${jwt.secretKey}") String secretKey, @Value("${jwt.expirationSecs}") long expirationSecs) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);

        this.expirationSecs = expirationSecs;
    }

    public TokenDTO generateToken(User user) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        long expirationMillis = nowMillis + this.expirationSecs * 1000;
        Date expirationDate = new Date(expirationMillis);

        Long userId = user.getId();
        User.UserRole userRole = user.getRole();

        String token = Jwts.builder()
                .subject(userId.toString())
                .claim("role", userRole.name())
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(this.signingKey)
                .compact();

        return new TokenDTO(token);
    }

    private Claims getClaimsFromToken(TokenDTO tokenDTO) {
        String token = tokenDTO.token();
        return Jwts.parser()
                .verifyWith(this.signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long extractUserId(TokenDTO tokenDTO) {
        Claims claims = getClaimsFromToken(tokenDTO);
        return Long.parseLong(claims.getSubject());
    }

    public String extractUserRole(TokenDTO tokenDTO) {
        Claims claims = getClaimsFromToken(tokenDTO);
        return User.UserRole.valueOf(claims.get("role", String.class)).name();
    }

    public void validateToken(TokenDTO tokenDTO) {
        try {
            String token = tokenDTO.token();
            Jwts.parser()
                    .verifyWith(this.signingKey)
                    .build()
                    .parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("Token expired");
        } catch (SecurityException e) {
            throw new InvalidTokenException("Invalid token signature");
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException("Invalid token format");
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid token");
        }
    }
}