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
@RequestMapping(value = "/admin/dish")
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
    public ModelAndView createNewDish(@RequestParam String restaurantId, @RequestParam("userEmail") String email, @RequestParam("userPassword") String encodedPassword) {
        logger.info("creating dish with restaurantId={}", restaurantId);
        Dish dish = new Dish();
        long parsedRestaurantId = parseId(restaurantId);
        if (!restaurantRepository.existsById(parsedRestaurantId))
            throw new EntityNotFoundException("There is no restaurant with id=" + restaurantId + "in repository");
        dish.setRestaurantId(parsedRestaurantId);
        return modelAndViewForDishForm(dish, email, encodedPassword);
    }

    @PostMapping(value = "/save")
    public String saveDish(@ModelAttribute("dish") Dish dish, @RequestParam("userEmail") String email, @RequestParam("userPassword") String encodedPassword) {
        logger.info("saving dish: {}", dish);
        dishRepository.saveAndFlush(checkEntity(dish, dish.getName(), dish.getPrice(), dish.getRestaurantId()));
        return "redirect:/admin/home?userEmail=" + email + "&userPassword=" + encodedPassword;
    }

    @RequestMapping(value = "/update")
    public ModelAndView updateDish(@RequestParam String dishId, @RequestParam("userEmail") String email, @RequestParam("userPassword") String encodedPassword) {
        logger.info("updating dish with id={}", dishId);
        long parsedDishId = parseId(dishId);
        if (!dishRepository.existsById(parsedDishId)) throw new EntityNotFoundException("There is no dish with id=" + dishId + "in repository");
        Dish dish = dishRepository.findById(parseId(dishId)).get();
        return modelAndViewForDishForm(dish, email, encodedPassword);
    }

    @RequestMapping(value = "/delete")
    public String deleteById(@RequestParam String dishId, @RequestParam("userEmail") String email, @RequestParam("userPassword") String encodedPassword) {
        logger.info("deleting dish with id={}", dishId);
        long parsedDishId = parseId(dishId);
        if (dishRepository.existsById(parsedDishId)) dishRepository.deleteById(parsedDishId);
        else throw new EntityNotFoundException("There is no dish with id=" + parsedDishId + " in repository");
        return "redirect:/admin/home?userEmail=" + email + "&userPassword=" + encodedPassword;
    }

    private ModelAndView modelAndViewForDishForm(Dish dish, String email, String encodedPassword) {
        ModelAndView mav = new ModelAndView("dishForm");
        mav.addObject("dish", dish);
        mav.addObject("userEmail", email);
        mav.addObject("userPassword", encodedPassword);
        return mav;
    }
}
