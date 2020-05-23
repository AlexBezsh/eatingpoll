package com.javawebinar.eatingpoll;

import com.javawebinar.eatingpoll.exceptions.BadRequestException;
import com.javawebinar.eatingpoll.model.Dish;
import com.javawebinar.eatingpoll.model.Restaurant;
import com.javawebinar.eatingpoll.model.user.Role;
import com.javawebinar.eatingpoll.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.javawebinar.eatingpoll.util.AppUtil.checkEntity;
import static com.javawebinar.eatingpoll.util.AppUtil.checkName;
import static com.javawebinar.eatingpoll.util.AppUtil.parseId;

public class AppUtilTest {

    @Test
    public void testParseId() {
        String id1 = null;
        String id2 = "-1";
        String id3 = "bla";
        String id4 = "3";
        Assertions.assertThrows(BadRequestException.class, () -> parseId(id1));
        Assertions.assertThrows(BadRequestException.class, () -> parseId(id2));
        Assertions.assertThrows(BadRequestException.class, () -> parseId(id3));
        Assertions.assertEquals(3, parseId(id4));
    }

    @Test
    public void testNameCheck() {
        String name1 = null;
        String name2 = "";
        String name3 = "f";
        String name4 = "name";
        Assertions.assertThrows(BadRequestException.class, () -> checkName(name1));
        Assertions.assertThrows(BadRequestException.class, () -> checkName(name2));
        Assertions.assertThrows(BadRequestException.class, () -> checkName(name3));
        Assertions.assertEquals("name", checkName(name4));
    }

    @Test
    public void testDishCheck(){
        Dish dish1 = new Dish(1L, "dish", 4.4, null);
        Dish dish2 = new Dish(1L, "dish", -1.0, 1L);
        Dish dish3 = new Dish(1L, "dish", null, 1L);
        Dish dish4 = new Dish(1L, "dish", 2.4545, 1L);
        Assertions.assertThrows(BadRequestException.class, () -> checkEntity(dish1, dish1.getName(), dish1.getPrice(), dish1.getRestaurantId()));
        Assertions.assertThrows(BadRequestException.class, () -> checkEntity(dish2, dish2.getName(), dish2.getPrice(), dish2.getRestaurantId()));
        Assertions.assertThrows(BadRequestException.class, () -> checkEntity(dish3, dish3.getName(), dish3.getPrice(), dish3.getRestaurantId()));
        Dish dish5 = checkEntity(dish4, dish4.getName(), dish4.getPrice(), dish4.getRestaurantId());
        Assertions.assertEquals(2.45, dish5.getPrice());
    }

    @Test
    public void testRestaurantCheck(){
        Restaurant restaurant1 = new Restaurant(1L, "restaurant", new ArrayList<>());
        Restaurant restaurant2 = new Restaurant(1L, "restaurant", null);
        Assertions.assertEquals(restaurant1, checkEntity(restaurant1, restaurant1.getName()));
        Assertions.assertNotNull(checkEntity(restaurant2, restaurant2.getName()).getDishes());
    }

    @Test
    public void testUserCheck(){
        User user1 = new User(1L, "user1", "email@gmail.com", "password", null, null);
        User user2 = new User(1L, "user1", "email@gmailcom", "password", null, null);
        User user3 = new User(1L, "user1", "email@gmail.com", "", null, null);
        User user4 = new User(1L, "user1", null, "password", null, null);
        User user5 = new User(1L, "user1", "email@gmail.com", "password", Role.ADMIN, null);
        Assertions.assertNotNull(checkEntity(user1, user1.getName(), user1.getEmail(), user1.getPassword(), user1.getRole()).getRole());
        Assertions.assertThrows(BadRequestException.class, () -> checkEntity(user2, user2.getName(), user2.getEmail(), user2.getPassword(), user2.getRole()));
        Assertions.assertThrows(BadRequestException.class, () -> checkEntity(user3, user3.getName(), user3.getEmail(), user3.getPassword(), user3.getRole()));
        Assertions.assertThrows(BadRequestException.class, () -> checkEntity(user4, user4.getName(), user4.getEmail(), user4.getPassword(), user4.getRole()));
        Assertions.assertEquals(user5, checkEntity(user5, user5.getName(), user5.getEmail(), user5.getPassword(), user5.getRole()));
    }
}
