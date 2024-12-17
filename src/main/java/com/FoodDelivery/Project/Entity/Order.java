package com.FoodDelivery.Project.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="order_table")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime  orderDateTime;

    @ManyToOne
            @JoinColumn(name="customer_id")
    Customer customer;

    @ManyToOne
    @JoinColumn(name="restaurant_id")
    Restaurant restaurant;

    @OneToMany(mappedBy = "order")
    List<OrderItem> orderItems=new ArrayList<>();


}
