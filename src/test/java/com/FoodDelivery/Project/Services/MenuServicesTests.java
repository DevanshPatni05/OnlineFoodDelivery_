package com.FoodDelivery.Project.Services;

import com.FoodDelivery.Project.Entity.Menuu;
import com.FoodDelivery.Project.Entity.Restaurant;
import com.FoodDelivery.Project.Repository.MenuRepo;
import com.FoodDelivery.Project.Repository.RestaurantRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MenuServicesTests {

    @Mock
    MenuRepo menuRepo;

    @Mock
    RestaurantRepo  restaurantRepo1;

    @InjectMocks
    MenuServices menuServices;

    private final RestaurantRepo restaurantRepo=mock(RestaurantRepo.class);


    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testFindByRestaurant_Success() {
        // Given
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        restaurant.setName("Alav");

        List<Menuu> menuList =new ArrayList<>() ;
        Menuu menuu=new Menuu();
        menuu.setId(1L);
        menuu.setRestaurant(restaurant);
        menuu.setAvailability("Availaible");
        menuu.setDishName("Butter Paneer");
        menuu.setDescription("isgfvybzkmis");
        menuList.add(menuu);

        Menuu menuu1=new Menuu();
        menuu1.setId(2L);
        menuu1.setRestaurant(restaurant);
        menuu1.setAvailability("Availaible");
        menuu1.setDishName("Butter Paneer");
        menuu1.setDescription("isgfvybzkmis");
        menuList.add(menuu1);



        when(restaurantRepo.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(menuRepo.findByRestaurant_restaurantId(restaurantId)).thenReturn(menuList);
        List<Menuu> result = menuServices.findByRestaurant(restaurantId);

        assertNotNull(result);
        assertEquals(2,
                result.size());
        assertEquals("Butter Paneer", result.get(0).getDishName());
        assertEquals("Butter Paneer", result.get(1).getDishName());
        verify(restaurantRepo).findById(restaurantId);
        verify(menuRepo).findByRestaurant_restaurantId(restaurantId);
    }

    @Test
    void testFindByRestaurant_RestaurantNotFound() {

        Long restaurantId = 1L;


        when(restaurantRepo.findById(restaurantId)).thenReturn(Optional.empty());


        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            menuServices.findByRestaurant(restaurantId);
        });
        assertEquals("Cannot find restaurant", thrown.getMessage());
        verify(restaurantRepo).findById(restaurantId);
        verify(menuRepo, never()).findByRestaurant_restaurantId(restaurantId);
    }

    @Test
    void testAddMenuForRestaurant() {
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        restaurant.setName("Alav");

        Menuu menu = new Menuu();
        menu.setId(1L);
        menu.setDishName("ButterPaneer");

        when(restaurantRepo.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(menuRepo.save(menu)).thenReturn(menu);

        Menuu result = menuServices.addMenuForRestaurant(restaurantId, menu);

        assertNotNull(result);
        assertEquals("ButterPaneer", result.getDishName());
        assertEquals(restaurant, result.getRestaurant());
        verify(restaurantRepo, times(1)).findById(restaurantId);
        verify(menuRepo, times(1)).save(menu);
    }

    @Test
    void testDeleteItems_Success() {
        Long restaurantId = 1L;
        Long menuId = 100L;

        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        restaurant.setName("Alav");
        Menuu menu = new Menuu();
        menu.setId(1L);
        menu.setDishName("ButterPaneer");
        menu.setRestaurant(restaurant);



        when(restaurantRepo.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(menuRepo.findById(menuId)).thenReturn(Optional.of(menu));

        Menuu result = menuServices.deleteItems(menuId, restaurantId);


        assertNotNull(result);
        assertEquals(menu, result);
        assertEquals(restaurant, result.getRestaurant());
        verify(restaurantRepo, times(1)).findById(restaurantId);
        verify(menuRepo, times(1)).findById(menuId);
        verify(menuRepo, times(1)).deleteById(menuId);
    }

    @Test
    void testDeleteItems_RestaurantNotFound() {

        Long restaurantId = 1L;
        Long menuId = 100L;
        when(restaurantRepo.findById(restaurantId)).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            menuServices.deleteItems(menuId, restaurantId);
        });
        assertEquals("Cannot find the restaurant", thrown.getMessage());
        verify(restaurantRepo, times(1)).findById(restaurantId);
        verify(menuRepo, never()).findById(menuId);
        verify(menuRepo, never()).deleteById(anyLong());
    }

    @Test
    void testAllMenuItems_Success() {
        Menuu menu1 = new Menuu();
        menu1.setId(1L);
        menu1.setDishName("Butter Paneer");

        Menuu menu2 = new Menuu();
        menu2.setId(2L);
        menu2.setDishName("Butter Paneer");

        List<Menuu> menus = Arrays.asList(menu1, menu2);

        when(menuRepo.findAll()).thenReturn(menus);

        List<Menuu> result = menuServices.allMenuItems();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Butter Paneer", result.get(0).getDishName());
        assertEquals("Butter Paneer", result.get(1).getDishName());
        verify(menuRepo, times(1)).findAll();
    }

}

