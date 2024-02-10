package com.example.jwtProject.service;


import com.example.jwtProject.model.Bill;
import com.example.jwtProject.model.User;
import com.example.jwtProject.repository.BillRepository;
import com.example.jwtProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final UserRepository userRepository;
    private final PredictedReminderDateService predictedReminderDateService;

    private final BillRepository billRepository;

    @Autowired
    public NotificationService(UserRepository userRepository, PredictedReminderDateService predictedReminderDateService, BillRepository billRepository) {
        this.userRepository = userRepository;
        this.predictedReminderDateService = predictedReminderDateService;
        this.billRepository = billRepository;
    }

    public ResponseEntity<String> runScriptForUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            try {
                LocalDateTime predictedReminderDateTime = executeNotificationScriptForUser(user);

                // Set notificationEnabled to true
                user.setNotificationEnabled(true);
                userRepository.save(user);

                // Mengambil tagihan terbaru yang sudah dibayar
                Optional<Bill> latestPaidBillOptional = billRepository.findLatestPaidBillForUser(user.getId());
                if (latestPaidBillOptional.isPresent()) {
                    Bill latestPaidBill = latestPaidBillOptional.get();
                    String billType = latestPaidBill.getBillType();

                    // Save the predicted reminder date and time to the database
                    predictedReminderDateService.updateOrSavePredictedReminderDate(user, billType, predictedReminderDateTime);

                    return ResponseEntity.ok("Script executed successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("No paid bill found for userId: " + userId);
                }
            } catch (IOException | InterruptedException e) {
                // Log the error details for debugging purposes
                System.err.println("Error executing script for user " + userId + ": " + e.getMessage());

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error executing script. Please check the logs for more details.");
            }
        } else {
            // Log the user not found error for debugging purposes
            System.err.println("User not found for userId: " + userId);

            // Return NOT_FOUND response with a descriptive message
            return ResponseEntity.notFound()
                    .header("Error", "User not found for userId: " + userId)
                    .build();
        }
    }


    private LocalDateTime executeNotificationScriptForUser(User user) throws IOException, InterruptedException {
        List<String> command = Arrays.asList("python3", "notification_script.py", user.getUsername());
        String output = executeNotificationScript(command);

        // Using the method parseDateTimeString instead of getDueDateString
        return parseDateTimeString(output.trim());
    }

    private String executeNotificationScript(List<String> command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        int exitCode = process.waitFor();

        if (exitCode == 0) {
            return output.toString();
        } else {
            throw new RuntimeException("Script execution failed with exit code " + exitCode);
        }
    }

    private LocalDateTime parseDateTimeString(String dateTimeString) {
        try {
            // Mengambil hanya baris pertama dari output
            String cleanedOutput = dateTimeString.split("\n")[0].trim();

            if (cleanedOutput.startsWith("Error in script output")) {
                throw new RuntimeException("Error in script output: " + cleanedOutput);
            }

            // Menggunakan formatter untuk mengonversi string ke LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(cleanedOutput, formatter);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing date-time string: " + e.getMessage(), e);
        }
    }

}