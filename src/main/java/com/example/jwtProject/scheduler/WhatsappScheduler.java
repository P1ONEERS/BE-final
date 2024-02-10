package com.example.jwtProject.scheduler;

import com.example.jwtProject.service.WhatsappNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WhatsappScheduler {

    private final WhatsappNotificationService whatsappNotificationService;
    private static final Logger logger = LoggerFactory.getLogger(WhatsappScheduler.class);

    @Autowired
    public WhatsappScheduler(WhatsappNotificationService whatsappNotificationService) {
        this.whatsappNotificationService = whatsappNotificationService;
    }

    @Scheduled(cron = "0 48 13 * * *", zone = "Asia/Jakarta")
    public void checkAndSendWhatsappMessage() {
        try {
            logger.info("Scheduled task started.");
            whatsappNotificationService.sendScheduledMessages();
            logger.info("Scheduled task completed successfully.");
        } catch (Exception e) {
            logger.error("Error during scheduled task execution.", e);
        }
    }
}