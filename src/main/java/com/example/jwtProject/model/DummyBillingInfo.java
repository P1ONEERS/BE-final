package com.example.jwtProject.model;


import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "dummy_billing_info")
public class DummyBillingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idpel", nullable = false, unique = true)
    private String idpel;

    @Column(name = "nominal_payment", nullable = false)
    private BigDecimal nominalPayment;

    @Column(name = "bill_type", nullable = false)
    private String billType;

    @Column(name = "admin_fee", nullable = false)
    private BigDecimal adminFee;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public DummyBillingInfo() {
    }

    public DummyBillingInfo(String idpel, BigDecimal nominalPayment, BigDecimal adminFee, User user) {
        this.idpel = idpel;
        this.nominalPayment = nominalPayment;
        this.adminFee = adminFee;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdpel() {
        return idpel;
    }

    public void setIdpel(String idpel) {
        this.idpel = idpel;
    }


    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public BigDecimal getNominalPayment() {
        return nominalPayment;
    }

    public void setNominalPayment(BigDecimal nominalPayment) {
        this.nominalPayment = nominalPayment;
    }

    public BigDecimal getAdminFee() {
        return adminFee;
    }

    public void setAdminFee(BigDecimal adminFee) {
        this.adminFee = adminFee;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
