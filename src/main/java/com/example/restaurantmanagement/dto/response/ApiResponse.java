package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.NotificationMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private int status;
    private String message;
    private T data;
    private String errorCode;
    private Map<String, String> errors;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true).status(200).message(NotificationMessage.SUCCESS.getMessage())
                .data(data).build();
    }

    public static <T> ApiResponse<T> success(NotificationMessage notificationMessage, T data) {
        return ApiResponse.<T>builder()
                .success(true).status(200).message(notificationMessage.getMessage())
                .errorCode(notificationMessage.getErrorCode())
                .data(data).build();
    }
    
    // Backward compatibility method
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true).status(200).message(message)
                .data(data).build();
    }

    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .success(true).status(201).message(NotificationMessage.CREATED_SUCCESS.getMessage())
                .data(data).build();
    }

    public static <T> ApiResponse<T> created(NotificationMessage notificationMessage, T data) {
        return ApiResponse.<T>builder()
                .success(true).status(201).message(notificationMessage.getMessage())
                .errorCode(notificationMessage.getErrorCode())
                .data(data).build();
    }

    public static <T> ApiResponse<T> error(int status, NotificationMessage notificationMessage) {
        return ApiResponse.<T>builder()
                .success(false).status(status).message(notificationMessage.getMessage())
                .errorCode(notificationMessage.getErrorCode()).build();
    }

    public static <T> ApiResponse<T> error(int status, String errorCode, String message) {
        return ApiResponse.<T>builder()
                .success(false).status(status).message(message)
                .errorCode(errorCode).build();
    }

    public static <T> ApiResponse<T> error(int status, NotificationMessage notificationMessage, Map<String, String> errors) {
        return ApiResponse.<T>builder()
                .success(false).status(status).message(notificationMessage.getMessage())
                .errorCode(notificationMessage.getErrorCode()).errors(errors).build();
    }
    
    // Backward compatibility method
    public static <T> ApiResponse<T> error(int status, String message) {
        return ApiResponse.<T>builder()
                .success(false).status(status).message(message).build();
    }
    
    // Backward compatibility method
    public static <T> ApiResponse<T> error(int status, String message, Map<String, String> errors) {
        return ApiResponse.<T>builder()
                .success(false).status(status).message(message).errors(errors).build();
    }
}

