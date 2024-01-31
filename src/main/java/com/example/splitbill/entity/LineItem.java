package com.example.splitbill.entity;

import lombok.Data;


@Data
public class LineItem {
    private String description;
    private Integer quantity;
    private Integer totalAmount;
    // Include other properties if needed
}