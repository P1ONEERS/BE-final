package com.example.splitbill.controller;
import com.example.splitbill.entity.LineItem;
import com.example.splitbill.service.OcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.splitbill.entity.Ocr;
import java.util.List;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@RestController
@RequestMapping("/api/ocr")
public class OcrController {
    @Autowired
    private OcrService ocrService;

    @PostMapping("/send-json")
    public ResponseEntity<String> sendJsonToDb(@RequestBody List<Ocr> ocrDataList) {
        try {
            ocrService.sendJson(ocrDataList);
            return ResponseEntity.ok("Data sent successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request");
        }
    }


}
