package com.FoodDelivery.Project.Services;


import com.FoodDelivery.Project.Entity.Menuu;
import com.FoodDelivery.Project.Entity.Restaurant;
import com.FoodDelivery.Project.Repository.MenuRepo;
import com.FoodDelivery.Project.Repository.RestaurantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MenuServices {

    @Autowired
    private MenuRepo menuRepo;

    @Autowired
    private RestaurantRepo restaurantRepo;



    public List<Menuu> allMenuItems()
    {
        return menuRepo.findAll();

    }
    public Menuu addMenuForRestaurant(Long restaurantId, Menuu menu)

    {
        Optional<Restaurant> restaurant=restaurantRepo.findById(restaurantId);

        if(restaurant.isPresent())
        {
           menu.setRestaurant(restaurant.get());
            return menuRepo.save(menu);
        }

        throw new RuntimeException("Restaurant Not found");
    }

    public Menuu deleteItems(Long id,Long restaurant_Id)
    {
        Optional<Restaurant> restaurant=restaurantRepo.findById(restaurant_Id);

        if(restaurant.isPresent())
        {
            Restaurant restaurant1=restaurant.get();

            Optional<Menuu>item=menuRepo.findById(id);
            if(item.isEmpty())
            {
                throw new RuntimeException("Menu not found with ID"+id);
            }
            Menuu menu=item.get();
            if(menu.getRestaurant().getRestaurantId()==restaurant_Id)
            {
                 menuRepo.deleteById(id);
                 return menu;
            }
            throw new RuntimeException("Menu does not belong to the restaurant");

        }
        throw new RuntimeException("Cannot find the restaurant");
    }

    public List<Menuu> findByRestaurant(Long restaurantId)
    {
        Optional<Restaurant> restaurant=restaurantRepo.findById(restaurantId);
        if(restaurant.isPresent())
        {
            return menuRepo.findByRestaurant_restaurantId(restaurantId);
        }
        throw new RuntimeException("Cannot find restaurant");
    }




}
