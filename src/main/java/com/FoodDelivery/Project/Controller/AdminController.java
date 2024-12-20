package com.FoodDelivery.Project.Controller;


import com.FoodDelivery.Project.Entity.Admin;
import com.FoodDelivery.Project.Entity.Restaurant;
import com.FoodDelivery.Project.Services.AdminServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Admin")
@Tag(name="Admin's API's")
public class AdminController {

    @Autowired
    private AdminServices adminServices;



    @PostMapping("/login")
    public String login(@RequestBody Admin admin) throws Exception {
       System.out.println("inside login");
        return adminServices.verify(admin);
    }

    @PostMapping("/signup")
    public String signup(@RequestBody Admin admin)
    {
        adminServices.insert(admin);
        return "OK";
    }


    @GetMapping("/allEntry")
    public List<Admin> allEntry()
    {
        return adminServices.allEntry();
    }

    @PostMapping("/insertRestaurant")
    public ResponseEntity<Restaurant> insertRestaurant(@RequestBody Restaurant restaurant)
    {
        adminServices.insertRestaurant(restaurant);
        return new ResponseEntity<>(restaurant,HttpStatus.OK);
    }



}
