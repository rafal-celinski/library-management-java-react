package pl.rafalcelinski.librarymanagment.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;
import pl.rafalcelinski.librarymanagment.dto.HttpErrorDTO;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final View error;

    public CustomAuthenticationEntryPoint(View error) {
        this.error = error;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        HttpErrorDTO httpErrorDTO = new HttpErrorDTO(
                LocalDateTime.now().toInstant(ZoneOffset.UTC),
                request.getRequestURI(),
                status.value(),
                status.getReasonPhrase(),
                authException.getMessage(),
                null
        );

        response.setStatus(status.value());
        response.setContentType("application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonResponse = objectMapper.writeValueAsString(httpErrorDTO);
        response.getWriter().write(jsonResponse);

    }
}