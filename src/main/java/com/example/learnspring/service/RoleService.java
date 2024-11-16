package com.example.learnspring.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.learnspring.Entity.Role;
import com.example.learnspring.dto.request.RoleRequest;
import com.example.learnspring.dto.response.RoleResponse;
import com.example.learnspring.mapper.RoleMapper;
import com.example.learnspring.reponsitory.PermissionRepository;
import com.example.learnspring.reponsitory.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        Role role = roleMapper.toRole(request);
        var per = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(per));

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getAll() {
        var roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(String id) {
        roleRepository.deleteById(id);
    }
}
