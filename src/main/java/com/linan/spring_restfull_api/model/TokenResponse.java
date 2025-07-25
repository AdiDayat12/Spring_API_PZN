package com.linan.spring_restfull_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TokenResponse {
    private String token;
    private Long expiredAt;
}
