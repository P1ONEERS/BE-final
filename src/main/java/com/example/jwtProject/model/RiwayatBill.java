package com.example.jwtProject.model;


import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "riwayatbill")

public class RiwayatBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_riwayatbill")
    private Long id_riwayatbill;

    @Column(name = "total_seluruh_teman")
    private double total_seluruh_teman;



    // Getter and Setter methods


    public Long getIdRiwayatBill() {
        return id_riwayatbill;
    }

    public void setIdRiwayatBill(Long idRiwayatBill) {
        this.id_riwayatbill = idRiwayatBill;
    }

    public double getTotalSeluruhTeman() {
        return total_seluruh_teman;
    }

    public void setTotalSeluruhTeman(double totalSeluruhTeman) {
        this.total_seluruh_teman = totalSeluruhTeman;
    }

}

