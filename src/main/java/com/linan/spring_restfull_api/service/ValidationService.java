package com.linan.spring_restfull_api.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ValidationService {
    @Autowired
    private Validator validator;

    public void validate (Object o) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(o);

        if (!constraintViolations.isEmpty()){
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}

