package com.FoodDelivery.Project.Repository;

import com.FoodDelivery.Project.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem,Long> {
}
