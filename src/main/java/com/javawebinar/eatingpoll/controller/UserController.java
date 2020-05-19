package com.javawebinar.eatingpoll.controller;

import com.javawebinar.eatingpoll.exceptions.BadRequestException;
import com.javawebinar.eatingpoll.exceptions.EntityNotFoundException;
import com.javawebinar.eatingpoll.exceptions.TimeException;
import com.javawebinar.eatingpoll.model.Restaurant;
import com.javawebinar.eatingpoll.model.user.Role;
import com.javawebinar.eatingpoll.model.user.User;
import com.javawebinar.eatingpoll.repository.RestaurantRepository;
import com.javawebinar.eatingpoll.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;

import static com.javawebinar.eatingpoll.util.AppUtil.checkEntity;
import static com.javawebinar.eatingpoll.util.AppUtil.parseId;

import java.time.LocalTime;
import java.util.List;

@Controller
public class UserController {

    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;
    private final LocalTime votingFinish = LocalTime.of(11, 0);
    private final String adminPassword = "password";

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
        List<User> mockUsers = List.of(
                new User(1L, "User1", "user1@hmail.com", "user1", Role.USER, null),
                new User(2L, "User2", "user2@gmail.com", "user2", Role.USER, null),
                new User(3L, "Admin1", "admin1@gmail.com", "admin1", Role.ADMIN, null));
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("users", mockUsers);
        mav.addObject("user", new User());
        mav.addObject("adminPassword", "");
        return mav;
    }

    @RequestMapping(value = "/user/create")
    public ModelAndView createUser(@ModelAttribute("user") User user) {
        Role role;
        String receivedPassword = user.getPassword();
        if (receivedPassword == null) role = Role.USER;
        else if (receivedPassword.equals(adminPassword)) role = Role.ADMIN;
        else throw new BadRequestException("Wrong admin password!");
        user.setRole(role);
        ModelAndView mav = new ModelAndView("userForm");
        mav.addObject("user", user);
        return mav;
    }

    @PostMapping(value = "/user/save")
    public ModelAndView saveUser(@ModelAttribute("user") User user) {
        User registered = userRepository.findOneByEmail(user.getEmail());
        if (registered != null) throw new BadRequestException("User with this email is already registered");
        userRepository.save(checkEntity(user, user.getName(), user.getEmail(), user.getPassword(), user.getRole()));
        return modelAndViewForVoting(user);
    }

    @RequestMapping(value = "/user/login")
    public ModelAndView login(@ModelAttribute("user") User user) {
        User registered = userRepository.findOneByEmailAndPassword(user.getEmail(), user.getPassword());
        if (registered == null) throw new BadRequestException("Wrong email or password!");
        return modelAndViewForVoting(registered);
    }

    //Main page
    @RequestMapping(value = "/voting")
    public ModelAndView votingPage(@RequestParam String userId) {
        long parsedUserId = parseId(userId);
        if (!userRepository.existsById(parsedUserId)) throw new EntityNotFoundException("There is no user with id=" + userId + "in repository");
        return modelAndViewForVoting(userRepository.findById(parsedUserId).get());
    }

    private ModelAndView modelAndViewForVoting(User user) {
        ModelAndView mav = new ModelAndView(user.isAdmin() ? "adminPage" : "userPage");
        mav.addObject("restaurants", restaurantRepository.findAll());
        if (user.isAdmin()) mav.addObject("restaurant", new Restaurant());
        mav.addObject("user", user);
        return mav;
    }

    @Transactional
    @RequestMapping(value = "/vote")
    public String vote(@RequestParam String restaurantId, @RequestParam String userId) {
        if (LocalTime.now().isAfter(votingFinish)) throw new TimeException("You can't vote after 11 o'clock");

        long parsedRestaurantId = parseId(restaurantId);
        long parsedUserId = parseId(userId);

        if (userRepository.existsById(parsedUserId) && restaurantRepository.existsById(parsedRestaurantId)) {
            User user = userRepository.findById(parsedUserId).get();
            if (user.hasVoted()) {
                Restaurant previousUserRestaurant = restaurantRepository.findById(user.getChosenRestaurantId()).get();
                previousUserRestaurant.minusVote();
                restaurantRepository.saveAndFlush(previousUserRestaurant);
            }
            Restaurant restaurant = restaurantRepository.findById(parsedRestaurantId).get();
            restaurant.plusVote();
            restaurantRepository.saveAndFlush(restaurant);

            user.setChosenRestaurantId(restaurant.getId());
            userRepository.saveAndFlush(user);
        }
        return "redirect:/voting?userId=" + userId;
    }

    @Transactional
    @RequestMapping(value = "/discard")
    public String discardResult(@RequestParam String userId) {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        for (Restaurant restaurant : restaurants) restaurant.setVotesCount(0);
        restaurantRepository.saveAll(restaurants);

        List<User> users = userRepository.findAll();
        for (User user : users) user.setChosenRestaurantId(null);
        userRepository.saveAll(users);

        return "redirect:/voting?userId=" + userId;
    }
}
