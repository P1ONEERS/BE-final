package com.example.jwtProject.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.persistence.*;
import lombok.Setter;

@Data
@Entity
@Table(name = "item")
public class Ocr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_item;

    @Column(name = "name")
    private String description;

    @Column(name = "quantity")
    private int quantity;
    // Setter for totalAmount

    @Column(name = "price")
    private int total_amount;

}