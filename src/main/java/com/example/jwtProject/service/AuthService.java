package com.example.jwtProject.service;

import com.example.jwtProject.model.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}
