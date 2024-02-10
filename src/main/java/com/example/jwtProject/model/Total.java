package com.example.jwtProject.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name="total")

public class Total {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_total")

    private Long id_total;
    private Double total;

    @ManyToOne
    @JoinColumn(name="id_item")
    private Item item;
    @ManyToOne
    @JoinColumn(name="id_trans")
    @JsonIgnoreProperties("totals")
    private SplitBill splitBill;

    public Total(){

    }

    public Total(Double total){
        this.total = total;
    }

    //get set


    public Long getId_total() {
        return id_total;
    }

    public void setId_total(Long id_total) {
        this.id_total = id_total;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public SplitBill getBill() {
        return splitBill;
    }

    public void setBill(SplitBill splitBill) {
        this.splitBill = splitBill;
    }

}
