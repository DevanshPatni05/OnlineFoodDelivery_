package com.FoodDelivery.Project.Controller;

import com.FoodDelivery.Project.Entity.Admin;
import com.FoodDelivery.Project.Entity.Restaurant;
import com.FoodDelivery.Project.Services.AdminServices;
//import jdk.internal.jshell.tool.ConsoleIOContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AdminControllerTests {

        @Mock
        private AdminServices adminServices;

        @InjectMocks
        private AdminController adminController;


        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }
    @Test
    void testLogin_Success() throws Exception {
        Admin admin= new Admin();
        admin.setName("Ram");
        admin.setPassword("5514");

        when(adminServices.verify(admin)).thenReturn("Login successful");

        String response = adminController.login(admin);

        assertEquals("Login successful", response);
    }

    @Test
    void testSignUpAdmin_Success() {
        Admin admin = new Admin();
        admin.setName("Ram");

        when(adminServices.insert(admin)).thenReturn(admin);

        String response = adminController.signup(admin);

        assertEquals(response,"OK");
        assertNotNull(response);
    }

    @Test
    void testAllEntry_Success() {
        List<Admin> admins = new ArrayList<>();
        admins.add(new Admin());
        admins.add(new Admin());

        when(adminServices.allEntry()).thenReturn(admins);

        List<Admin> response = adminController.allEntry();

        assertEquals(2, response.size());
    }


    @Test
    void testInsertRestaurant_Success() {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(1L);
        restaurant.setName("Alav");

        when(adminServices.insertRestaurant(restaurant)).thenReturn(restaurant);

        ResponseEntity<Restaurant> response = adminController.insertRestaurant(restaurant);

        assertNotNull(response);
        assertEquals(200, ((org.springframework.http.ResponseEntity<?>) response).getStatusCodeValue());
        assertEquals(restaurant, response.getBody());

       verify(adminServices, times(1)).insertRestaurant(restaurant);
    }
}






