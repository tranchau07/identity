package com.example.learnspring.service;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import com.example.learnspring.Entity.Role;
import com.example.learnspring.Entity.User;
import com.example.learnspring.dto.request.UserRequest;
import com.example.learnspring.dto.response.UserResponse;
import com.example.learnspring.reponsitory.RoleRepository;
import com.example.learnspring.reponsitory.UserRepository;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    private UserRequest request;
    private User user;
    private UserResponse response;

    private LocalDate dob;

    @BeforeEach
    void init() throws Exception {

        dob = LocalDate.of(2004, 8, 28);

        request = UserRequest.builder()
                .userName("yeuAnRat")
                .password("12345678")
                .firstName("An")
                .lastName("Nguyen")
                .dob(dob)
                .build();
        response = UserResponse.builder()
                .userName("yeuAnRat")
                .firstName("An")
                .lastName("Nguyen")
                .dob(dob)
                .build();
        user = User.builder()
                .userName("yeuAnRat")
                .firstName("An")
                .lastName("Nguyen")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // GIVE
        Mockito.when(userRepository.existsByUserName(ArgumentMatchers.anyString()))
                .thenReturn(false);
        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);
        Mockito.when(roleRepository.findAllById(ArgumentMatchers.anyList())).thenReturn(List.of(new Role()));

        // WHEN
        var result = userService.createUser(request);
        // THEN
        Assertions.assertThat(result.getUserName()).isEqualTo("yeuAnRat");
    }
}
