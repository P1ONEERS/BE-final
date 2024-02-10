package com.example.jwtProject.service;

import com.example.jwtProject.model.LoginDto;
import com.example.jwtProject.model.RegistrationDto;
import com.example.jwtProject.model.User;
import com.example.jwtProject.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserFromAuthentication(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    public User registerUser(RegistrationDto registrationDto) {
        // Check if username and email are unique
        if (userRepository.existsByUsername(registrationDto.getUsername()) ||
                userRepository.existsByEmail(registrationDto.getEmail())) {
            // Handle duplicate username or email
            // You may throw an exception or handle it according to your application's requirements
            throw new RuntimeException("Username or email already exists");
        }

        // Create a new user entity
        User newUser = new User();
        newUser.setName(registrationDto.getName());
        newUser.setUsername(registrationDto.getUsername());
        newUser.setEmail(registrationDto.getEmail());
        newUser.setMpin(passwordEncoder.encode(registrationDto.getMpin())); // Hash the mpin
        newUser.setBalance(registrationDto.getBalance());
        newUser.setAccountType(registrationDto.getAccountType());
        newUser.setAccountNumber(registrationDto.getAccountNumber());
        newUser.setTransactionCode(passwordEncoder.encode(registrationDto.getTransactionCode())); // Hash the transactionCode

        // Save the new user
        return userRepository.save(newUser);
    }

    public User loginUser(LoginDto loginDto) {
        // Find user by username or email
        User user = userRepository.findByUsernameOrEmail(loginDto.getUsernameOrEmail(), loginDto.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the provided mpin matches the stored hashed mpin
        if (!passwordEncoder.matches(loginDto.getMpin(), user.getMpin())) {
            // Handle incorrect mpin
            // You may throw an exception or handle it according to your application's requirements
            throw new RuntimeException("Incorrect mpin");
        }

        return user;
    }

    @Transactional
    public void cancelNotificationByUserId(Long userId) {
        userRepository.cancelNotificationByUserId(userId);
    }

}
