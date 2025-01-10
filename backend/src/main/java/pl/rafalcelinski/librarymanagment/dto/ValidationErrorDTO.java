package pl.rafalcelinski.librarymanagment.dto;

public record ValidationErrorDTO(
        String field,
        String message
) {}
