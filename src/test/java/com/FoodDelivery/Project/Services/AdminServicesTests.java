package com.FoodDelivery.Project.Services;

import com.FoodDelivery.Project.Entity.Admin;
import com.FoodDelivery.Project.Entity.Customer;
import com.FoodDelivery.Project.Entity.UserPrincipal;
import com.FoodDelivery.Project.Repository.AdminRepo;
import io.jsonwebtoken.Jwt;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminServicesTests {


    @Mock
    AdminRepo adminRepo;

    @InjectMocks
    AdminServices adminServices;

    @Mock
    AuthenticationManager authManager;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    JWTService service;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testRegisterAdmin() {
        Admin admin = new Admin();
        admin.setName("John Doe");
        admin.setPassword("plainPassword");
        String encodedPassword = "encodedPassword";

        when(bCryptPasswordEncoder.encode(admin.getPassword())).thenReturn(encodedPassword);

        when(adminRepo.save(ArgumentMatchers.any(Admin.class))).thenAnswer(invocation -> {
            Admin savedAdmin = invocation.getArgument(0);
            savedAdmin.setRole("Admin");
            return savedAdmin;
        });
        Admin saveAdmin = adminServices.insert(admin);

        assertNotNull(saveAdmin, "Saved admin should not be null.");
        assertEquals("Admin", saveAdmin.getRole(), "Role should be 'Admin'.");
        assertEquals(encodedPassword, saveAdmin.getPassword(), "Password should be encoded correctly.");
        verify(adminRepo, times(1)).save(admin);
        verify(bCryptPasswordEncoder, times(1)).encode("plainPassword");


    }
    @Test
    public void testLoadUserByName() {
        Admin admin = new Admin();
        admin.setName("ram");
        admin.setAdminId(1L);
        admin.setMail("ram@gmail.com");
        when(adminRepo.findByName("ram")).thenReturn(admin);

        UserPrincipal userPrincipal = (UserPrincipal) adminServices.loadUserByUsername("ram");

        assertNotNull(userPrincipal, "Customer should not be null");
        assertEquals("ram", userPrincipal.getUsername());

    }

    @Test
    public void testALlEntry() {
        Admin admin1 = new Admin();
        admin1.setName("ram");
        admin1.setAdminId(1L);
        admin1.setMail("ram@gmail.com");

        Admin admin2 = new Admin();
        admin2.setName("ram");
        admin2.setAdminId(2L);
        admin2.setMail("ram@gmail.com");

        List<Admin> mockCustomer = new ArrayList<>();
        mockCustomer.add(admin1);
        mockCustomer.add(admin2);

        when(adminRepo.findAll()).thenReturn(mockCustomer);

        List<Admin> admins = adminServices.allEntry();

        assertNotNull(admins, "The list should not be null");
        assertEquals("ram",admins.get(0).getName());

    }
    @Test
    public void testVerify_SuccessfulAuthentication() {
        Admin admin = new Admin();
        admin.setName("ram");
        admin.setAdminId(1L);
        admin.setMail("ram@gmail.com");

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authManager.authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);


        String generateToken = "ajdoannof";
        when(service.generateToken(ArgumentMatchers.any(UserPrincipal.class))).thenReturn(generateToken);

        String result = adminServices.verify(admin);

        assertEquals(generateToken, result);

    }

    @Test
    public void testVerify_FailedAuthentication() {
        Admin admin = new Admin();
        admin.setName("ram");
        admin.setPassword("wrong password");

        Authentication authentication = mock(Authentication.class);

        when(authentication.isAuthenticated()).thenReturn(false);
        when(authManager.authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        String result = adminServices.verify(admin);

        assertEquals("Invalid credentials", result);

        verify(authManager).authenticate(new UsernamePasswordAuthenticationToken("ram", "wrong password"));
        verify(service, never()).generateToken(ArgumentMatchers.any(UserPrincipal.class));


    }
}
