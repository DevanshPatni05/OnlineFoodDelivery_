package com.FoodDelivery.Project.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CustomerId;
    private String name;
    private String password;
    private String mail;
    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "customer")
            @JsonIgnore
    List<Order> orders=new ArrayList<>();




    public String getRole() {
        return role;
    }


}