package com.javawebinar.eatingpoll;

import com.javawebinar.eatingpoll.model.Dish;
import com.javawebinar.eatingpoll.model.Restaurant;
import com.javawebinar.eatingpoll.model.user.Role;
import com.javawebinar.eatingpoll.model.user.User;
import com.javawebinar.eatingpoll.transfer.UserDto;

import java.util.Arrays;

public class TestData {

    public static final User MOCK_USER1 = new User(1L, "User1", "user1@gmail.com", "user1", Role.USER, null);
    public static final User MOCK_USER2 = new User(2L, "User2", "user2@gmail.com", "user2", Role.USER, null);
    public static final User MOCK_ADMIN1 = new User(3L, "Admin1", "admin1@gmail.com", "admin1", Role.ADMIN, null);

    public static final UserDto MOCK_USER1_DTO = new UserDto(MOCK_USER1);
    public static final UserDto MOCK_USER2_DTO = new UserDto(MOCK_USER2);
    public static final UserDto MOCK_ADMIN1_DTO = new UserDto(MOCK_ADMIN1);

    public static final Restaurant MOCK_RESTAURANT1 = new Restaurant();
    public static final Restaurant MOCK_RESTAURANT2 = new Restaurant();

    public static final Dish MOCK_DISH1 = new Dish(6L, "Fish", 23.4, MOCK_RESTAURANT1);
    public static final Dish MOCK_DISH2 = new Dish(7L, "Steak", 45.5, MOCK_RESTAURANT1);
    public static final Dish MOCK_DISH3 = new Dish(8L, "Salad", 12.4, MOCK_RESTAURANT2);
    public static final Dish MOCK_DISH4 = new Dish(9L, "Soup", 20.5, MOCK_RESTAURANT2);

    static {
        MOCK_RESTAURANT1.setId(4L);
        MOCK_RESTAURANT1.setName("Some restaurant");
        MOCK_RESTAURANT1.setDishes(Arrays.asList(MOCK_DISH1, MOCK_DISH2));

        MOCK_RESTAURANT2.setId(5L);
        MOCK_RESTAURANT2.setName("Another restaurant");
        MOCK_RESTAURANT2.setDishes(Arrays.asList(MOCK_DISH3, MOCK_DISH4));
    }

    public static final Restaurant ADDITIONAL_MOCK_RESTAURANT = new Restaurant(null, "Additional restaurant", null);
    public static final Dish ADDITIONAL_MOCK_DISH = new Dish(null, "Juice", 5.0, MOCK_RESTAURANT2);
}
