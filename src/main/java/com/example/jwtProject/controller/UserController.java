package com.example.jwtProject.controller;

import com.example.jwtProject.model.User;
import com.example.jwtProject.repository.BillRepository;
import com.example.jwtProject.repository.UserRepository;
import com.example.jwtProject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PredictedReminderDateService predictedReminderDateService;
    private final BillRepository billRepository;
    private final WhatsappNotificationService whatsappNotificationService;
    private final NotificationService notificationService;
    private final NotificationAfterBillService notificationAfterBillService;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, PredictedReminderDateService predictedReminderDateService, BillRepository billRepository, WhatsappNotificationService whatsappNotificationService, NotificationService notificationService, NotificationAfterBillService notificationAfterBillService, UserService userService) {
        this.userRepository = userRepository;
        this.predictedReminderDateService = predictedReminderDateService;
        this.billRepository = billRepository;
        this.whatsappNotificationService = whatsappNotificationService;
        this.notificationService = notificationService;
        this.notificationAfterBillService = notificationAfterBillService;
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/{userId}/run-script")
    public ResponseEntity<String> runScriptForUser(@PathVariable Long userId, @RequestHeader("Authorization") String authorizationHeader) {
        ResponseEntity<String> response = notificationService.runScriptForUser(userId, authorizationHeader);
        if (response.getBody().equals("Notification disabled. No script executed.")) {
            return ResponseEntity.ok().body(response.getBody());
        } else {
            return response;
        }
    }

    @PostMapping("/{userId}/run-script-after-bill")
    public ResponseEntity<String> runScriptAfterBill(@PathVariable Long userId, @RequestHeader("Authorization") String authorizationHeader) {

        ResponseEntity<String> response = notificationAfterBillService.runScriptAfterBill(userId, authorizationHeader);
        if (response.getBody().equals("Notification disabled. No script executed.")) {
            return ResponseEntity.ok().body(response.getBody());
        } else {
            return response;
        }
    }


    @PostMapping("/{userId}/cancel-notification") // samain pathvariable nya menjadi username
    public ResponseEntity<String> cancelNotification(@PathVariable Long userId) {
        try {

            // Menghapus data terkait dari tabel whatsapp
            whatsappNotificationService.deleteDataByUserId(userId);

            // Menghapus data terkait dari tabel predicted_reminder_date
            predictedReminderDateService.deleteDataByUserId(userId);

            // Membatalkan notifikasi melalui userService
            userService.cancelNotificationByUserId(userId);

            return ResponseEntity.ok("Notification feature canceled successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error canceling notification: " + e.getMessage());
        }
    }

}

