package com.FoodDelivery.Project.Controller;

import com.FoodDelivery.Project.Entity.Admin;
import com.FoodDelivery.Project.Services.AdminServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


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





}
