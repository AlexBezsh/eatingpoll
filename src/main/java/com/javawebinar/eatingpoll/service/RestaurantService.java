package com.javawebinar.eatingpoll.service;

import com.javawebinar.eatingpoll.exceptions.BadRequestException;
import com.javawebinar.eatingpoll.exceptions.EntityNotFoundException;
import com.javawebinar.eatingpoll.model.Restaurant;
import com.javawebinar.eatingpoll.model.user.User;
import com.javawebinar.eatingpoll.repository.DishRepository;
import com.javawebinar.eatingpoll.repository.RestaurantRepository;
import com.javawebinar.eatingpoll.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static com.javawebinar.eatingpoll.util.AppUtil.*;

@Service
public class RestaurantService {

    private final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

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

    public List<Restaurant> getAllRestaurants() {
        logger.debug("loading all restaurants from database");
        List<Restaurant> restaurants = restaurantRepository.findAll();
        restaurants.sort(Comparator.comparingInt(Restaurant::getVotesCount).reversed());
        return restaurants;
    }

    public void save(Restaurant restaurant) {
        logger.debug("saving new restaurant: {}", restaurant);
        if (restaurantRepository.existsByName(restaurant.getName())) throw new BadRequestException("Restaurant with this name is already exists");
        restaurantRepository.saveAndFlush(checkEntity(restaurant, restaurant.getName()));
    }

    public void update(Restaurant restaurant) {
        logger.debug("updating restaurant with id={}", restaurant.getId());
        if (restaurant.getId() == null || restaurant.getId() < 1) throw  new BadRequestException("Received restaurant has invalid id");
        Restaurant restaurantFromDB = restaurantRepository.findById(restaurant.getId()).get();
        restaurantFromDB.setName(checkName(restaurant.getName()));
        restaurantRepository.saveAndFlush(restaurantFromDB);
    }

    public void deleteById(String restaurantId) {
        logger.debug("deleting restaurant with id={} in three steps", restaurantId);
        long parsedRestaurantId = parseId(restaurantId);
        if (restaurantRepository.existsById(parsedRestaurantId)) {
            logger.debug("step one: deleting all dishes of restaurant with id={}", restaurantId);
            Restaurant restaurant = restaurantRepository.findById(parsedRestaurantId).get();
            dishRepository.deleteAll(restaurant.getDishes());

            logger.debug("step two: getting all users, who have chosen restaurant with id={} and setting nulls instead", restaurantId);
            List<User> users = userRepository.findAllByChosenRestaurantId(parsedRestaurantId);
            for (User user : users) user.setChosenRestaurantId(null);
            userRepository.saveAll(users);

            logger.debug("step three: deleting restaurant with id={} from database", restaurantId);
            restaurantRepository.deleteById(parsedRestaurantId);
        } else
            throw new EntityNotFoundException("There is no restaurant with id=" + parsedRestaurantId + " in repository");
    }
}
