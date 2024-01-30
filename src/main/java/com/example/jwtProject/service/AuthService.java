package com.example.jwtProject.service;

import com.example.jwtProject.model.LoginDto;
import com.example.jwtProject.model.RegistrationDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegistrationDto registrationDTO);
}
