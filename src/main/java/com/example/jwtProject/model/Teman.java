package com.example.jwtProject.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="teman")
@Data
public class Teman {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_teman")
    private Long id_user;

    @Column(name = "name_teman")
    private String name;

    @Column(name = "no_telp")
    private String NoTelp;

    @JsonIgnore
    @OneToMany(mappedBy = "teman", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Item> items;


    @Override
    public String toString() {
        StringBuilder itemsString = new StringBuilder();
        for (Item item : items) {
            itemsString.append("- ").append(item.getName()).append(": ").append(item.getQuantity()).append("\n");
        }

        return "Users{" +
                "id_user=" + id_user +
                ", name='" + name + '\'' +
                ", NoTelp='" + NoTelp + '\'' +
                ", items=" + itemsString.toString() +
                '}';
    }

    public Teman(){

    }

    public Teman(String name){
        this.name = name;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoTelp() {
        return NoTelp;
    }

    public void setNoTelp(String NoTelp) {
        this.NoTelp = NoTelp;
    }
}