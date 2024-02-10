package com.example.jwtProject.controller;


import com.example.jwtProject.model.JwtAuthResponse;
import com.example.jwtProject.model.LoginDto;
import com.example.jwtProject.model.RegistrationDto;
import com.example.jwtProject.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    // Build Login REST API
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto) {

        // Call the authService.login method, expecting a JwtAuthResponse
        JwtAuthResponse jwtAuthResponse = authService.login(loginDto);

        // Directly return the JwtAuthResponse object
        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationDto registrationDto) {
        try {
            authService.register(registrationDto);
            String response = "Registration successful";
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Registration failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

