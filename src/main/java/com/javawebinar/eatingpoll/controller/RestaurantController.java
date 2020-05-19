package com.javawebinar.eatingpoll.controller;

import com.javawebinar.eatingpoll.exceptions.EntityNotFoundException;
import com.javawebinar.eatingpoll.model.Restaurant;
import com.javawebinar.eatingpoll.model.user.User;
import com.javawebinar.eatingpoll.repository.DishRepository;
import com.javawebinar.eatingpoll.repository.RestaurantRepository;
import com.javawebinar.eatingpoll.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;

import java.util.List;

import static com.javawebinar.eatingpoll.util.AppUtil.*;

@Controller
@RequestMapping(value = "/restaurant", produces = "text/plain;charset=UTF-8")
public class RestaurantController {

    private RestaurantRepository restaurantRepository;
    private DishRepository dishRepository;
    private UserRepository userRepository;

    @Autowired
    public void setRestaurantRepository(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Autowired
    public void setDishRepository(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/save")
    public String saveRestaurant(@ModelAttribute("restaurant") Restaurant restaurant, @RequestParam String userId) {
        restaurantRepository.saveAndFlush(checkEntity(restaurant, restaurant.getName()));
        return "redirect:/voting?userId=" + userId;
    }

    @PostMapping(value = "/update")
    public String updateRestaurant(@ModelAttribute("restaurant") Restaurant restaurant, @RequestParam String userId) {
        Restaurant restaurantFromDB = restaurantRepository.findById(restaurant.getId()).get();
        restaurantFromDB.setName(checkName(restaurant.getName()));
        restaurantRepository.saveAndFlush(restaurantFromDB);
        return "redirect:/voting?userId=" + userId;
    }

    @Transactional
    @RequestMapping(value = "/delete")
    public String deleteById(@RequestParam String restaurantId, @RequestParam String userId) {
        long parsedRestaurantId = parseId(restaurantId);
        if (restaurantRepository.existsById(parsedRestaurantId)) {
            Restaurant restaurant = restaurantRepository.findById(parsedRestaurantId).get();
            dishRepository.deleteAll(restaurant.getDishes());

            List<User> users = userRepository.findAllByChosenRestaurantId(parsedRestaurantId);
            for (User user: users) user.setChosenRestaurantId(null);
            userRepository.saveAll(users);

            restaurantRepository.deleteById(parsedRestaurantId);
        } else throw new EntityNotFoundException("There is no restaurant with id=" + parsedRestaurantId + " in repository");
        return "redirect:/voting?userId=" + userId;
    }
}

