package com.example.learnspring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    SUCCESS(1000, "Success", HttpStatus.ACCEPTED),
    UNCATEGORIZED_EXCEPTION(1001, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_ALREADY_EXISTS(1002, "Username already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTS(1003, "Username not exists", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(1004, "User not found", HttpStatus.NOT_FOUND),
    INVALID_USERNAME(1005, "Invalid username", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1006, "Invalid password", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1007, "Invalid key", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1008, "You do not have permission", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(1008, "Unauthenticated", HttpStatus.FORBIDDEN),
    INVALID_DOB(1009, "you are not yet 18 years old", HttpStatus.BAD_REQUEST),
    ;

    int code;
    String message;
    HttpStatusCode statusCode;
}
