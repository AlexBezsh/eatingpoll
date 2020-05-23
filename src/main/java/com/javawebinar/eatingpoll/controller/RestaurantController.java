package com.javawebinar.eatingpoll.controller;

import com.javawebinar.eatingpoll.exceptions.EntityNotFoundException;
import com.javawebinar.eatingpoll.model.Restaurant;
import com.javawebinar.eatingpoll.model.user.User;
import com.javawebinar.eatingpoll.repository.DishRepository;
import com.javawebinar.eatingpoll.repository.RestaurantRepository;
import com.javawebinar.eatingpoll.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

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
        logger.info("saving restaurant: {} by user with id={}", restaurant, userId);
        restaurantRepository.saveAndFlush(checkEntity(restaurant, restaurant.getName()));
        return "redirect:/voting?userId=" + userId;
    }

    @PostMapping(value = "/update")
    public String updateRestaurant(@ModelAttribute("restaurant") Restaurant restaurant, @RequestParam String userId) {
        logger.info("updating restaurant with id={} by user with id={}", restaurant.getId(), userId);
        Restaurant restaurantFromDB = restaurantRepository.findById(restaurant.getId()).get();
        restaurantFromDB.setName(checkName(restaurant.getName()));
        restaurantRepository.saveAndFlush(restaurantFromDB);
        return "redirect:/voting?userId=" + userId;
    }

    @Transactional
    @RequestMapping(value = "/delete")
    public String deleteById(@RequestParam String restaurantId, @RequestParam String userId) {
        logger.info("deleting restaurant with id={} by user with id={} in three steps", restaurantId, userId);
        long parsedRestaurantId = parseId(restaurantId);
        if (restaurantRepository.existsById(parsedRestaurantId)) {
            logger.info("step one: deleting all dishes of restaurant with id={}", restaurantId);
            Restaurant restaurant = restaurantRepository.findById(parsedRestaurantId).get();
            dishRepository.deleteAll(restaurant.getDishes());

            logger.info("step two: cleaning chosenRestaurantId variables in users, who have chosen restaurant with id={}", restaurantId);
            List<User> users = userRepository.findAllByChosenRestaurantId(parsedRestaurantId);
            for (User user: users) user.setChosenRestaurantId(null);
            userRepository.saveAll(users);

            logger.info("step three: deleting restaurant with id={} from database", restaurantId);
            restaurantRepository.deleteById(parsedRestaurantId);
        } else throw new EntityNotFoundException("There is no restaurant with id=" + parsedRestaurantId + " in repository");
        return "redirect:/voting?userId=" + userId;
    }
}

