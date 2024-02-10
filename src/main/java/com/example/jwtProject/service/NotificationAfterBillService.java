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
public class NotificationAfterBillService {

    private final UserRepository userRepository;
    private final PredictedReminderDateService predictedReminderDateService;
    private final WhatsappService whatsappService;
    private final BillRepository billRepository;

    @Autowired
    public NotificationAfterBillService(UserRepository userRepository, PredictedReminderDateService predictedReminderDateService, WhatsappService whatsappService, BillRepository billRepository) {
        this.userRepository = userRepository;
        this.predictedReminderDateService = predictedReminderDateService;
        this.whatsappService = whatsappService;
        this.billRepository = billRepository;
    }


    // NotificationAfterBillService
// NotificationAfterBillService
    public ResponseEntity<String> runScriptAfterBill(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (!user.isNotificationEnabled()) {
                return ResponseEntity.ok("Notification is not enabled for this user.");
            }

            try {
                // Mengambil tagihan terbaru yang sudah dibayar
                Optional<Bill> latestPaidBillOptional = billRepository.findLatestPaidBillForUser(user.getId());

                if (latestPaidBillOptional.isPresent()) {
                    Bill latestPaidBill = latestPaidBillOptional.get();
                    String output = executeNotificationScriptForUser(user, latestPaidBill.getDueDateString());

                    System.out.println("Script Output: " + output);
                    LocalDateTime predictedReminderDateTime = parseDateTimeString(output.trim());

                    predictedReminderDateService.updateOrSavePredictedReminderDate(user, latestPaidBill.getBillType(), predictedReminderDateTime);

                    return ResponseEntity.ok("Script executed successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("No paid bill found for userId: " + userId);
                }
            } catch (IOException | InterruptedException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error executing script: " + e.getMessage());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private String executeNotificationScriptForUser(User user, String dueDateString) throws IOException, InterruptedException {
        List<String> command = Arrays.asList("python3", "notification_script.py", user.getUsername(), dueDateString);
        return executeNotificationScript(command);
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

        // Log output and exit code
        System.out.println("Script Output: " + output.toString());
        System.out.println("Exit Code: " + exitCode);

        if (exitCode == 0) {
            return output.toString();
        } else {
            throw new IOException("Script execution failed with exit code " + exitCode);
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
