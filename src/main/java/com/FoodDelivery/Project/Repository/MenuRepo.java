package com.FoodDelivery.Project.Repository;
import com.FoodDelivery.Project.Entity.Menuu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepo extends JpaRepository<Menuu,Long> {
    List<Menuu> findByRestaurant_restaurantId(Long restaurantId);


}
