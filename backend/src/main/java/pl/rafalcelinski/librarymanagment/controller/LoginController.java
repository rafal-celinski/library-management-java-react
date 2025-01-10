package pl.rafalcelinski.librarymanagment.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.rafalcelinski.librarymanagment.dto.AuthResponseDTO;
import pl.rafalcelinski.librarymanagment.dto.LoginDTO;
import pl.rafalcelinski.librarymanagment.dto.RegisterDTO;
import pl.rafalcelinski.librarymanagment.dto.TokenDTO;
import pl.rafalcelinski.librarymanagment.entity.User;
import pl.rafalcelinski.librarymanagment.service.AuthorizationService;
import pl.rafalcelinski.librarymanagment.service.TokenService;
import pl.rafalcelinski.librarymanagment.service.UserService;


@RestController
@RequestMapping("")
public class LoginController {

    private final UserService userService;
    private final AuthorizationService authorizationService;
    private final TokenService tokenService;

    public LoginController(UserService userService, AuthorizationService authorizationService, TokenService tokenService) {
        this.userService = userService;
        this.authorizationService = authorizationService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO, HttpServletResponse response) {
        TokenDTO tokenDTO = userService.registerNewUser(registerDTO);
        String userRole = tokenService.extractUserRole(tokenDTO);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(userRole);

        Cookie cookie = new Cookie("authToken", tokenDTO.token());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "Strict");
        cookie.setPath("/");
        cookie.setMaxAge(-1); // session in the browser

        response.addCookie(cookie);

        return ResponseEntity.ok(authResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        TokenDTO tokenDTO = authorizationService.login(loginDTO);
        String userRole = tokenService.extractUserRole(tokenDTO);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(userRole);

        Cookie cookie = new Cookie("authToken", tokenDTO.token());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "Strict");
        cookie.setPath("/");
        cookie.setMaxAge(-1); // session in the browser

        response.addCookie(cookie);

        return ResponseEntity.ok(authResponseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout( HttpServletResponse response) {
        Cookie cookie = new Cookie("authToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "Strict");
        cookie.setPath("/");
        cookie.setMaxAge(0); // immediately deleted

        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }


}