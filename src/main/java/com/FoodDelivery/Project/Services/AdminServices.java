package com.FoodDelivery.Project.Services;

import com.FoodDelivery.Project.Entity.Admin;
import com.FoodDelivery.Project.Entity.Restaurant;
import com.FoodDelivery.Project.Entity.UserPrincipal;
import com.FoodDelivery.Project.Repository.AdminRepo;
import com.FoodDelivery.Project.Repository.RestaurantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("adminServices")
public class AdminServices implements UserDetailsService {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    @Lazy
    AuthenticationManager authManager;

    @Autowired
    private JWTService service;

    @Autowired
    RestaurantRepo restaurantRepo;




    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);
    public Admin insert(Admin admin)
    {
        admin.setRole("Admin");
        admin.setPassword(encoder.encode(admin.getPassword()));
       return adminRepo.save(admin);
    }
    public String verify(Admin admin) throws Exception {


        System.out.println(admin.getName()+admin.getPassword());
        String role=admin.getRole();
        Authentication authentication=authManager.authenticate(
                new UsernamePasswordAuthenticationToken(admin.getName(), admin.getPassword()));

        if (authentication.isAuthenticated()) {
            System.out.println("Inside if");
            UserPrincipal userPrincipal=new UserPrincipal(admin);
            return service.generateToken(userPrincipal);
        }
        else {

            System.out.println("Inside else");
            return "Invalid credentials";

        }

    }

    public List<Admin> allEntry()
    {
        return adminRepo.findAll();

    }


    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Admin admin=adminRepo.findByName(name);
        if(admin!=null)
        {
            return  UserPrincipal.create(admin);
        }
        throw new UsernameNotFoundException("Admin not found");
    }

    public Restaurant insertRestaurant(Restaurant restaurant)
    {
        return restaurantRepo.save(restaurant);
    }
}
