package com.linan.spring_restfull_api.controllers;

import com.linan.spring_restfull_api.entity.User;
import com.linan.spring_restfull_api.model.LoginUserRequest;
import com.linan.spring_restfull_api.model.TokenResponse;
import com.linan.spring_restfull_api.model.WebResponse;
import com.linan.spring_restfull_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(
            path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> login (@RequestBody LoginUserRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        return WebResponse.<TokenResponse>builder()
                .data(tokenResponse)
                .build();
    }

    @DeleteMapping(
            path = "/api/auth/logout",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> logout (User user){
        authService.logout(user);
        return WebResponse.<String>builder()
                .data("OK")
                .build();
    }
}
