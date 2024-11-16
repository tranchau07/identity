package com.example.learnspring.dto.request;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    @Size(min = 6, message = "INVALID_USERNAME")
    String userName;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;

    String firstName;
    String lastName;
    LocalDate dob;
    Set<String> roles;
}
