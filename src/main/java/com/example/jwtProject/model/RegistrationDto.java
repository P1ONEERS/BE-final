package com.example.jwtProject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {
    private String name;
    private String username;
    private String email;
    private String mpin;
    private Double balance;
    private String accountType;
    private String accountNumber;
    private String transactionCode;
    private String noTelp;
}

