package com.example.jwtProject.controller;

import com.example.jwtProject.model.Bill;
import com.example.jwtProject.model.PredictedReminderDate;
import com.example.jwtProject.model.User;
import com.example.jwtProject.repository.BillRepository;
import com.example.jwtProject.service.PredictedReminderDateService;
import com.example.jwtProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/predicted-reminder-date")
public class PredictedReminderDateController {

    private final PredictedReminderDateService predictedReminderDateService;
    private final UserService userService;

    private final BillRepository billRepository;

    @Autowired
    public PredictedReminderDateController(PredictedReminderDateService predictedReminderDateService, UserService userService, BillRepository billRepository) {
        this.predictedReminderDateService = predictedReminderDateService;
        this.userService = userService;
        this.billRepository = billRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PredictedReminderDate>> getAllPredictedReminderDates(Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        List<PredictedReminderDate> predictedReminderDates = predictedReminderDateService.findAllByUser(user);
        return new ResponseEntity<>(predictedReminderDates, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updatePredictedReminderDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime predictedReminderDate,
            Authentication authentication) {
        try {
            User user = userService.getUserFromAuthentication(authentication);

            // Mengambil tagihan terbaru yang sudah dibayar
            Optional<Bill> latestPaidBillOptional = billRepository.findLatestPaidBillForUser(user.getId());
            if (latestPaidBillOptional.isPresent()) {
                Bill latestPaidBill = latestPaidBillOptional.get();
                String billType = latestPaidBill.getBillType();

                // Update atau simpan tanggal pengingat yang diprediksi
                predictedReminderDateService.updateOrSavePredictedReminderDate(user, billType, predictedReminderDate);

                return new ResponseEntity<>("Predicted reminder date updated successfully", HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("No paid bill found for the user");
            }
        } catch (Exception e) {
            // Log the error details for debugging purposes
            System.err.println("Error updating predicted reminder date: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating predicted reminder date. Please check the logs for more details.");
        }
    }

    // Pendekatan sederhana tanpa class baru
    @PostMapping("/update-simple")
    public ResponseEntity<String> updatePredictedReminderDateSimple(
            @RequestBody Map<String, Object> requestMap,
            Authentication authentication) {
        try {
            LocalDateTime predictedReminderDate = LocalDateTime.parse((String) requestMap.get("predictedReminderDate"));

            User user = userService.getUserFromAuthentication(authentication);

            // Mengambil tagihan terbaru yang sudah dibayar
            Optional<Bill> latestPaidBillOptional = billRepository.findLatestPaidBillForUser(user.getId());
            if (latestPaidBillOptional.isPresent()) {
                Bill latestPaidBill = latestPaidBillOptional.get();
                String billType = latestPaidBill.getBillType();

                // Update atau simpan tanggal pengingat yang diprediksi
                predictedReminderDateService.updateOrSavePredictedReminderDate(user, billType, predictedReminderDate);

                return new ResponseEntity<>("Predicted reminder date updated successfully (simple approach)", HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("No paid bill found for the user");
            }
        } catch (Exception e) {
            // Log the error details for debugging purposes
            System.err.println("Error updating predicted reminder date (simple approach): " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating predicted reminder date (simple approach). Please check the logs for more details.");
        }
    }
}
