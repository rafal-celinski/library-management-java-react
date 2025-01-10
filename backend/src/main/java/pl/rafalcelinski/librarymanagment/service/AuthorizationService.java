package pl.rafalcelinski.librarymanagment.service;

import org.springframework.stereotype.Service;
import pl.rafalcelinski.librarymanagment.dto.LoginDTO;
import pl.rafalcelinski.librarymanagment.dto.TokenDTO;
import pl.rafalcelinski.librarymanagment.entity.User;
import pl.rafalcelinski.librarymanagment.exception.InvalidCredentialsException;
import pl.rafalcelinski.librarymanagment.repository.UserRepository;

@Service
public class AuthorizationService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordService passwordService;

    AuthorizationService(UserRepository userRepository, TokenService tokenService, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordService = passwordService;
    }

    public TokenDTO login(LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.username())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordService.checkPassword(loginDTO.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        return tokenService.generateToken(user);
    }
}
