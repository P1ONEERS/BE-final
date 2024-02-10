//package com.example.jwtProject.service;;
//
//
//import com.example.jwtProject.repository.LoginRepository;
//import com.example.jwtProject.repository.UsersRepository;
//import com.mysql.cj.log.Log;
//import jakarta.transaction.Transactional;
//import org.apache.hc.client5.http.auth.InvalidCredentialsException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//@Service
//public class LoginService {
//
//    @Autowired
//    LoginRepository loginRepository;
//
//    @Autowired
//    UsersRepository usersRepository;
//
//    @Autowired
//    BCryptPasswordEncoder passwordEncoder;
//
//    @Autowired
//    public LoginService(LoginRepository loginRepository) {
//        this.loginRepository = loginRepository;
//    }
//
//    public List<Login> getAllLogins() {
//        return loginRepository.findAll();
//    }
//
//    public Login authenticateLogin(Long id_user, Integer mpin) throws InvalidCredentialsException {
//        if (Objects.isNull(id_user) || mpin == null) {
//            throw new InvalidCredentialsException("User ID or MPIN is empty");
//        }
//
//        Optional<Login> login = loginRepository.findUserByCredentials(id_user, String.valueOf(mpin));
//
//        if (login.isPresent()) {
//            return login.get();
//        } else {
//            throw new InvalidCredentialsException("User ID or MPIN is false");
//        }
//    }
//
//
//    public Login authenticateLoginWithHash(Long id_user, Integer mpin) throws InvalidCredentialsException {
//        if (Objects.isNull(id_user) || mpin == null) {
//            throw new InvalidCredentialsException("User ID or MPIN is empty");
//        }
//
//        Optional<Login> login = loginRepository.findUserByUserId(id_user);
//
//        if (login.isPresent()) {
//            if (passwordEncoder.matches(String.valueOf(mpin), String.valueOf(login.get().getMpin()))) {
//                return login.get();
//            } else {
//                throw new InvalidCredentialsException("User ID or MPIN is incorrect");
//            }
//        } else {
//            throw new InvalidCredentialsException("User ID or MPIN is false");
//        }
//    }
//
//
//    @Transactional
//    public void registerLogin(Long id_user, Integer mpin) {
//        if (Objects.isNull(id_user) || Objects.isNull(mpin)) {
//            throw new IllegalArgumentException("User ID or MPIN is empty");
//        }
//
//        String mpinString = String.valueOf(mpin);
//
//        if (mpinString.length() != 6) {
//            throw new IllegalArgumentException("MPIN must be 6 characters long");
//        }
//
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String hashedMpin = encoder.encode(mpinString);
//
//        // Create a new Users entity
//        Users user = new Users();
//        // Set other user attributes if needed
//        // user.setName("John Doe");
//        // user.setEmail("john@example.com");
//
//        // Save the new user to the database
//        usersRepository.save(user);
//
//        // Now, associate the Login entity with the newly created user
//        Login login = new Login();
//        login.setMpin(Integer.valueOf(hashedMpin));
//        login.setUsers(user);
//
//        // Set other Login attributes if needed
//        // login.setLogin(UUID.randomUUID());
//        // login.setAccountNumber(generateAccountNumber());
//
//        // Save the Login entity
//        loginRepository.save(login);
//    }
//
//
//
//
//
//    private boolean validatePasswordStrength(String password) {
//        return password.length() >= 8 &&
//                password.matches(".*[A-Z].*") &&
//                password.matches(".*[a-z].*") &&
//                password.matches(".*\\d.*") &&
//                password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\|,.<>/?].*");
//    }
//}
