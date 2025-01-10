package pl.rafalcelinski.librarymanagment.security;

import io.jsonwebtoken.Jwt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.rafalcelinski.librarymanagment.dto.TokenDTO;
import pl.rafalcelinski.librarymanagment.entity.User;
import pl.rafalcelinski.librarymanagment.service.TokenService;
import pl.rafalcelinski.librarymanagment.service.UserService;

import java.io.IOException;
import java.util.List;


@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserService userService;

    public TokenAuthenticationFilter(TokenService tokenService, UserService userService) {
        super();
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            Cookie[] cookies = request.getCookies();
            TokenDTO tokenDTO = null;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("authToken".equals(cookie.getName())) {
                        tokenDTO = new TokenDTO(cookie.getValue());
                        break;
                    }
                }
            }

            tokenService.validateToken(tokenDTO);

            String token = tokenDTO.token();
            Long userId = tokenService.extractUserId(tokenDTO);

            String userRole = tokenService.extractUserRole(tokenDTO);
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRole);

            JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(token, userId, List.of(authority));
            jwtAuthenticationToken.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
        }
        catch (Exception _) {}

        filterChain.doFilter(request, response);
    }
}
