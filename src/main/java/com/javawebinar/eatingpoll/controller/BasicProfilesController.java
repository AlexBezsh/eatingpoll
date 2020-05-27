package com.javawebinar.eatingpoll.controller;

import com.javawebinar.eatingpoll.exceptions.BadRequestException;
import com.javawebinar.eatingpoll.exceptions.EntityNotFoundException;
import com.javawebinar.eatingpoll.exceptions.TimeException;
import com.javawebinar.eatingpoll.model.Restaurant;
import com.javawebinar.eatingpoll.model.user.Role;
import com.javawebinar.eatingpoll.model.user.User;
import com.javawebinar.eatingpoll.repository.RestaurantRepository;
import com.javawebinar.eatingpoll.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import static com.javawebinar.eatingpoll.util.AppUtil.*;

@Controller
public class BasicProfilesController {

    private final Logger logger = LoggerFactory.getLogger(BasicProfilesController.class);

    private final LocalTime votingFinish = LocalTime.of(23, 59);
    private final String adminPassword = "password";

    protected UserRepository userRepository;
    protected RestaurantRepository restaurantRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRestaurantRepository(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping(value = "/")
    public ModelAndView start() {
        logger.info("start page is loading");
        List<User> mockUsers = List.of(userRepository.getById(1L), userRepository.getById(2L), userRepository.getById(3L));
        for (User user : mockUsers) user.setPassword(encode(user.getPassword()));

        ModelAndView mav = new ModelAndView("index");
        mav.addObject("users", mockUsers);
        mav.addObject("user", new User());
        return mav;
    }

    @RequestMapping(value = "/login")
    public ModelAndView login(@ModelAttribute("user") User user) {
        logger.info("user: {} is logging in", user);
        return modelAndViewForHomePage(user.getEmail(), user.getPassword());
    }

    @RequestMapping(value = "/create")
    public ModelAndView createUser(@ModelAttribute("user") User user) {
        logger.info("creating new user");
        Role role;
        String receivedPassword = user.getPassword();
        if (receivedPassword == null) role = Role.USER;
        else if (receivedPassword.equals(adminPassword)) role = Role.ADMIN;
        else throw new BadRequestException("Wrong admin password!");
        user.setRole(role);
        user.setPassword(null);
        ModelAndView mav = new ModelAndView("userForm");
        mav.addObject("user", user);
        return mav;
    }

    @PostMapping(value = "/save")
    public ModelAndView saveUser(@ModelAttribute("user") User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        if (user.getId() == null) saveNewUser(user);
        else saveUpdatedUser(user, email);
        return modelAndViewForHomePage(email, password);
    }

    protected void saveNewUser(User user) {
        logger.info("registering user: {}", user);
        String email = user.getEmail();
        User registered = userRepository.findOneByEmail(email);
        if (registered != null) throw new BadRequestException("User with this email is already registered");
        userRepository.save(checkEntity(user, user.getName(), email, user.getPassword(), user.getRole()));
    }

    protected void saveUpdatedUser(User user, String email) {
        logger.info("updating user with email={}", email);
        checkEntity(user, user.getName(), email, user.getPassword(), user.getRole());
        User userFromDB = userRepository.findOneByEmail(email);
        if (userFromDB == null)
            throw new EntityNotFoundException("There is no user with email=" + email + " in repository");
        userFromDB.setName(user.getName());
        userFromDB.setPassword(user.getPassword());
        userRepository.saveAndFlush(userFromDB);
    }

    protected ModelAndView updateUser(String email, String password) {
        User user = getUser(email, password);
        logger.info("updating user: {}", user);
        ModelAndView mav = new ModelAndView("userForm");
        mav.addObject("user", user);
        return mav;
    }

    protected ModelAndView modelAndViewForHomePage(String email, String password) {
        User user = getUser(email, password);
        user.setPassword(encode(password));
        logger.info("loading home page for user: {}", user);

        List<Restaurant> restaurants = restaurantRepository.findAll();
        restaurants.sort(Comparator.comparingInt(Restaurant::getVotesCount).reversed());

        ModelAndView mav = new ModelAndView(user.isAdmin() ? "adminPage" : "userPage");
        mav.addObject("restaurants", restaurants);
        if (user.isAdmin()) mav.addObject("restaurant", new Restaurant());
        mav.addObject("user", user);
        return mav;
    }

    protected void vote(String restaurantId, String email, String password) {
        if (LocalTime.now().isAfter(votingFinish)) throw new TimeException("You can't vote after 11 o'clock");
        User user = getUser(email, password);
        logger.info("user: {} choosing restaurant with id={}. Saving process takes three steps", user, restaurantId);

        long parsedRestaurantId = parseId(restaurantId);
        if (restaurantRepository.existsById(parsedRestaurantId)) {
            logger.info("step one: checking whether user: {} has already voted and decrementing number of votes in previous restaurant", user);
            if (user.hasVoted()) {
                Restaurant previousUserRestaurant = restaurantRepository.findById(user.getChosenRestaurantId()).get();
                previousUserRestaurant.minusVote();
                restaurantRepository.saveAndFlush(previousUserRestaurant);
            }
            logger.info("step two: incrementing number of votes in chosen restaurant");
            Restaurant restaurant = restaurantRepository.findById(parsedRestaurantId).get();
            restaurant.plusVote();
            restaurantRepository.saveAndFlush(restaurant);

            logger.info("step three: saving new \"chosenRestaurantId\" in user: {}", user);
            user.setChosenRestaurantId(restaurant.getId());
            userRepository.saveAndFlush(user);
        } else
            throw new EntityNotFoundException("There is no restaurant with id=" + parsedRestaurantId + " in repository");
    }

    protected void deleteUser(String userId) {
        logger.info("deleting user with id={} in two steps", userId);
        long parsedUserId = parseId(userId);
        User userFromDB;
        if (userRepository.existsById(parsedUserId)) userFromDB = userRepository.findById(parsedUserId).get();
        else throw new EntityNotFoundException("There is no user with id=" + parsedUserId + " in repository");

        logger.info("step one: checking \"chosenRestaurantId\" field in user and decrementing votes count in chosen restaurant");
        if (userFromDB.hasVoted() && restaurantRepository.existsById(userFromDB.getChosenRestaurantId())) {
            Restaurant restaurantFromDB = restaurantRepository.findById(userFromDB.getChosenRestaurantId()).get();
            restaurantFromDB.minusVote();
            restaurantRepository.saveAndFlush(restaurantFromDB);
        }
        logger.info("step two: deleting user from database");
        userRepository.deleteById(parsedUserId);
    }

    private User getUser(String email, String password) {
        User user = userRepository.findOneByEmailAndPassword(email, password);
        if (user == null) throw new BadRequestException("Wrong email or password");
        return user;
    }
}
