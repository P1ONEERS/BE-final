package com.example.jwtProject.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "predicted_reminder_date")
public class PredictedReminderDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "bill_type", nullable = false)
    private String billType;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "billing_period", nullable = false)
    private String billingPeriod;

    @Column(name = "last_updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime lastUpdatedAt;

    public PredictedReminderDate() {
    }

    public PredictedReminderDate(User user, String billType, LocalDateTime dueDate, String billingPeriod, LocalDateTime lastUpdatedAt) {
        this.user = user;
        this.billType = billType;
        this.dueDate = dueDate;
        this.billingPeriod = billingPeriod;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getBillingPeriod() {
        return billingPeriod;
    }

    public void setBillingPeriod(String billingPeriod) {
        this.billingPeriod = billingPeriod;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }
}

