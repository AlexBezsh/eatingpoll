package com.javawebinar.eatingpoll.util;

import com.javawebinar.eatingpoll.exceptions.BadRequestException;
import com.javawebinar.eatingpoll.model.Dish;
import com.javawebinar.eatingpoll.model.Restaurant;
import com.javawebinar.eatingpoll.model.user.Role;
import com.javawebinar.eatingpoll.model.user.User;

import java.util.ArrayList;

public class AppUtil {

    public static Long parseId(String id) {
        if (id == null) throw new BadRequestException("Received id is null");
        long num;
        try {
            num = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Received id (" + id + ") is not a number");
        }
        if (num < 1) throw new BadRequestException("Received id is less than one");
        return num;
    }

    public static Dish checkEntity(Dish dish, String name, Double price, Long restaurantId) {
        checkName(name);
        if (price == null || price < 0) throw new BadRequestException("Price must be a positive number or 0");
        dish.setPrice(Double.parseDouble(String.format("%.2f", price).replace(',', '.')));
        if (restaurantId == null || restaurantId < 0) throw new BadRequestException("Dish has wrong restaurantId");
        return dish;
    }

    public static Restaurant checkEntity(Restaurant restaurant, String name) {
        checkName(name);
        if (restaurant.getVotesCount() == null) restaurant.setVotesCount(0);
        if (restaurant.getDishes() == null) restaurant.setDishes(new ArrayList<>());
        return restaurant;
    }

    public static User checkEntity(User user, String name, String email, String password, Role role) {
        checkName(name);
        if (password == null || password.isBlank() || password.length() < 5 || password.length() > 20) throw new BadRequestException("Password must be between 5 and 20 characters");
        if (!password.matches("\\w+")) throw new BadRequestException("Only numbers and latin letters can be used in the password");
        if (email == null || !email.matches(".+@.+\\..+")) throw new BadRequestException("Wrong email");
        if (role == null) user.setRole(Role.USER);
        return user;
    }

    public static String checkName(String name) {
        if (name == null || name.isBlank()) throw new BadRequestException("Empty name");
        if (name.length() < 2 || name.length() > 60) throw new BadRequestException("Name must be between 2 and 60 characters");
        return name;
    }
}
