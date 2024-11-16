package com.example.learnspring.controller;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.learnspring.dto.request.UserRequest;
import com.example.learnspring.dto.response.UserResponse;
import com.example.learnspring.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

// test ano
@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserRequest request;
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
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(response);
        // WHEN
        // THEN

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(0));
    }

    @Test
    void createUser_validUserName_fail() throws Exception {
        request.setUserName("mon");
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(response);
        // WHEN
        // THEN

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1005))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Invalid username"));
    }
}
