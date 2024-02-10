package com.example.jwtProject.service;

import com.example.jwtProject.model.JwtAuthResponse;
import com.example.jwtProject.model.LoginDto;
import com.example.jwtProject.model.RegistrationDto;
import com.example.jwtProject.model.User;
import com.example.jwtProject.repository.UserRepository;
import com.example.jwtProject.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public JwtAuthResponse login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getMpin()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = jwtTokenProvider.generateToken(authentication);

            JwtAuthResponse response = new JwtAuthResponse(
                    user.getId().toString(),
                    token,
                    "Bearer",
                    user.getName(),
                    user.getBalance(),
                    user.getAccountType(),
                    user.getAccountNumber(),
                    user.isNotificationEnabled());
            return response;
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public String register(RegistrationDto registrationDto) {
        try {
            if (userRepository.existsByUsername(registrationDto.getUsername())) {
                throw new BadRequestException("Username is already taken!");
            }

            if (userRepository.existsByEmail(registrationDto.getEmail())) {
                throw new BadRequestException("Email is already taken!");
            }

            User user = new User();
            user.setName(registrationDto.getName());
            user.setUsername(registrationDto.getUsername());
            user.setEmail(registrationDto.getEmail());
            user.setMpin(passwordEncoder.encode(registrationDto.getMpin()));
            user.setBalance(registrationDto.getBalance());
            user.setAccountType(registrationDto.getAccountType());
            user.setAccountNumber(registrationDto.getAccountNumber());
            user.setTransactionCode(passwordEncoder.encode(registrationDto.getTransactionCode()));


            userRepository.save(user);

            return jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(user.getUsername(), null, new ArrayList<>()));
        } catch (BadRequestException e) {
            return "Registration failed: " + e.getMessage();
        }
    }
}

