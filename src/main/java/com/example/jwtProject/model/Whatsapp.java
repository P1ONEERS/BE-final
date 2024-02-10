package com.example.jwtProject.model;


import jakarta.persistence.*;

@Entity
@Table(name = "whatsapp")
public class Whatsapp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "predicted_reminder_date_id")
    private PredictedReminderDate predictedReminderDate;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "sent", nullable = false)
    private Boolean sent;

    @Column(name = "bill_type")
    private String billType;

    public Whatsapp() {
        // Default constructor
    }

    public Whatsapp(PredictedReminderDate predictedReminderDate, String message, Boolean sent, String billType) {
        this.predictedReminderDate = predictedReminderDate;
        this.message = message;
        this.sent = sent;
        this.billType = billType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PredictedReminderDate getPredictedReminderDate() {
        return predictedReminderDate;
    }

    public void setPredictedReminderDate(PredictedReminderDate predictedReminderDate) {
        this.predictedReminderDate = predictedReminderDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSent() {
        return sent;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }
}

