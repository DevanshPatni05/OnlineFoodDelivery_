package com.FoodDelivery.Project.Controller;


import com.FoodDelivery.Project.Entity.Customer;
import com.FoodDelivery.Project.Entity.Menuu;
import com.FoodDelivery.Project.Entity.Order;
import com.FoodDelivery.Project.Services.CustomerServices;
import com.FoodDelivery.Project.Services.MenuServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Customer")
public class CustomerController {

    @Autowired
    private CustomerServices customerServices;

    @Autowired
    private MenuServices menuServices;



    @GetMapping("/healthCheck")
    public String healthCheck()
    {
        return "OK";
    }

    @PostMapping("/insert")
    public ResponseEntity<Customer> insert(@RequestBody Customer customer)
    {
      
        customerServices.save(customer);

        return new ResponseEntity<>(customer, HttpStatus.OK);

    }

    @GetMapping("/allEntry")
    public List<Customer> allEntry()
    {
        return customerServices.allEntry();
    }

    @PostMapping ("/login")
    public String login(@RequestBody Customer customer) {
        return customerServices.verify(customer);
    }





    
    @PutMapping("/update/{id}")
    public ResponseEntity<Customer> update(@PathVariable long id,@RequestBody Customer customer)
    {
        Optional<Customer> update= customerServices.update(id,customer);
        if(update.isPresent()) {

            return new ResponseEntity<>(update.get(), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Customer> delete(@PathVariable String name)
    {
       Customer deleteCustomer= customerServices.delete(name);
        if(deleteCustomer!=null)
        {
            return new ResponseEntity<>(deleteCustomer,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/MenuItems")
    public List<Menuu> allMenuItems()
    {
        return menuServices.allMenuItems();
    }

    @PostMapping("/placeOrder")
    public Order placeOrder(@RequestBody Order order)
    {
        return customerServices.placeOrder(order);
    }





}
