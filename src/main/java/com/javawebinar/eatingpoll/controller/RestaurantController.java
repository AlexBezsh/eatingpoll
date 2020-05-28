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
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

import java.util.List;

import static com.javawebinar.eatingpoll.util.AppUtil.*;

@Controller
@RequestMapping(value = "/admin/restaurant")
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
    public String saveNewRestaurant(@ModelAttribute("restaurant") Restaurant restaurant, @RequestParam("userEmail") String email, @RequestParam("userPassword") String encodedPassword) {
        logger.info("saving restaurant: {}", restaurant);
        restaurantRepository.saveAndFlush(checkEntity(restaurant, restaurant.getName()));
        return "redirect:/admin/home?userEmail=" + email + "&userPassword=" + encodedPassword;
    }

    @PostMapping(value = "/update")
    public String updateRestaurant(@ModelAttribute("restaurant") Restaurant restaurant, @RequestParam("userEmail") String email, @RequestParam("userPassword") String encodedPassword) {
        logger.info("updating restaurant with id={}", restaurant.getId());
        Restaurant restaurantFromDB = restaurantRepository.findById(restaurant.getId()).get();
        restaurantFromDB.setName(checkName(restaurant.getName()));
        restaurantRepository.saveAndFlush(restaurantFromDB);
        return "redirect:/admin/home?userEmail=" + email + "&userPassword=" + encodedPassword;
    }

    @Transactional
    @RequestMapping(value = "/delete")
    public String deleteById(@RequestParam String restaurantId, @RequestParam("userEmail") String email, @RequestParam("userPassword") String encodedPassword) {
        logger.info("deleting restaurant with id={} in three steps", restaurantId);

        long parsedRestaurantId = parseId(restaurantId);
        if (restaurantRepository.existsById(parsedRestaurantId)) {
            logger.info("step one: deleting all dishes of restaurant with id={}", restaurantId);
            Restaurant restaurant = restaurantRepository.findById(parsedRestaurantId).get();
            dishRepository.deleteAll(restaurant.getDishes());

            logger.info("step two: cleaning \"chosenRestaurantId\" variable in users, who have chosen restaurant with id={}", restaurantId);
            List<User> users = userRepository.findAllByChosenRestaurantId(parsedRestaurantId);
            for (User user : users) user.setChosenRestaurantId(null);
            userRepository.saveAll(users);

            logger.info("step three: deleting restaurant with id={} from database", restaurantId);
            restaurantRepository.deleteById(parsedRestaurantId);
        } else
            throw new EntityNotFoundException("There is no restaurant with id=" + parsedRestaurantId + " in repository");

        return "redirect:/admin/home?userEmail=" + email + "&userPassword=" + encodedPassword;
    }
}

