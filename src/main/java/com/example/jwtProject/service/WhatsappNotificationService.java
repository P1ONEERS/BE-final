package com.example.jwtProject.service;

import com.example.jwtProject.model.PredictedReminderDate;
import com.example.jwtProject.model.Whatsapp;
import com.example.jwtProject.repository.WhatsappRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class WhatsappNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(WhatsappNotificationService.class);

    private final WhatsappRepository whatsappRepository;

    @Value("${whatsapp.api.url}")
    private String whatsappApiUrl;

    @Value("${whatsapp.api.token}")
    private String whatsappApiToken;

    public WhatsappNotificationService(WhatsappRepository whatsappRepository) {
        this.whatsappRepository = whatsappRepository;
    }

    public void sendScheduledMessages() {
        logger.info("Scheduled job started at: {}", LocalDateTime.now());

        try {
            LocalDate currentDate = LocalDate.now();

            List<PredictedReminderDate> duePredictedReminderDates = whatsappRepository.findPredictedReminderDatesToProcess(currentDate.atTime(LocalTime.MAX));
            logger.info("Executing query findPredictedReminderDatesToProcess with currentDate: {}", currentDate);
            logger.info("Query result size: {}", duePredictedReminderDates.size());

            for (PredictedReminderDate predictedReminderDate : duePredictedReminderDates) {
                logger.info("Processing PredictedReminderDate: {}", predictedReminderDate.getId());
                processPredictedReminderDate(predictedReminderDate);
            }

            logger.info("Scheduled job completed at: {}", LocalDateTime.now());
        } catch (Exception e) {
            logger.error("Error in scheduled job: {}", e.getMessage(), e);
        }
    }

    private void processPredictedReminderDate(PredictedReminderDate predictedReminderDate) {
        try {
            // Lakukan logika pengiriman pesan WhatsApp di sini
            sendWhatsAppMessage(predictedReminderDate);

            // Set notifikasi sebagai sudah dikirim dan catat log
            saveWhatsAppLog(predictedReminderDate);
        } catch (Exception e) {
            logger.error("Error processing and sending WhatsApp message: {}", e.getMessage(), e);
        }
    }

    private void saveWhatsAppLog(PredictedReminderDate predictedReminderDate) {
        try {
            // Simpan data ke dalam tabel whatsapp sebagai log
            Whatsapp whatsappLog = new Whatsapp();
            whatsappLog.setPredictedReminderDate(predictedReminderDate);
            whatsappLog.setSent(true);  // Atur status terkirim menjadi true
            whatsappLog.setMessage(createWhatsAppMessage(predictedReminderDate));
            whatsappRepository.save(whatsappLog);

            logger.info("WhatsApp log saved for PredictedReminderDate ID: {}", predictedReminderDate.getId());
        } catch (Exception e) {
            logger.error("Error saving WhatsApp log: {}", e.getMessage(), e);
        }
    }

    private void sendWhatsAppMessage(PredictedReminderDate predictedReminderDate) {
        LocalDateTime startTime = LocalDateTime.now();
        logger.info("Sending WhatsApp message for user {} at: {}", predictedReminderDate.getUser().getUsername(), startTime);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(whatsappApiToken);

        // Construct JSON payload
        String jsonPayload = "{\n" +
                "    \"messaging_product\": \"whatsapp\",\n" +
                "    \"recipient_type\": \"individual\",\n" +
                "    \"to\": \"whatsapp: " + predictedReminderDate.getUser().getNoTelp() + "\",\n" +
                "    \"type\": \"text\",\n" +
                "    \"text\": {\n" +
                "        \"preview_url\": false,\n" +
                "        \"body\": \"Halo " + predictedReminderDate.getUser().getName() + "!\\n\\n" +
                "Jangan lupa, masih ada tagihan *_" + predictedReminderDate.getBillType().toLowerCase() + "_* yang perlu dibayar nih.\\n\\n" +
                "Biar makin asik, yuk bayar sekarang!\"\n" +
                "    }\n" +
                "}";

        HttpClient httpClient = HttpClient.newHttpClient();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(whatsappApiUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + whatsappApiToken)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HttpStatus.OK.value()) {
                logger.info("WhatsApp Message Sent: {}", response.body());
                logger.info("Scheduled message sent at: {}", LocalDateTime.now());
            } else {
                logger.error("Error sending WhatsApp message. Status Code: {}", response.statusCode());
                logger.error("Response Body: {}", response.body());
            }
        } catch (HttpClientErrorException.Unauthorized ex) {
            logger.error("Unauthorized access. Check your authorization token.", ex);
        } catch (Exception e) {
            logger.error("Error sending WhatsApp message. {}", e.getMessage(), e);
        }
    }


    private String createWhatsAppMessage(PredictedReminderDate predictedReminderDate) {
        // Implementasi logika untuk menghasilkan pesan WhatsApp berdasarkan PredictedReminderDate
        // Sesuaikan dengan kebutuhan aplikasi

        return "Halo " + predictedReminderDate.getUser().getName() + "!\n" +
                "Jangan lupa, masih ada tagihan " + predictedReminderDate.getBillType() + " yang perlu dibayar nih.\n" +
                "Biar makin asik, yuk bayar sekarang!";
    }

    @Transactional
    public void deleteDataByUserId(Long userId) {
        whatsappRepository.deleteByPredictedReminderDate_User_Id(userId);
    }
}
