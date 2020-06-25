package com.javawebinar.eatingpoll.controller;

import com.javawebinar.eatingpoll.model.Restaurant;
import com.javawebinar.eatingpoll.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;

@Controller
@RequestMapping(value = "/admin/restaurant")
public class RestaurantController {

    private final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    private RestaurantService restaurantService;

    @Autowired
    public void setRestaurantService(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping(value = "/save")
    public String saveNewRestaurant(@ModelAttribute("restaurant") Restaurant restaurant) {
        logger.info("saving new restaurant: {}", restaurant);
        restaurantService.saveNewRestaurant(restaurant);
        return "redirect:/admin/home";
    }

    @PostMapping(value = "/update")
    public String updateRestaurant(@ModelAttribute("restaurant") Restaurant restaurant) {
        logger.info("updating restaurant with id={}", restaurant.getId());
        restaurantService.updateRestaurant(restaurant);
        return "redirect:/admin/home";
    }

    @Transactional
    @RequestMapping(value = "/delete")
    public String deleteById(@RequestParam String restaurantId) {
        logger.info("deleting restaurant with id={}", restaurantId);
        restaurantService.deleteById(restaurantId);
        return "redirect:/admin/home";
    }
}

