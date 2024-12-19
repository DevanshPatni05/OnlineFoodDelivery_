    package com.FoodDelivery.Project.Services;


    import com.FoodDelivery.Project.Entity.*;
    import com.FoodDelivery.Project.Repository.*;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Lazy;
    import org.springframework.context.annotation.Primary;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.stereotype.Service;

    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;

    @Primary
    @Service
    public class CustomerServices implements UserDetailsService {

        @Autowired
        @Lazy
        AuthenticationManager authManager;

        @Autowired
        private CustomerRepo repo;

        @Autowired
        private JWTService service;


        @Autowired
        private RestaurantRepo restaurantRepo;

        @Autowired
        OrderRepo orderRepo;

        @Autowired
        OrderItemRepo orderItemRepo;

        @Autowired
        MenuRepo menuRepo;




        private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

        public Customer save(Customer customer) {
            customer.setRole("Customer");
            customer.setPassword(encoder.encode(customer.getPassword()));
            return repo.save(customer);


        }

        public List<Customer> allEntry() {
            return repo.findAll();


        }



        @Override
        public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
            Customer customer = repo.findByName(name);

            return UserPrincipal.create(customer);
        }

        public String verify(Customer customer) {
            System.out.println("Inside verify");
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(customer.getName(), customer.getPassword())
            );
            if (authentication.isAuthenticated()) {
                UserPrincipal userPrincipal = new UserPrincipal(customer);
                return service.generateToken(userPrincipal);
            } else {
                return "Invalid credentials";
            }
        }


        public Optional<Customer> update(long id,Customer customer)
        {
            Optional<Customer> existingCustomerOptional=repo.findById(id);
            if(existingCustomerOptional.isPresent())
            {
                Customer oldCustomer= existingCustomerOptional.get();
                oldCustomer.setName(customer.getName());
                oldCustomer.setPassword(encoder.encode(customer.getPassword()));
                oldCustomer.setMail(customer.getMail());
                Customer update=repo.save(oldCustomer);
                return Optional.of(update);

            }
            else
            {
               return Optional.empty();
            }
        }

        public Customer delete(String name)
        {
            Customer customer=repo.findByName(name);
            if(customer!=null)
            {
                repo.delete(customer);
                return customer;


            }
            return null;
        }

        public Order placeOrder(Order order,Long id)
        {
            Optional<Customer> customer=repo.findById(order.getCustomer().getCustomerId());
            if(customer.isPresent()) {

                Optional<Restaurant> restaurant = restaurantRepo.findById(id);
                if (restaurant.isPresent()) {
                    order.setCustomer(customer.get());
                    order.setRestaurant(restaurant.get());
                    order.setOrderDateTime(LocalDateTime.now());
                    List<OrderItem> orderItem = new ArrayList<>();

                    for (OrderItem orderItems : order.getOrderItems()) {
                        orderItemRepo.save(orderItems);
                        Optional<Menuu> menuItems = menuRepo.findById(orderItems.getMenu().getId());
                        if (menuItems.isPresent()) {

                            orderItems.setMenu(menuItems.get());
                            orderItems.setOrder(order);
                            orderItem.add(orderItems);

                        } else {
                            throw new RuntimeException("Invalid menu item");

                        }

                    }
                    order.setOrderItems(orderItem);
                    return orderRepo.save(order);


                } else {
                    throw new RuntimeException("CannotFind The restaurant");
                }
            }else
                {
                    throw new RuntimeException("invalid customer details");
                }

            }



    }

