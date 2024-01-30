package com.example.jwtProject.service;

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
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

    @Override
    public String register(RegistrationDto registrationDto) {
        // Perform registration logic here
        // Ensure to encrypt the password before saving it to the database

        // Check if username or email is already registered
        checkIfUsernameOrEmailExists(registrationDto.getUsername(), registrationDto.getEmail());

        // Create a new user entity
        User user = new User();
        user.setName(registrationDto.getName());
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        // Save the user to the database
        userRepository.save(user);

        String token = login(new LoginDto(registrationDto.getUsername(), registrationDto.getPassword()));
        return token;
    }

    private void checkIfUsernameOrEmailExists(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            try {
                throw new BadRequestException("Username is already taken!");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }

        if (userRepository.existsByEmail(email)) {
            try {
                throw new BadRequestException("Email is already taken!");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }
    }


}

