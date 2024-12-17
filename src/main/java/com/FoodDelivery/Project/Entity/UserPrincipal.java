package com.FoodDelivery.Project.Entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {

    private String username;
    private String password;
    private String role;

    private Customer customer;
    private Admin admin;

    public UserPrincipal(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Constructor to create UserPrincipal from a Customer entity
    public UserPrincipal(Customer customer) {
        this.customer = customer;
        this.username = customer.getName();
        this.password = customer.getPassword();
        this.role = customer.getRole();
    }

    public UserPrincipal(Admin admin) {
        this.admin=admin;
        this.username = admin.getName();
        this.password = admin.getPassword();
        this.role = admin.getRole();
    }


    public static UserPrincipal create(Customer customer) {
        return new UserPrincipal(customer);
    }

    public static UserPrincipal create(Admin admin)
    {
        return new UserPrincipal(admin);
    }

    // Authorities (Roles)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (admin == null && role != null) {
            return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + customer.getRole()));
        } else if (customer == null && role != null) {
            return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + admin.getRole()));
        } else {
            return Collections.emptyList();
        }
    }

    // Get Password
    @Override
    public String getPassword() {
        if (customer != null) {
            return customer.getPassword();
        }
        if (admin != null) {
            return admin.getPassword();
        }
        return null;
    }

    @Override
    public String getUsername() {
        if (customer != null) {
            return customer.getName();
        }
        if (admin != null) {
            return admin.getName();
        }
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
