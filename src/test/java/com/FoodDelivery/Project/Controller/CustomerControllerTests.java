package com.FoodDelivery.Project.Controller;

import com.FoodDelivery.Project.Entity.Customer;
import com.FoodDelivery.Project.Entity.Menuu;
import com.FoodDelivery.Project.Entity.Order;
import com.FoodDelivery.Project.Services.CustomerServices;
import com.FoodDelivery.Project.Services.CustomerServicesTests;
import com.FoodDelivery.Project.Services.MenuServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class CustomerControllerTests {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerServices customerServices;

    @Mock
    private MenuServices menuServices;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHealthCheck_ReturnsOK()
    {
        String response=customerController.healthCheck();
        assertEquals("OK",response);
    }

    @Test
    void testInsertCustomer_Success() {
        Customer customer = new Customer();
        customer.setName("Ram");

        when(customerServices.save(customer)).thenReturn(customer);

        ResponseEntity<Customer> response = customerController.insert(customer);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Ram", response.getBody().getName());
    }

    @Test
    void testAllEntry_Success() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer());
        customers.add(new Customer());

        when(customerServices.allEntry()).thenReturn(customers);

        List<Customer> response = customerController.allEntry();

        assertEquals(2, response.size());
    }

    @Test
     void testLogin_Success() {
        Customer customer = new Customer();
        customer.setName("Ram");
        customer.setPassword("5514");

        when(customerServices.verify(customer)).thenReturn("Login successful");

        String response = customerController.login(customer);

        assertEquals("Login successful", response);
    }

    @Test
    void testUpdateCustomer_Success() {
        long id = 1L;
        Customer customer = new Customer();
        customer.setName("Laxman");

        when(customerServices.update(id, customer)).thenReturn(Optional.of(customer));

        ResponseEntity<Customer> response = customerController.update(id, customer);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Laxman", response.getBody().getName());
    }

    @Test
    void testUpdateCustomer_NotFound() {
        long id = 1L;
        Customer customer = new Customer();

        when(customerServices.update(id, customer)).thenReturn(Optional.empty());

        ResponseEntity<Customer> response = customerController.update(id, customer);

        assertEquals(404, response.getStatusCodeValue());
    }
    @Test
    void testDeleteCustomer_Success() {
        String name = "John Doe";
        Customer customer = new Customer();
        customer.setName(name);

        when(customerServices.delete(name)).thenReturn(customer);

        ResponseEntity<Customer> response = customerController.delete(name);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(name, response.getBody().getName());
    }

    @Test
    void testDeleteCustomer_NotFound() {
        String name = "NonExistent";

        when(customerServices.delete(name)).thenReturn(null);

        ResponseEntity<Customer> response = customerController.delete(name);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testAllMenuItems_Success() {
        List<Menuu> menuItems = new ArrayList<>();
        menuItems.add(new Menuu());
        menuItems.add(new Menuu());

        when(menuServices.allMenuItems()).thenReturn(menuItems);

        List<Menuu> response = customerController.allMenuItems();

        assertEquals(2, response.size());
    }

    @Test
    void testPlaceOrder_Success() {
        Long customerId = 1L;
        Order order = new Order();

        when(customerServices.placeOrder(order, customerId)).thenReturn(order);

        Order response = customerController.placeOrder(order, customerId);

        assertNotNull(response);
    }


}
