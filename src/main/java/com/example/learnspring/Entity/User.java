package com.example.learnspring.Entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import com.example.learnspring.validation.ValidAge;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Size(min = 6, max = 50, message = "INVALID_USERNAME")
    String userName;

    @Size(min = 2, message = "INVALID_PASSWORD")
    String password;

    String firstName;
    String lastName;

    @ValidAge
    LocalDate dob;

    @ManyToMany
    Set<Role> roles;
}
