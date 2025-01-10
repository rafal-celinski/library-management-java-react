package pl.rafalcelinski.librarymanagment.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.rafalcelinski.librarymanagment.dto.RegisterDTO;
import pl.rafalcelinski.librarymanagment.dto.TokenDTO;
import pl.rafalcelinski.librarymanagment.dto.ValidationErrorDTO;
import pl.rafalcelinski.librarymanagment.entity.User;
import pl.rafalcelinski.librarymanagment.exception.ValidationException;
import pl.rafalcelinski.librarymanagment.repository.UserRepository;
import pl.rafalcelinski.librarymanagment.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordService passwordService;
    private final TokenService tokenService;

    UserService(UserRepository userRepository, UserMapper userMapper, PasswordService passwordService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordService = passwordService;
        this.tokenService = tokenService;
    }

    @Transactional
    public TokenDTO registerNewUser(RegisterDTO registerDTO) {

        List<ValidationErrorDTO> validationErrors = new ArrayList<>();

        if (userRepository.findByEmail(registerDTO.email()).isPresent()) {
            validationErrors.add(
                    new ValidationErrorDTO("email", "User with this email already exists")
            );
        }

        if (userRepository.findByUsername(registerDTO.username()).isPresent()) {
            validationErrors.add(
                    new ValidationErrorDTO("username", "User with this username already exists")
            );
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

        String hashedPassword = passwordService.hashPassword(registerDTO.password());

        User user = userMapper.toEntity(registerDTO, hashedPassword, User.UserRole.USER);
        user = userRepository.save(user);
        return tokenService.generateToken(user);
    }
}
