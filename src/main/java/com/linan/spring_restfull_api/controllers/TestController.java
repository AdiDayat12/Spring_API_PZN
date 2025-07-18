package com.linan.spring_restfull_api.controllers;

import io.sentry.Sentry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    @GetMapping("/test-error")
    public void testError() {
        try {
            throw new Exception("This is a test.");
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new RuntimeException(e); // lempar ulang agar tercatat sebagai error
        }
    }


}

