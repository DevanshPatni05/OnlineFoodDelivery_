package com.FoodDelivery.Project.Services;

import com.FoodDelivery.Project.Entity.*;
import com.FoodDelivery.Project.Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class
CustomerServicesTests {

    @Mock
    CustomerRepo customerRepo;

    @Mock
    AuthenticationManager authManager;

    @Mock
    JWTService service;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    CustomerServices customerServices;

    @Mock
    OrderItemRepo orderItemRepo;

    @Mock
    OrderRepo orderRepo;

    @Mock
    MenuRepo menuRepo;

    @Mock
    RestaurantRepo restaurantRepo;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


    }


    @Test
    public void testLoadUserByName() {
        Customer customer = new Customer();
        customer.setName("ram");
        customer.setCustomerId(1L);
        customer.setMail("ram@gmail.com");
        when(customerRepo.findByName("ram")).thenReturn(customer);

        UserPrincipal userPrincipal = (UserPrincipal) customerServices.loadUserByUsername("ram");

        assertNotNull(userPrincipal, "Customer should not be null");
        assertEquals("ram", userPrincipal.getUsername());

    }

    @Test
    public void testALlEntry() {
        Customer customer = new Customer();
        customer.setName("ram");
        customer.setCustomerId(1L);
        customer.setMail("ram@gmail.com");

        Customer customer1 = new Customer();
        customer1.setName("ram");
        customer1.setCustomerId(1L);
        customer1.setMail("ram@gmail.com");

        List<Customer> mockCustomer = new ArrayList<>();
        mockCustomer.add(customer);
        mockCustomer.add(customer1);

        when(customerRepo.findAll()).thenReturn(mockCustomer);

        List<Customer> customers = customerServices.allEntry();

        assertNotNull(customers, "The list should not be null");
        assertEquals("ram", customers.get(0).getName());

    }

    @Test
    public void testVerify_SuccessfulAuthentication() {
        Customer customer = new Customer();
        customer.setName("ram");
        customer.setPassword("5514");

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);


        String generateToken = "ajdoannof";
        when(service.generateToken(any(UserPrincipal.class))).thenReturn(generateToken);

        String result = customerServices.verify(customer);

        assertEquals(generateToken, result);

    }

    @Test
    public void testVerify_FailedAuthentication() {
        Customer customer = new Customer();
        customer.setName("ram");
        customer.setPassword("wrong password");

        Authentication authentication = mock(Authentication.class);

        when(authentication.isAuthenticated()).thenReturn(false);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        String result = customerServices.verify(customer);

        assertEquals("Invalid credentials", result);

        verify(authManager).authenticate(new UsernamePasswordAuthenticationToken("ram", "wrong password"));
        verify(service, never()).generateToken(any(UserPrincipal.class));


    }

    @Test
    public void testDelete_Success() {
        String name = "ram";
        Customer customer = new Customer();
        customer.setName(name);

        when(customerRepo.findByName(name)).thenReturn(customer);

        Customer result = customerServices.delete(name);

        assertEquals(customer, result);
        verify(customerRepo).delete(customer);
        verify(customerRepo).findByName(name);
    }

    @Test
    public void testDelete_Failed() {
        String name = "ram";
        Customer customer = new Customer();
        customer.setName(name);
        when(customerRepo.findByName(name)).thenReturn(null);

        Customer result = customerServices.delete(name);

        assertEquals(null, result);
        verify(customerRepo, never()).delete(any(Customer.class));
        verify(customerRepo).findByName(name);

    }

    @Test
    void testUpdate() {
        Long id = 1L;
        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(id);
        existingCustomer.setName("Ram");
        existingCustomer.setMail("ram@gmail.com");

        Customer updateCustomer = new Customer();
        updateCustomer.setCustomerId(id);
        updateCustomer.setName("Laxman");
        updateCustomer.setMail("laxman@gmail.com");

        when(customerRepo.findById(id)).thenReturn(Optional.of(existingCustomer));
        when(customerRepo.save(any(Customer.class))).thenReturn(updateCustomer);
    }

    @Test
    void testSaveCustomer() {
        // Arrange
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setPassword("plainPassword");
        String encodedPassword = "encodedPassword";

        when(bCryptPasswordEncoder.encode(customer.getPassword())).thenReturn(encodedPassword);

        when(customerRepo.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer savedCustomer = invocation.getArgument(0);
            savedCustomer.setRole("Customer");
            return savedCustomer;
        });
        Customer savedCustomer = customerServices.save(customer);

        assertNotNull(savedCustomer);
        assertEquals("Customer", savedCustomer.getRole());
        assertEquals(encodedPassword, savedCustomer.getPassword());
        verify(customerRepo, times(1)).save(customer);
        verify(bCryptPasswordEncoder, times(1)).encode("plainPassword");


    }


    @Test
    void testPlaceOrder() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(1L);

        Menuu menuItem = new Menuu();
        menuItem.setId(1L);

        OrderItem orderItem = new OrderItem();
        orderItem.setMenu(menuItem);
        orderItem.setQuantity(2);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderItems(orderItems);

        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(restaurantRepo.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuRepo.findById(1L)).thenReturn(Optional.of(menuItem));
        when(orderItemRepo.save(any(OrderItem.class))).thenReturn(orderItem);
        when(orderRepo.save(any(Order.class))).thenReturn(order);

        Order placedOrder = customerServices.placeOrder(order, 1L);

        assertNotNull(placedOrder);
        assertEquals(1L, placedOrder.getCustomer().getCustomerId());
        assertEquals(1L, placedOrder.getRestaurant().getRestaurantId());
        assertEquals(LocalDateTime.now().getMinute(), placedOrder.getOrderDateTime().getMinute());
        verify(orderItemRepo, times(1)).save(orderItem);
        verify(orderRepo, times(1)).save(placedOrder);
    }

    @Test
    void testPlaceOrder_CustomerNotFound() {
        Order order = new Order();
        order.setCustomer(new Customer());
        order.setOrderItems(new ArrayList<>());

        when(customerRepo.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> customerServices.placeOrder(order, 1L));
        assertEquals("invalid customer details", exception.getMessage());
    }

    @Test
    void testPlaceOrder_RestaurantNotFound() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderItems(new ArrayList<>());
        when(restaurantRepo.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> customerServices.placeOrder(order, 1L));
        assertEquals("invalid customer details", exception.getMessage());
    }
}