package com.FoodDelivery.Project.Services;

import com.FoodDelivery.Project.Entity.Order;
import com.FoodDelivery.Project.Entity.Restaurant;
import com.FoodDelivery.Project.Repository.OrderRepo;
import com.FoodDelivery.Project.Repository.RestaurantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Qualifier("restaurantServices")
public class RestaurantServices implements UserDetailsService {

    @Autowired
    RestaurantRepo restaurantRepo;

    @Autowired
    OrderRepo orderRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }


    public List<Order> viewOrders(Long restaurantId)
    {
        Optional<Restaurant> restaurant=restaurantRepo.findById(restaurantId);
        if(restaurant.isPresent())
        {
            List<Order> orders = orderRepo.findByRestaurant_RestaurantId(restaurantId);
            if(orders.isEmpty())
            {
                throw new RuntimeException("There are no orders");
            }
            return orders;

        }
        throw new RuntimeException("Invalid restaurant id or no orders related to restaurant");
    }


}
