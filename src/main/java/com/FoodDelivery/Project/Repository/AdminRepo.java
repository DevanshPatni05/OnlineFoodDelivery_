package com.FoodDelivery.Project.Repository;

import com.FoodDelivery.Project.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepo extends JpaRepository<Admin,Long> {
    Admin findByName(String name);
}
