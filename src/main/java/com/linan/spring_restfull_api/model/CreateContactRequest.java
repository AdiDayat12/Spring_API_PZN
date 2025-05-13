package com.linan.spring_restfull_api.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateContactRequest {
    @NotBlank
    @Size(max = 100)
    private String firstName;
    private String lastName;
    @Email
    private String email;
    private String phone;
}
