package com.FoodDelivery.Project.Repository;

import com.FoodDelivery.Project.Entity.Customer;  // Import the correct Customer entity
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {
    Customer findByName(String name);

}

