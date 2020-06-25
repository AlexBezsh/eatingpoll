package com.javawebinar.eatingpoll;

import com.javawebinar.eatingpoll.exceptions.BadRequestException;
import com.javawebinar.eatingpoll.model.Dish;
import com.javawebinar.eatingpoll.model.Restaurant;
import com.javawebinar.eatingpoll.model.user.Role;
import com.javawebinar.eatingpoll.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.javawebinar.eatingpoll.TestData.MOCK_RESTAURANT1;
import static com.javawebinar.eatingpoll.TestData.MOCK_RESTAURANT2;
import static com.javawebinar.eatingpoll.util.AppUtil.*;

public class AppUtilTest {

    @Test
    public void testParseId() {
        Assertions.assertThrows(BadRequestException.class, () -> parseId(null));
        Assertions.assertThrows(BadRequestException.class, () -> parseId("-1"));
        Assertions.assertThrows(BadRequestException.class, () -> parseId("blah"));
        Assertions.assertEquals(3, parseId("3"));
    }

    @Test
    public void testNameCheck() {
        Assertions.assertThrows(BadRequestException.class, () -> checkName(null));
        Assertions.assertThrows(BadRequestException.class, () -> checkName(""));
        Assertions.assertThrows(BadRequestException.class, () -> checkName("f"));
    }

    @Test
    public void testDishCheck(){
        Dish dish1 = new Dish(1L, "dish", 4.4, MOCK_RESTAURANT1);
        Assertions.assertDoesNotThrow(() -> getCheckedDish(dish1));

        Dish dish2 = new Dish(1L, "dish", -1.0, null);
        Assertions.assertThrows(BadRequestException.class, () -> getCheckedDish(dish2));

        Dish dish3 = new Dish(1L, "dish", null, MOCK_RESTAURANT1);
        Assertions.assertThrows(BadRequestException.class, () -> getCheckedDish(dish3));

        Dish dish4 = new Dish(1L, "dish", 2.4545, MOCK_RESTAURANT2);
        Assertions.assertEquals(2.45, getCheckedDish(dish4).getPrice());
    }

    @Test
    public void testRestaurantCheck(){
        Restaurant restaurant1 = new Restaurant(1L, "restaurant", null);
        Assertions.assertNotNull(getCheckedRestaurant(restaurant1).getDishes());

        Restaurant restaurant2 = new Restaurant(1L, "r", new ArrayList<>());
        Assertions.assertThrows(BadRequestException.class, () -> getCheckedRestaurant(restaurant2));

        Restaurant restaurant3 = new Restaurant(1L, "restaurant", new ArrayList<>());
        Assertions.assertEquals(restaurant3, getCheckedRestaurant(restaurant3));
    }

    @Test
    public void testUserCheck(){
        User user1 = new User(1L, "user1", "email@gmail.com", "password", null, null);
        Assertions.assertNotNull(getCheckedUser(user1).getRole());

        User user2 = new User(1L, "user1", "email@gmailcom", "password", null, null);
        Assertions.assertThrows(BadRequestException.class, () -> getCheckedUser(user2)); //wrong email

        User user3 = new User(1L, "user1", "email@gmail.com", "", null, null);
        Assertions.assertThrows(BadRequestException.class, () -> getCheckedUser(user3)); // wrong password

        User user5 = new User(1L, "user1", "email@gmail.com", "password", Role.ADMIN, null);
        Assertions.assertEquals(user5, getCheckedUser(user5));
    }
}
