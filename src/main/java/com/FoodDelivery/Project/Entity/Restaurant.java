package com.FoodDelivery.Project.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name="Restaurant")
@Getter
@Setter
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long restaurantId;
    private String name;
    private String mail;
    private String password;

    @OneToMany(mappedBy = "restaurant")
    List<Menuu> menu=new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    @JsonIgnore
    List<Order>orders=new ArrayList<>();

}
