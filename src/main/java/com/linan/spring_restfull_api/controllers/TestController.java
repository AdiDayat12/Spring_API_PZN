package com.linan.spring_restfull_api.controllers;

import io.sentry.Sentry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author adilinan
 */
@RestController
public class TestController {

    @GetMapping("/test-error")
    public String testError() {
        try {
            throw new Exception("This is a test.");
        } catch (Exception e) {
            Sentry.captureException(e);
        }
        return "Error sent to Sentry";
    }
}

