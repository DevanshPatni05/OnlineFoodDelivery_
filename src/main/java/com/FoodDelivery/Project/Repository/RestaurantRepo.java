package com.FoodDelivery.Project.Repository;

import com.FoodDelivery.Project.Entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepo extends JpaRepository<Restaurant,Long> {


}
