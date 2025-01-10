package pl.rafalcelinski.librarymanagment.exception;

import pl.rafalcelinski.librarymanagment.dto.ValidationErrorDTO;
import java.util.List;

public class ValidationException extends RuntimeException {

    private final List<ValidationErrorDTO> validationErrors;

    public ValidationException(List<ValidationErrorDTO> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public List<ValidationErrorDTO> getValidationErrors() {
        return validationErrors;
    }
}
