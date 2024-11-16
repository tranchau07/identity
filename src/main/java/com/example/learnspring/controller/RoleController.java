package com.example.learnspring.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.learnspring.dto.request.RoleRequest;
import com.example.learnspring.dto.response.ApiResponse;
import com.example.learnspring.dto.response.RoleResponse;
import com.example.learnspring.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> delete(@PathVariable("id") String id) {
        roleService.delete(id);
        return ApiResponse.<Void>builder().build();
    }
}
