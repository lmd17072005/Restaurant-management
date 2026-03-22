package com.example.restaurantmanagement.exception;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {
    private String errorCode = "DUPLICATE_RESOURCE";

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}