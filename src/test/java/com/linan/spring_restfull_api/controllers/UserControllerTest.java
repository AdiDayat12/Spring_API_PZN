package com.linan.spring_restfull_api.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linan.spring_restfull_api.entity.User;
import com.linan.spring_restfull_api.model.RegisterUserRequest;
import com.linan.spring_restfull_api.model.UpdateUserRequest;
import com.linan.spring_restfull_api.model.UserResponse;
import com.linan.spring_restfull_api.model.WebResponse;
import com.linan.spring_restfull_api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@SpringBootTest
@AutoConfigureMockMvc

class UserControllerTest {

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
    void testA () throws Exception {
        mockMvc.perform(
                post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"John\", \"password\":\"123\", \"name\":\"joj\"}")) // Mengirim JSON dalam request body
                .andExpect(status().isOk()) // Memeriksa apakah statusnya 201 Created
//                .andExpect(jsonPath("$.username").value("John")) // Memeriksa apakah response JSON memiliki field "name" dengan nilai "John"
//                .andExpect(jsonPath("$.name").value("joj"))
                .andDo(print()); // Mencetak hasil ke konsol

    }

    @Test
    void testRegisterSuccess () throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("test123");
        request.setPassword("rahasia");
        request.setName("test");

        mockMvc.perform(
                post("/api/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });
            assertEquals("OK", response.getData());
        });
    }

    @Test
    void testRegisterFailed () throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("");
        request.setPassword("");
        request.setName("");

        mockMvc.perform(
                post("/api/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });
            assertNotNull(response);
        });
    }

    @Test
    void getUnauthorizedUser() throws Exception {
        mockMvc.perform(
                get("/api/user/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "notfound")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});
                    assertNotNull(response);
                });
    }


    @Test
    void getUnauthorizedUserTokenNotSent() throws Exception {
        mockMvc.perform(
                get("/api/user/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "notfound")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(
                result -> {
                    WebResponse<String> response =
                            objectMapper.readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<String>>() {});
                    assertNotNull(response);
                });
    }

    @Test
    void getUserSuccess () throws Exception{
        User user = userRepository.findById("test123")
                .orElseThrow(RuntimeException::new);


        mockMvc.perform(
                get("/api/user/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", user.getToken())
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<UserResponse> response =
                            objectMapper.readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<UserResponse>>() {});
                    assertEquals("test", response.getData().getName());
                }

        );
    }


    @Test
    void updateUserFailed () throws Exception{
        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("");

        mockMvc.perform(
                patch("/api/user/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void updateUserSuccess () throws  Exception{
        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("GoatIsMe");
        mockMvc.perform(
                patch("/api/user/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<UserResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<UserResponse>>() {});
                    assertEquals("GoatIsMe", response.getData().getName());
                }
        );
    }


}