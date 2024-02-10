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
//    private Long id_total;

    @Column(name = "grand_total")
    private Double grand_total;
//
    @OneToMany(mappedBy = "splitBill", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("splitBill")
    private List<Total> totals = new ArrayList<>();

//    @ManyToOne
//    @JoinColumn(name = "id_user")
//    private Users user;

    public SplitBill(){

    }

    public Long getId_trans() {
        return id_trans;
    }

    public void setId_trans(Long id_trans) {
        this.id_trans = id_trans;
    }


//    public Long getId_total() {
//        return id_total;
//    }
//
//    public void setId_total(Long id_total) {
//        this.id_total = id_total;
//    }
//
//    public Total getTotal() {
//        return total;
//    }

//    public void setTotals(Total total) {
//        this.total = total;
//    }

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





}

