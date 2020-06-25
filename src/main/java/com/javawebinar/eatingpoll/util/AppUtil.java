package com.javawebinar.eatingpoll.util;

import com.javawebinar.eatingpoll.exceptions.BadRequestException;
import com.javawebinar.eatingpoll.model.Dish;
import com.javawebinar.eatingpoll.model.Restaurant;
import com.javawebinar.eatingpoll.model.user.Role;
import com.javawebinar.eatingpoll.model.user.User;

import java.util.ArrayList;

public class AppUtil {

    public static Long parseId(String id) {
        if (id == null) throw new BadRequestException("Id is null");
        long num;
        try {
            num = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id (" + id + ") is not a number");
        }
        if (num < 1) throw new BadRequestException("Id is less than one");
        return num;
    }

    public static Dish getCheckedDish(Dish dish) {
        if (dish == null) throw new BadRequestException("Dish is null");
        checkName(dish.getName());
        dish.setPrice(checkAndTrimPrice(dish.getPrice()));
        if (dish.getRestaurant() == null) throw new BadRequestException("Dish has no restaurant");
        return dish;
    }

    public static Restaurant getCheckedRestaurant(Restaurant restaurant) {
        if (restaurant == null) throw new BadRequestException("Restaurant is null");
        checkName(restaurant.getName());
        if (restaurant.getVotesCount() == null) restaurant.setVotesCount(0);
        if (restaurant.getDishes() == null) restaurant.setDishes(new ArrayList<>());
        return restaurant;
    }

    public static User getCheckedUser(User user) {
        if (user == null) throw new BadRequestException("User is null");
        checkName(user.getName());
        checkNewPassword(user.getPassword());
        checkEmail(user.getEmail());
        if (user.getRole() == null) user.setRole(Role.USER);
        return user;
    }

    public static void checkName(String name) {
        if (name == null || name.isBlank()) throw new BadRequestException("Empty name");
        if (name.length() < 2 || name.length() > 60) throw new BadRequestException("Name must be between 2 and 60 characters");
    }

    public static void checkNewPassword(String password) {
        if (password == null || password.isBlank() || password.length() < 5 || password.length() > 20) {
            throw new BadRequestException("Password must be between 5 and 20 characters");
        }
        if (!password.matches("\\w+")) throw new BadRequestException("Only numbers and latin letters can be used in the password");
    }

    public static void checkEmail(String email) {
        if (email == null || !email.matches(".+@.+\\..+")) throw new BadRequestException("Wrong email");
    }

    public static Double checkAndTrimPrice(Double price) {
        if (price == null || price < 0) throw new BadRequestException("Price must be a positive number or 0");
        return Double.parseDouble(String.format("%.2f", price).replace(',', '.'));
    }
}
