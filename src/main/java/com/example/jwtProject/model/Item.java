package com.example.jwtProject.model;

import jakarta.persistence.*;

@Entity
@Table(name="item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_item")
    private Long id_item;
    private String name;
    private Double price;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name="id_teman")
    private Teman teman;

    @Column(name="flag")
    private int flag;

    // Buat getter dan setter untuk flag
    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
    public Item(){

    }

    public Item(String name,Double price, Integer quantity, Teman teman){
        this.name=name;
        this.price= price;
        this.quantity= quantity;
        this.teman = teman;
    }

    //get and set


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getId_item() {
        return id_item;
    }

    public void setId_item(Long id_item) {
        this.id_item = id_item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Teman getUsers() {
        return teman;
    }

    public void setUsers(Teman teman) {
        this.teman = teman;
    }
}
