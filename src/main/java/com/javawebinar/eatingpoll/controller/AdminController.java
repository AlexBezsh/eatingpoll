package com.javawebinar.eatingpoll.controller;

import com.javawebinar.eatingpoll.model.Restaurant;
import com.javawebinar.eatingpoll.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.util.List;

import static com.javawebinar.eatingpoll.util.AppUtil.decode;

@Controller
@RequestMapping(value = "/admin")
public class AdminController extends BasicProfilesController {

    private final Logger logger = LoggerFactory.getLogger(BasicProfilesController.class);

    @RequestMapping(value = "/home")
    public ModelAndView home(@RequestParam("userEmail") String email, @RequestParam("userPassword") String encodedPassword) {
        return modelAndViewForHomePage(email, decode(encodedPassword));
    }

    @RequestMapping(value = "/update")
    public ModelAndView updateUser(@RequestParam("userEmail") String email, @RequestParam("userPassword") String encodedPassword) {
        return super.updateUser(email, decode(encodedPassword));
    }

    @Transactional
    @RequestMapping(value = "/delete")
    public String deleteProfile(@RequestParam String userId) {
        deleteUser(userId);
        return "redirect:/";
    }

    @RequestMapping(value = "/users")
    public ModelAndView users(@RequestParam("userEmail") String email, @RequestParam("userPassword") String encodedPassword) {
        logger.info("loading all users by admin with email={}", email);
        List<User> users = userRepository.findAll();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(email)) {
                users.remove(i);
                i--;
                continue;
            }
            users.get(i).setPassword(null);
        }
        ModelAndView mav = new ModelAndView("users");
        mav.addObject("users", users);
        mav.addObject("userEmail", email);
        mav.addObject("userPassword", encodedPassword);
        return mav;
    }

    @Transactional
    @RequestMapping(value = "/users/delete")
    public String deleteUserByAdmin(@RequestParam("userId") String userId, @RequestParam("userEmail") String email, @RequestParam("userPassword") String encodedPassword) {
        deleteUser(userId);
        return "redirect:/admin/users?userEmail=" + email + "&userPassword=" + encodedPassword;
    }

    @Transactional
    @RequestMapping(value = "/vote")
    public String adminVote(@RequestParam String restaurantId, @RequestParam("userEmail") String email, @RequestParam("userPassword") String encodedPassword) {
        vote(restaurantId, email, decode(encodedPassword));
        return "redirect:/admin/home?userEmail=" + email + "&userPassword=" + encodedPassword;
    }

    @Transactional
    @RequestMapping(value = "/discard")
    public String discardResults(@RequestParam("userEmail") String email, @RequestParam("userPassword") String encodedPassword) {
        logger.info("discarding results of voting in two steps");

        logger.info("step one: setting variables \"votesCount\" in all restaurants to zero");
        List<Restaurant> restaurants = restaurantRepository.findAll();
        for (Restaurant restaurant : restaurants) restaurant.setVotesCount(0);
        restaurantRepository.saveAll(restaurants);

        logger.info("step two: setting chosenRestaurantId's in all users to null");
        List<User> users = userRepository.findAll();
        for (User user : users) user.setChosenRestaurantId(null);
        userRepository.saveAll(users);

        return "redirect:/admin/home?userEmail=" + email + "&userPassword=" + encodedPassword;
    }

}
