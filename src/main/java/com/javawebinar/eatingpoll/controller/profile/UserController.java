package com.javawebinar.eatingpoll.controller.profile;

import com.javawebinar.eatingpoll.transfer.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;

@Controller
@RequestMapping("/user")
public class UserController extends BasicProfilesController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);


    @RequestMapping("/home")
    public ModelAndView home(@SessionAttribute("user") UserDto user) {
        logger.info("loading home page for user: {}", user);
        return modelAndViewForHomePage(user);
    }

    @GetMapping("/update")
    public ModelAndView updateProfile(@SessionAttribute("user") UserDto user) {
        logger.info("loading update form for user: {}", user);
        return modelAndViewForUpdatingUser(user.getEmail());
    }

    @Transactional
    @RequestMapping("/delete")
    public String deleteProfile(@SessionAttribute("user") UserDto user) {
        logger.info("user with email={} deletes his account", user.getEmail());
        userService.deleteUserByEmail(user.getEmail());
        return "redirect:/";
    }

    @Transactional
    @RequestMapping("/vote")
    public String vote(@RequestParam String restaurantId, @SessionAttribute("user") UserDto user) {
        logger.info("user with email={} votes for a restaurant with id={}", user.getEmail(), restaurantId);
        userService.vote(restaurantId, user.getEmail());
        return "redirect:/user/home";
    }
}
