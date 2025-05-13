package com.linan.spring_restfull_api.model;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
