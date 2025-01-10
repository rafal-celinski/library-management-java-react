package pl.rafalcelinski.librarymanagment.dto;

import pl.rafalcelinski.librarymanagment.entity.User;

public record UserDTO (
    Long id,
    String username,
    String email,
    String phoneNumber,
    String firstName,
    String lastName,
    String role
) {}
