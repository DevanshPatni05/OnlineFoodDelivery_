package com.FoodDelivery.Project.Services;

import com.FoodDelivery.Project.Entity.Menuu;
import com.FoodDelivery.Project.Entity.Order;
import com.FoodDelivery.Project.Entity.OrderItem;
import com.FoodDelivery.Project.Entity.Restaurant;
import com.FoodDelivery.Project.Repository.OrderItemRepo;
import com.FoodDelivery.Project.Repository.OrderRepo;
import com.FoodDelivery.Project.Repository.RestaurantRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RestaurantServicesTest {

    @Mock
    private RestaurantRepo restaurantRepo;

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private OrderItemRepo orderItemRepo;

    @InjectMocks
    private RestaurantServices restaurantServices;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        restaurantServices=new RestaurantServices();
        restaurantServices.restaurantRepo=restaurantRepo;
        restaurantServices.orderRepo=orderRepo;
    }

    @Test
    void testViewOrders_Success() {

        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        List<Order> orders = new ArrayList<>();
        orders.add(new Order());

        when(restaurantRepo.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(orderRepo.findByRestaurant_RestaurantId(restaurantId)).thenReturn(orders);

        List<Order> result = restaurantServices.viewOrders(restaurantId);

        assertNotNull(result);
        assertEquals(1, result.size());

    }


        @Test
        void testViewOrders_InvalidRestaurantId() {
            Long restaurantId = 1L;

            when(restaurantRepo.findById(restaurantId)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class, () -> restaurantServices.viewOrders(restaurantId));
            assertEquals("Invalid restaurant id or no orders related to restaurant", exception.getMessage());
            verifyNoInteractions(orderRepo);
        }

}
