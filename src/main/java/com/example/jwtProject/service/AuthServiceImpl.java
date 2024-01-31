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
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;


@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

//    @Override
//    public String login(LoginDto loginDto) {
//
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                loginDto.getUsernameOrEmail(),
//                loginDto.getPassword()
//        ));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String token = jwtTokenProvider.generateToken(authentication);
//
//        return token;
//    }

    @Override
    public JwtAuthResponse login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // Fetch user data from the database based on username
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            String token = jwtTokenProvider.generateToken(authentication);

            // Create and return JwtAuthResponse with additional data
            JwtAuthResponse response = new JwtAuthResponse(token, "Bearer", user.getName(), user.getBalance(), user.getAccountType(), user.getAccountNumber());
            return response;
        } else {
            // Handle the case where the user is not found in the database
            throw new UsernameNotFoundException("User not found");
        }
    }


    @Override
    public String register(RegistrationDto registrationDto) {
        // Check for duplicate username or email
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            try {
                throw new BadRequestException("Username is already taken!");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            try {
                throw new BadRequestException("Email is already taken!");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }

        // Create user object and encrypt password
        User user = new User();
        user.setName(registrationDto.getName());
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setBalance(registrationDto.getBalance());
        user.setAccountType(registrationDto.getAccountType());
        user.setAccountNumber(registrationDto.getAccountNumber());
        user.setTransactionCode(passwordEncoder.encode(registrationDto.getTransactionCode()));

        // Save user to database
        userRepository.save(user);

        // Generate and return JWT token
        return jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(user.getUsername(), null, new ArrayList<>()));
    }


}

