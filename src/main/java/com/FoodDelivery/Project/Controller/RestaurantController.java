package com.FoodDelivery.Project.Controller;


import com.FoodDelivery.Project.Entity.Menuu;
import com.FoodDelivery.Project.Entity.Order;
import com.FoodDelivery.Project.Services.MenuServices;
import com.FoodDelivery.Project.Services.RestaurantServices;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/Restaurant")
public class RestaurantController {

    @Autowired
    RestaurantServices restaurantServices;

    @Autowired
    MenuServices menuServices;

    @PostMapping("/enterItems/{restaurant_id}")
    public void insertItems(@PathVariable Long restaurant_id ,@RequestBody Menuu menu) {


        menuServices.addMenuForRestaurant(restaurant_id,menu);
    }

    @DeleteMapping("/deleteItem/{id}/{restaurant_id}")
    public Menuu deleteItems(@PathVariable Long id,@PathVariable Long restaurant_id)
    {
        return menuServices.deleteItems(id,restaurant_id);
    }

    @GetMapping("/GetMenu/{restaurantId}")
    public List<Menuu> getMenuByRestaurant(@PathVariable Long restaurantId)
    {
        return menuServices.findByRestaurant(restaurantId);
    }

    @GetMapping("/viewOrders/{restaurant_id}")
    public List<Order> viewOrder(Long restaurant_id)
    {
        return restaurantServices.viewOrders(restaurant_id);
    }


}


