package com.example.jwtProject.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name= "email", nullable = false, unique = true)
    private String email;

    @Column(name = "mpin", nullable = false)
    private String mpin;

    @Column(name = "balance",nullable = false)
    private Double balance;

    @Column(name = "account_type", nullable = false)
    private String accountType;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "transaction_code", nullable = false)
    private String transactionCode;

    @Column(name = "notification_enabled", columnDefinition = "boolean default false")
    private boolean notificationEnabled;
}