package com.javawebinar.eatingpoll.service;

import com.javawebinar.eatingpoll.exceptions.BadRequestException;
import com.javawebinar.eatingpoll.model.Restaurant;
import com.javawebinar.eatingpoll.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.javawebinar.eatingpoll.util.AppUtil.*;

@Service
public class RestaurantService {

    private RestaurantRepository restaurantRepository;

    @Autowired
    public void setRestaurantRepository(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        restaurants.sort(Comparator.comparingInt(Restaurant::getVotesCount).reversed());
        return restaurants;
    }

    @Transactional
    public void saveNewRestaurant(Restaurant restaurant) {
        if (restaurantRepository.existsByName(restaurant.getName()))
            throw new BadRequestException("Restaurant with this name already exists");
        restaurantRepository.saveAndFlush(getCheckedRestaurant(restaurant));
    }

    @Transactional
    public void updateRestaurant(Restaurant restaurant) {
        String newName = restaurant.getName();
        checkName(newName);
        if (restaurantRepository.existsByName(newName))
            throw new BadRequestException("Restaurant with this name already exists");

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurant.getId());
        if (optionalRestaurant.isEmpty())
            throw new BadRequestException("There is no restaurant with id=" + restaurant.getId() + " in database");

        Restaurant restaurantFromDB = optionalRestaurant.get();
        restaurantFromDB.setName(newName);
        restaurantRepository.saveAndFlush(restaurantFromDB);
    }

    public void deleteById(String restaurantId) {
        restaurantRepository.deleteById(parseId(restaurantId));
    }
}
