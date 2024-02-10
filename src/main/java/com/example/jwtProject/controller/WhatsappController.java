package com.example.jwtProject.controller;


import com.example.jwtProject.service.WhatsappService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class WhatsappController {

    @Autowired
    private WhatsappService whatsappService;

    @PostMapping("/send-whatsapp/{userId}")
    public void sendWhatsappMessages(@PathVariable Long userId) {
        whatsappService.sendWhatsAppMessage(userId);
    }
}
