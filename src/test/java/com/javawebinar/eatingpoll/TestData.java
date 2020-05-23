package com.javawebinar.eatingpoll;

import com.javawebinar.eatingpoll.model.Dish;
import com.javawebinar.eatingpoll.model.Restaurant;
import com.javawebinar.eatingpoll.model.user.Role;
import com.javawebinar.eatingpoll.model.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestData {

    public static final User MOCK_USER1 = new User(1L, "User1", "user1@gmail.com", "user1", Role.USER, null);
    public static final User MOCK_USER2 = new User(2L, "User2", "user2@gmail.com", "user2", Role.USER, null);
    public static final User MOCK_USER3 = new User(3L, "Admin1", "admin1@gmail.com", "admin1", Role.ADMIN, null);

    public static final List<User> MOCK_USERS = List.of(MOCK_USER1, MOCK_USER2, MOCK_USER3);

    public static final Dish MOCK_DISH1 = new Dish(6L, "Fish", 23.4, 4L);
    public static final Dish MOCK_DISH2 = new Dish(7L, "Steak", 45.5, 4L);
    public static final Dish MOCK_DISH3 = new Dish(8L, "Salad", 12.4, 5L);
    public static final Dish MOCK_DISH4 = new Dish(9L, "Soup", 20.5, 5L);

    public static final Restaurant MOCK_RESTAURANT1 = new Restaurant(4L, "Some restaurant", Arrays.asList(MOCK_DISH1, MOCK_DISH2));
    public static final Restaurant MOCK_RESTAURANT2 = new Restaurant(5L, "Another restaurant", Arrays.asList(MOCK_DISH3, MOCK_DISH4));

    public static final User ADDITIONAL_MOCK_USER = new User(null, "Admin1", "admin1@gmail.com", "admin1", Role.USER, null);
    public static final Restaurant ADDITIONAL_MOCK_RESTAURANT = new Restaurant(null, "Additional restaurant", new ArrayList<>());
    public static final Dish ADDITIONAL_MOCK_DISH = new Dish(null, "Juice", 5.0, 5L);
}
