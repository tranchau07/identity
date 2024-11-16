package com.example.learnspring.Entity;

import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
    String name;

    @Size
    String description;

    @ManyToMany
    Set<Permission> permissions;
}
