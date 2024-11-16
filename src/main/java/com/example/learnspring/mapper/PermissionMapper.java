package com.example.learnspring.mapper;

import org.mapstruct.Mapper;

import com.example.learnspring.Entity.Permission;
import com.example.learnspring.dto.request.PermissionRequest;
import com.example.learnspring.dto.response.PermissionResponse;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
