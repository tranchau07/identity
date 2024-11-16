package com.example.learnspring.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.learnspring.Entity.Permission;
import com.example.learnspring.dto.request.PermissionRequest;
import com.example.learnspring.dto.response.PermissionResponse;
import com.example.learnspring.mapper.PermissionMapper;
import com.example.learnspring.reponsitory.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    public List<PermissionResponse> findAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String id) {
        permissionRepository.deleteById(id);
    }
}
