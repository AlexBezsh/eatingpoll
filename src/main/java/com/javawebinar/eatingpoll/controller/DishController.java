package com.javawebinar.eatingpoll.controller;

import com.javawebinar.eatingpoll.model.Dish;
import com.javawebinar.eatingpoll.service.DishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/admin/dish")
public class DishController {

    private final Logger logger = LoggerFactory.getLogger(DishController.class);

    private DishService dishService;

    @Autowired
    public void setDishService(DishService dishService) {
        this.dishService = dishService;
    }

    @RequestMapping(value = "/create")
    public ModelAndView createNewDish(@RequestParam String restaurantId) {
        logger.info("creating dish with restaurantId={}", restaurantId);
        return modelAndViewForDishForm(dishService.createNewDish(restaurantId));
    }

    @PostMapping(value = "/save")
    public String saveNewDish(@ModelAttribute("dish") Dish dish, @RequestParam String restaurantId) {
        logger.info("saving dish: {} in restaurant with id={}", dish, restaurantId);
        dishService.saveNewDish(dish, restaurantId);
        return "redirect:/admin/home";
    }

    @GetMapping(value = "/update")
    public ModelAndView getDishForUpdate(@RequestParam String dishId) {
        logger.info("updating dish with id={}", dishId);
        return modelAndViewForDishForm(dishService.getDishForUpdate(dishId));
    }

    @PostMapping(value = "/update")
    public String updateDish(@ModelAttribute Dish dish, @RequestParam String restaurantId) {
        dishService.updateDish(dish, restaurantId);
        return "redirect:/admin/home";
    }

    @RequestMapping(value = "/delete")
    public String deleteById(@RequestParam String dishId) {
        logger.info("deleting dish with id={}", dishId);
        dishService.deleteDishById(dishId);
        return "redirect:/admin/home";
    }

    private ModelAndView modelAndViewForDishForm(Dish dish) {
        ModelAndView mav = new ModelAndView(dish.getId() == null ? "newDishForm" : "updateDishForm");
        mav.addObject("dish", dish);
        mav.addObject("restaurantId", dish.getRestaurant().getId());
        return mav;
    }
}
