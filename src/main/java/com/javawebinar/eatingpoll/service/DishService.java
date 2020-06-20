package com.javawebinar.eatingpoll.service;

import com.javawebinar.eatingpoll.exceptions.BadRequestException;
import com.javawebinar.eatingpoll.exceptions.EntityNotFoundException;
import com.javawebinar.eatingpoll.model.Dish;
import com.javawebinar.eatingpoll.repository.DishRepository;
import com.javawebinar.eatingpoll.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.javawebinar.eatingpoll.util.AppUtil.checkEntity;
import static com.javawebinar.eatingpoll.util.AppUtil.parseId;

@Service
public class DishService {

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

    public Dish createNewDish(String restaurantId) {
        Dish dish = new Dish();
        long parsedRestaurantId = parseId(restaurantId);
        if (!restaurantRepository.existsById(parsedRestaurantId))
            throw new EntityNotFoundException("There is no restaurant with id=" + restaurantId + "in repository");
        dish.setRestaurantId(parsedRestaurantId);
        return dish;
    }

    public void save(Dish dish) {
        if (dish.getId() == null && dishRepository.existsByNameAndRestaurantId(dish.getName(), dish.getRestaurantId()))
            throw new BadRequestException("Dish with this name is already exists in this restaurant");
        dishRepository.saveAndFlush(checkEntity(dish, dish.getName(), dish.getPrice(), dish.getRestaurantId()));
    }

    public Dish getDishToUpdate(String dishId) {
        long parsedDishId = parseId(dishId);
        if (!dishRepository.existsById(parsedDishId)) throw new EntityNotFoundException("There is no dish with id=" + dishId + "in repository");
        return dishRepository.findById(parsedDishId).get();
    }

    public void deleteById(String dishId) {
        long parsedDishId = parseId(dishId);
        if (dishRepository.existsById(parsedDishId)) dishRepository.deleteById(parsedDishId);
        else throw new EntityNotFoundException("There is no dish with id=" + parsedDishId + " in repository");
    }
}
