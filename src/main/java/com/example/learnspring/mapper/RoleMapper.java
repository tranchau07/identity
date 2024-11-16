package com.example.learnspring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.learnspring.Entity.Role;
import com.example.learnspring.dto.request.RoleRequest;
import com.example.learnspring.dto.response.RoleResponse;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest roleRequest);

    RoleResponse toRoleResponse(Role role);
}
