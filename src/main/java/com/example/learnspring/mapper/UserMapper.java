package com.example.learnspring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.learnspring.Entity.User;
import com.example.learnspring.dto.request.UserRequest;
import com.example.learnspring.dto.request.UserUpdateRequest;
import com.example.learnspring.dto.response.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toUser(UserRequest userRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest userRequest);

    UserResponse toUserResponse(User user);
}
