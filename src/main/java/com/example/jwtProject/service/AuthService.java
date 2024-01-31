package com.example.jwtProject.service;

import com.example.jwtProject.model.JwtAuthResponse;
import com.example.jwtProject.model.LoginDto;
import com.example.jwtProject.model.RegistrationDto;

public interface AuthService {
    JwtAuthResponse login(LoginDto loginDto);
    String register(RegistrationDto registrationDto);
}
