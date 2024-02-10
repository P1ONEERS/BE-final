package com.example.jwtProject.repository;

import com.example.jwtProject.entity.Ocr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OcrRepository extends JpaRepository<Ocr, Long> {
    // Any additional query methods can be added here if needed
}
