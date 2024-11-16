package com.example.learnspring.service;

import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.learnspring.Entity.User;
import com.example.learnspring.dto.request.UserRequest;
import com.example.learnspring.dto.request.UserUpdateRequest;
import com.example.learnspring.dto.response.UserResponse;
import com.example.learnspring.exception.AppException;
import com.example.learnspring.exception.ErrorCode;
import com.example.learnspring.mapper.UserMapper;
import com.example.learnspring.reponsitory.RoleRepository;
import com.example.learnspring.reponsitory.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    RoleRepository roleRepository;

    public UserResponse createUser(UserRequest request) {
        log.info("Create user");

        if (userRepository.existsByUserName(request.getUserName())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PostAuthorize("returnObject.userName == authentication.name")
    public UserResponse getUserById(long id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public UserResponse getMyInfo() {
        var Auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository
                .findByUserName(Auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(long id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
