package com.javawebinar.eatingpoll.controller.profile;

import com.javawebinar.eatingpoll.transfer.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController extends BasicProfilesController {

    private final Logger logger = LoggerFactory.getLogger(AdminController.class);

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

    @RequestMapping("/users")
    public ModelAndView users() {
        logger.info("loading all users");
        List<UserDto> users = userService.getAllUsers();
        ModelAndView mav = new ModelAndView("users");
        mav.addObject("users", users);
        return mav;
    }

    @RequestMapping("/users/delete")
    public String deleteUserByAdmin(@RequestParam String email) {
        logger.info("deleting user with email={} by admin", email);
        userService.deleteUserByEmail(email);
        return "redirect:/admin/users";
    }

    @RequestMapping("/vote")
    public String vote(@RequestParam String restaurantId, @SessionAttribute("user") UserDto user) {
        logger.info("admin votes for a restaurant with id={}", restaurantId);
        userService.vote(restaurantId, user.getEmail());
        return "redirect:/admin/home";
    }

    @RequestMapping("/discard")
    public String discardResults() {
        logger.info("admin discards results of voting");
        userService.discardResults();
        return "redirect:/admin/home";
    }
}
