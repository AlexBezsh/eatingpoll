package com.javawebinar.eatingpoll.controller;

import com.javawebinar.eatingpoll.model.Dish;
import com.javawebinar.eatingpoll.repository.DishRepository;
import com.javawebinar.eatingpoll.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.javawebinar.eatingpoll.exceptions.EntityNotFoundException;
import org.springframework.web.servlet.ModelAndView;

import static com.javawebinar.eatingpoll.util.AppUtil.checkEntity;
import static com.javawebinar.eatingpoll.util.AppUtil.parseId;

@Controller
@RequestMapping(value = "/dish")
public class DishController {

    private final Logger logger = LoggerFactory.getLogger(DishController.class);

    private DishRepository dishRepository;
    private RestaurantRepository restaurantRepository;

    @Autowired
    public void setDishRepository(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Autowired
    public void setRestaurantRepository(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @RequestMapping(value = "/create")
    public ModelAndView createDish(@RequestParam String restaurantId, @RequestParam String userId) {
        logger.info("creating dish with restaurantId={} by user with id={}", restaurantId, userId);
        Dish dish = new Dish();
        long parsedRestaurantId = parseId(restaurantId);
        if (!restaurantRepository.existsById(parsedRestaurantId)) throw new EntityNotFoundException("There is no restaurant with id=" + restaurantId + "in repository");
        dish.setRestaurantId(parsedRestaurantId);
        ModelAndView mav = new ModelAndView("dishForm");
        mav.addObject("dish", dish);
        mav.addObject("userId", userId);
        return mav;
    }

    @RequestMapping(value = "/update")
    public ModelAndView updateDish(@RequestParam String dishId, @RequestParam String userId) {
        logger.info("updating dish with id={} by user with id={}", dishId, userId);
        long parsedDishId = parseId(dishId);
        if (!dishRepository.existsById(parsedDishId)) throw new EntityNotFoundException("There is no dish with id=" + dishId + "in repository");
        Dish dish = dishRepository.findById(parseId(dishId)).get();
        ModelAndView mav = new ModelAndView("dishForm");
        mav.addObject("dish", dish);
        mav.addObject("userId", userId);
        return mav;
    }

    @PostMapping(value = "/save")
    public String saveDish(@ModelAttribute("dish") Dish dish, @RequestParam String userId) {
        logger.info("saving dish: {} by user with id={}", dish, userId);
        dishRepository.saveAndFlush(checkEntity(dish, dish.getName(), dish.getPrice(), dish.getRestaurantId()));
        return "redirect:/voting?userId=" + userId;
    }

    @RequestMapping(value ="/delete")
    public String deleteById(@RequestParam String dishId, @RequestParam String userId) {
        logger.info("deleting dish with id={} by user with id={}", dishId, userId);
        long parsedDishId = parseId(dishId);
        if (dishRepository.existsById(parsedDishId)) dishRepository.deleteById(parsedDishId);
        else throw new EntityNotFoundException("There is no dish with id=" + parsedDishId + " in repository");
        return "redirect:/voting?userId=" + userId;
    }
}
