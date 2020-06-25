package com.javawebinar.eatingpoll.service;

import com.javawebinar.eatingpoll.exceptions.BadRequestException;
import com.javawebinar.eatingpoll.exceptions.EntityNotFoundException;
import com.javawebinar.eatingpoll.model.Dish;
import com.javawebinar.eatingpoll.model.Restaurant;
import com.javawebinar.eatingpoll.repository.DishRepository;
import com.javawebinar.eatingpoll.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.javawebinar.eatingpoll.util.AppUtil.getCheckedDish;
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
        long parsedRestaurantId = parseId(restaurantId);
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(parsedRestaurantId);
        if (optionalRestaurant.isEmpty()) throw new EntityNotFoundException("There is no restaurant with id=" + restaurantId + "in repository");

        Dish dish = new Dish();
        dish.setRestaurant(optionalRestaurant.get());
        return dish;
    }

    @Transactional
    public void saveNewDish(Dish dish, String restaurantId) {
        Restaurant restaurant = getRestaurantForDish(restaurantId);
        if (dishRepository.existsByNameAndRestaurantId(dish.getName(), restaurant.getId()))
            throw new BadRequestException("Dish with this name is already exists in this restaurant");

        dish.setRestaurant(restaurant);
        dishRepository.save(getCheckedDish(dish));
    }

    public Dish getDishForUpdate(String dishId) {
        long parsedDishId = parseId(dishId);
        Optional<Dish> optionalDish = dishRepository.findById(parsedDishId);
        if (optionalDish.isEmpty()) throw new EntityNotFoundException("There is no dish with id=" + dishId + "in repository");
        return optionalDish.get();
    }

    @Transactional
    public void updateDish(Dish dish, String restaurantId) {
        Optional<Dish> optionalDish = dishRepository.findById(dish.getId());
        if (optionalDish.isEmpty()) throw new EntityNotFoundException("There is no dish with id=" + dish.getId() + " in repository");

        Dish dishFromDB = optionalDish.get();
        dishFromDB.setName(dish.getName());
        dishFromDB.setPrice(dish.getPrice());
        dishFromDB.setRestaurant(getRestaurantForDish(restaurantId));
        dishRepository.saveAndFlush(getCheckedDish(dishFromDB));
    }

    private Restaurant getRestaurantForDish(String restaurantId) {
        Long parsedRestaurantId = parseId(restaurantId);
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(parsedRestaurantId);
        if (optionalRestaurant.isEmpty()) throw new EntityNotFoundException("There is no restaurant with id=" + restaurantId + " in repository");
        return optionalRestaurant.get();
    }

    public void deleteDishById(String dishId) {
        long parsedDishId = parseId(dishId);
        if (dishRepository.existsById(parsedDishId)) dishRepository.deleteById(parsedDishId);
        else throw new EntityNotFoundException("There is no dish with id=" + parsedDishId + " in repository");
    }
}
