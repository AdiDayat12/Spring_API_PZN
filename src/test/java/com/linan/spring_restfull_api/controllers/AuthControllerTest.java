package com.linan.spring_restfull_api.controllers;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linan.spring_restfull_api.entity.User;
import com.linan.spring_restfull_api.model.LoginUserRequest;
import com.linan.spring_restfull_api.model.TokenResponse;
import com.linan.spring_restfull_api.model.WebResponse;
import com.linan.spring_restfull_api.repository.UserRepository;
import com.linan.spring_restfull_api.security.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

   @Autowired
   private ObjectMapper objectMapper;


//    @BeforeEach
//    void setUp() {
//        userRepository.deleteAll();
//    }

    @Test
    void loginFailedUserNotFound() throws Exception {
        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("hello");
        request.setPassword("test");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void loginSuccess () throws Exception {
        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("test123");
        request.setPassword("rahasia");
        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<TokenResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<TokenResponse>>() {});
                    assertNotNull(response.getData());
                }
        );
    }

    @Test
    void logoutFailed () throws Exception{
        mockMvc.perform(
                delete("/api/auth/logout")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<String>>() {});
                    assertNotNull(response.getErrors());
                }
        );
    }

    @Test
    void logoutSuccess () throws Exception{
        User user = new User();
        user.setUsername("a123");
        user.setName("adi");
        user.setPassword(BCrypt.hashpw("a123", BCrypt.gensalt()));
        user.setToken("1234");
        user.setTokenExpiredAt(System.currentTimeMillis() + 500000L);
        userRepository.save(user);

        mockMvc.perform(
                delete("/api/auth/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "1234")
        ).andExpectAll(
                status().isOk()
        ).andExpectAll(
                result -> {
                    WebResponse<String> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<String>>() {
                                    });
                    assertNull(response.getErrors());
                    assertEquals("OK", response.getData());
                }
        );
    }
}