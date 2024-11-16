package com.example.learnspring.dto.response;

import java.time.LocalDate;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    long id;
    String userName;
    String firstName;
    String lastName;
    LocalDate dob;
    Set<RoleResponse> roles;
}
