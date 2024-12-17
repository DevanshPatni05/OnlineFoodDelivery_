package com.FoodDelivery.Project.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="Admin_Table")
@Getter
@Setter
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long adminId;
    private String name;
    private String mail;
    private String password;
    private String role;

}
