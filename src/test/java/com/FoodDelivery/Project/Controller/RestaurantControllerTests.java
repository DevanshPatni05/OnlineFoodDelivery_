package com.FoodDelivery.Project.Controller;

import com.FoodDelivery.Project.Entity.Menuu;
import com.FoodDelivery.Project.Entity.Order;
import com.FoodDelivery.Project.Services.MenuServices;
import com.FoodDelivery.Project.Services.RestaurantServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RestaurantControllerTests {

    @InjectMocks
    private RestaurantController restaurantController;

    @Mock
    private RestaurantServices restaurantServices;

    @Mock
    private MenuServices menuServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
        @Test
        void testInsertItems_Success() {
            Long restaurantId = 1L;
            Menuu menu = new Menuu();
            menu.setDishName("Pizza");

            when(menuServices.addMenuForRestaurant(restaurantId, menu)).thenReturn(menu);


            String response=restaurantController.insertItems(restaurantId, menu);


            assertNotNull(response, "Response should not be null");
            assertEquals(response, "Inserted");

        }
        @Test
        void testDeleteItems_Success() {
            Long itemId = 1L;
            Long restaurantId = 1L;
            Menuu menu = new Menuu();
            menu.setId(itemId);

            when(menuServices.deleteItems(itemId, restaurantId)).thenReturn(menu);

            Menuu response = restaurantController.deleteItems(itemId, restaurantId);

            assertNotNull(response);
            assertEquals(itemId, response.getId());

        }

        @Test
        void testDeleteItems_NotFound() {
            Long itemId = 1L;
            Long restaurantId = 1L;

            when(menuServices.deleteItems(itemId, restaurantId)).thenReturn(null);

            Menuu response = restaurantController.deleteItems(itemId, restaurantId);

            assertNull(response);

        }

        // Test for getting menu by restaurant
        @Test
        void testGetMenuByRestaurant_Success() {
            Long restaurantId = 1L;
            List<Menuu> menuList = new ArrayList<>();
            menuList.add(new Menuu());
            menuList.add(new Menuu());

            when(menuServices.findByRestaurant(restaurantId)).thenReturn(menuList);

            List<Menuu> response = restaurantController.getMenuByRestaurant(restaurantId);

            assertNotNull(response);
            assertEquals(2, response.size());

        }

        @Test
        void testGetMenuByRestaurant_NoData() {
            Long restaurantId = 1L;

            when(menuServices.findByRestaurant(restaurantId)).thenReturn(new ArrayList<>());

            List<Menuu> response = restaurantController.getMenuByRestaurant(restaurantId);

            assertNotNull(response);
            assertTrue(response.isEmpty());
        }
        @Test
        void testViewOrder_Success() {
            Long restaurantId = 1L;
            List<Order> orders = new ArrayList<>();
            orders.add(new Order());
            orders.add(new Order());

            when(restaurantServices.viewOrders(restaurantId)).thenReturn(orders);

            List<Order> response = restaurantController.viewOrder(restaurantId);

            assertNotNull(response);
            assertEquals(2, response.size());
        }

        @Test
        void testViewOrder_NoOrders() {
            Long restaurantId = 1L;

            when(restaurantServices.viewOrders(restaurantId)).thenReturn(new ArrayList<>());

            List<Order> response = restaurantController.viewOrder(restaurantId);

            assertNotNull(response);
            assertTrue((response).isEmpty());
}
    }

