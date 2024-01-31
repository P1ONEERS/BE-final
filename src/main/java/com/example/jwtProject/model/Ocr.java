package com.example.jwtProject.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.persistence.*;
import lombok.Setter;

@Data
@Entity
@Table(name = "line_items")
public class Ocr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private int quantity;
    // Setter for totalAmount

    @Column(name = "total_amount")
    private int total_amount;

}

