package com.FoodDelivery.Project.Entity;


import jakarta.persistence.*;
import jdk.dynalink.linker.LinkerServices;
import lombok.*;

import java.sql.Struct;
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
    List<Order> orders=new ArrayList<>();




    public String getRole() {
        return role;
    }

}