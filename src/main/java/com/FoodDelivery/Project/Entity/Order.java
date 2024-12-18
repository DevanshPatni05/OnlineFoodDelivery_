package com.FoodDelivery.Project.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="restaurant_id")
    @JsonIgnore
    private Restaurant restaurant;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    List<OrderItem> orderItems=new ArrayList<>();


}
