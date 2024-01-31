package com.example.jwtProject.service;
import com.example.jwtProject.model.Ocr;
import com.example.jwtProject.repository.OcrRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OcrService {
    private final OcrRepository ocrRepository;

    public void sendJson(List<Ocr> ocrDataList) {
        for (Ocr ocr : ocrDataList) {
            System.out.println("Description: " + ocr.getDescription());
            System.out.println("getQuantity: " + ocr.getQuantity());
            System.out.println("Total Amount: " + ocr.getTotal_amount());
        }
        ocrRepository.saveAll(ocrDataList);
    }
}