package com.FoodDelivery.Project.Repository;

import com.FoodDelivery.Project.Entity.Order;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo  extends JpaRepository<Order,Long> {
    List<Order> findByRestaurant_RestaurantId(Long restaurantId);

}
