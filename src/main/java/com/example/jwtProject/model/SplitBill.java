package com.example.jwtProject.model;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="bill_split")

public class SplitBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trans")
    private Long id_trans;

    private Double grand_total;

    @ManyToOne
    @JoinColumn(name = "id_riwayatbill")
    @JsonIgnoreProperties("bills")
    private RiwayatBill riwayatBill;


    @OneToMany(mappedBy = "splitBill", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("splitBill")
    private List<Total> totals = new ArrayList<>();





    public SplitBill(){

    }


    public Long getIdRiwayatBill() {
        return (riwayatBill != null) ? riwayatBill.getIdRiwayatBill() : null;
    }

    public void setIdRiwayatBill(Long idRiwayatBill) {
        if (riwayatBill == null) {
            riwayatBill = new RiwayatBill();
        }
        riwayatBill.setIdRiwayatBill(idRiwayatBill);
    }


    public Long getId_trans() {
        return id_trans;
    }

    public void setId_trans(Long id_trans) {
        this.id_trans = id_trans;
    }



    public List<Total> getTotals() {
        return totals;
    }

    public void setTotals(List<Total> totals) {
        this.totals = totals;
    }

    public Double getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(Double grand_total) {
        this.grand_total = grand_total;
    }

    //get set


    public RiwayatBill getRiwayatBill() {
        return riwayatBill;
    }

    public void setRiwayatBill(RiwayatBill riwayatBill) {
        this.riwayatBill = riwayatBill;
    }
}

