package com.example.jwtProject.model;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "bill_type", nullable = false)
    private String billType;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "billing_period", nullable = false)
    private String billingPeriod;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "dummy_billing_info_id")
    private DummyBillingInfo dummyBillingInfo;

    @Column(name = "nominal_payment", precision = 10, scale = 2)
    private BigDecimal nominalPayment;

    @Column(name = "admin_fee", precision = 10, scale = 2)
    private BigDecimal adminFee;

    @Column(name = "is_paid", nullable = false)
    private boolean isPaid;

    @Column(name = "reference_number", unique = true)
    private String referenceNumber;

    @Column(name = "transaction_completion_time")
    private String transactionCompletionTime;


    // Constructor
    public Bill() {
    }

    // Getters and Setters

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

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getNominalPayment() {
        return nominalPayment;
    }

    public void setNominalPayment(BigDecimal nominalPayment) {
        this.nominalPayment = nominalPayment;
    }

    // Getter dan setter untuk properti adminFee
    public BigDecimal getAdminFee() {
        return adminFee;
    }

    public void setAdminFee(BigDecimal adminFee) {
        this.adminFee = adminFee;
    }

    public String getBillingPeriod() {
        return billingPeriod;
    }

    public void setBillingPeriod(String billingPeriod) {
        this.billingPeriod = billingPeriod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public DummyBillingInfo getDummyBillingInfo() {
        return dummyBillingInfo;
    }

    public void setDummyBillingInfo(DummyBillingInfo dummyBillingInfo) {
        this.dummyBillingInfo = dummyBillingInfo;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getTransactionCompletionTime() {
        return transactionCompletionTime;
    }

    public void setTransactionCompletionTime(String transactionCompletionTime) {
        this.transactionCompletionTime = transactionCompletionTime;
    }

    public String getDueDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dueDate.format(formatter);
    }

    public void setDueDateString(String dueDateString) {
        this.dueDate = LocalDate.parse(dueDateString);
    }
}

