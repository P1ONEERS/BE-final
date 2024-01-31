package com.example.splitbill.repository;

import com.example.splitbill.entity.Ocr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OcrRepository extends JpaRepository<Ocr, Long> {
    // Any additional query methods can be added here if needed
}
