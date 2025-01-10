package pl.rafalcelinski.librarymanagment.mapper;

import org.springframework.stereotype.Component;
import pl.rafalcelinski.librarymanagment.dto.RegisterDTO;
import pl.rafalcelinski.librarymanagment.dto.UserDTO;
import pl.rafalcelinski.librarymanagment.entity.User;

@Component
public class UserMapper {
    public User toEntity(RegisterDTO registerDTO, String hashedPassword, User.UserRole role) {
        return new User(
            registerDTO.username(),
            hashedPassword,
            registerDTO.email(),
            registerDTO.phoneNumber(),
            registerDTO.firstName(),
            registerDTO.lastName(),
            role
        );
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().toString()
        );
    }
}
