package com.example.learnspring.exception;

import java.util.Objects;

import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.learnspring.dto.response.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> runtimeExceptionHandler(AppException appException) {
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode errorCode = appException.getErrorCode();
        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setCode(errorCode.getCode());
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        e.printStackTrace();
        String errKey = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();

        ErrorCode errCode = ErrorCode.INVALID_KEY;
        try {
            errCode = ErrorCode.valueOf(errKey);
        } catch (IllegalArgumentException ex) {

        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(errCode.getMessage());
        apiResponse.setCode(errCode.getCode());

        return ResponseEntity.status(errCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().iterator().next().getMessage();

        ErrorCode errCode = ErrorCode.valueOf(message);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(errCode.getMessage());
        apiResponse.setCode(errCode.getCode()); // Hoặc một mã lỗi thích hợp

        return ResponseEntity.status(errCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> accessDeniedExceptionHandler(AccessDeniedException accessDeniedException) {

        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .message(errorCode.getMessage())
                        .code(errorCode.getCode())
                        .build());
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> exceptionHandler(Exception exception) {
        exception.printStackTrace();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatusCode())
                .body(apiResponse);
    }
}
