package com.linan.spring_restfull_api.controllers;

import com.linan.spring_restfull_api.model.RegisterUserRequest;
import com.linan.spring_restfull_api.model.WebResponse;
import com.linan.spring_restfull_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/users")
    public WebResponse<String> register (@RequestBody RegisterUserRequest request){
        userService.register(request);
        return WebResponse.<String>builder().data("OK").build();
    }
}
