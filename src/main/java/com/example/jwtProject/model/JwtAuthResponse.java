package com.example.jwtProject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {
    private String id;
    private String accessToken;
    private String tokenType = "Bearer";
    private String name;
    private Double balance;
    private String accountType;
    private String accountNumber;
    private boolean notificationEnabled;
}
