package pl.rafalcelinski.librarymanagment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import pl.rafalcelinski.librarymanagment.dto.validation.annotation.ValidDate;
import pl.rafalcelinski.librarymanagment.dto.validation.groups.OnCreate;
import pl.rafalcelinski.librarymanagment.dto.validation.groups.OnDisplay;
import pl.rafalcelinski.librarymanagment.dto.validation.groups.OnUpdate;

public record BookDTO (
        @Null(groups = OnCreate.class)
        @NotNull(groups = {OnDisplay.class, OnUpdate.class})
        Long id,
        @NotBlank(message = "Title is required")
        String title,
        @NotBlank(message = "Author is required")
        String author,
        @NotBlank(message = "Publisher is required")
        String publisher,
        @NotBlank(message = "Release date is required")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Invalid date format, expected YYYY-MM-DD")
        @ValidDate(message = "Invalid date, expected YYYY-MM-DD")
        String releaseDate
) {}
