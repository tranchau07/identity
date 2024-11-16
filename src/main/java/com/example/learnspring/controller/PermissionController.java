package com.example.learnspring.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.learnspring.dto.request.PermissionRequest;
import com.example.learnspring.dto.response.ApiResponse;
import com.example.learnspring.dto.response.PermissionResponse;
import com.example.learnspring.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.findAll())
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> delete(@PathVariable("id") String id) {
        permissionService.delete(id);
        return ApiResponse.<Void>builder().build();
    }
}
