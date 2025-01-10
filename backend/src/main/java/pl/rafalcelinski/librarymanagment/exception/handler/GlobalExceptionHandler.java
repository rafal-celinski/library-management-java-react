package pl.rafalcelinski.librarymanagment.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.rafalcelinski.librarymanagment.dto.HttpErrorDTO;
import pl.rafalcelinski.librarymanagment.dto.ValidationErrorDTO;
import pl.rafalcelinski.librarymanagment.exception.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private HttpErrorDTO createHttpErrorDTO(LocalDateTime timestamp, int statusCode, String error, String path, String message, Map<String, Object> details) {
        return new HttpErrorDTO(
                timestamp.toInstant(ZoneOffset.UTC),
                path,
                statusCode,
                error,
                message,
                details
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpErrorDTO> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ValidationErrorDTO> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.add(new ValidationErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()));
        });

        return handleValidationException(new ValidationException(errors), request);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<HttpErrorDTO> handleValidationException(ValidationException e, HttpServletRequest request) {
        Map<String, Object> errors = new HashMap<>();
        e.getValidationErrors().forEach(error -> {
            errors.put(error.field(), error.message());
        });

        HttpStatus status = HttpStatus.BAD_REQUEST;
        HttpErrorDTO httpErrorDTO = createHttpErrorDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                request.getRequestURI(),
                null,
                errors
        );

        return new ResponseEntity<>(httpErrorDTO, status);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<HttpErrorDTO> handleInvalidTokenException(InvalidTokenException e, HttpServletRequest request) {
        return handleUnauthorized(e, request);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<HttpErrorDTO> handleInvalidCredentialsException(InvalidCredentialsException e, HttpServletRequest request) {
        return handleUnauthorized(e, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<HttpErrorDTO> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        return handleUnauthorized(e, request);
    }

    private ResponseEntity<HttpErrorDTO> handleUnauthorized(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        HttpErrorDTO httpErrorDTO = createHttpErrorDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                request.getRequestURI(),
                e.getMessage(),
                null
        );

        return new ResponseEntity<>(httpErrorDTO, status);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<HttpErrorDTO> handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        HttpErrorDTO httpErrorDTO = createHttpErrorDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                request.getRequestURI(),
                e.getMessage(),
                null
        );

        return new ResponseEntity<>(httpErrorDTO, status);
    }
}