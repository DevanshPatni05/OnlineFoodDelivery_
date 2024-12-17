package com.FoodDelivery.Project.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="menu_table")
@Getter
@Setter
public class Menuu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long  id;
    private String dishName;
    private String description;
    private String price;
    private String availability;
    @ManyToOne
            @JoinColumn(name="restaurant_id" )
            @JsonIgnore
    Restaurant restaurant;

    @OneToMany(mappedBy = "menu")
    List<OrderItem> orderItems=new ArrayList<>();



}
