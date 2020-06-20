package com.javawebinar.eatingpoll.controller.profile;

import com.javawebinar.eatingpoll.model.user.User;
import com.javawebinar.eatingpoll.transfer.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;

@Controller
@RequestMapping("/user")
public class UserController extends BasicProfilesController {

    @RequestMapping("/home")
    public ModelAndView home(@SessionAttribute("user") UserDto user) {
        return modelAndViewForHomePage(user);
    }

    @GetMapping("/update")
    public ModelAndView updateProfile(@SessionAttribute("user") UserDto user) {
        return modelAndViewForUpdatingUser(user.getEmail());
    }

    @Transactional
    @RequestMapping("/delete")
    public String deleteProfile(@SessionAttribute("user") UserDto user) {
        User userFromDB = userService.getUserByEmail(user.getEmail());
        userService.deleteUserById(userFromDB.getId());
        return "redirect:/";
    }

    @Transactional
    @RequestMapping("/vote")
    public String vote(@RequestParam String restaurantId, @SessionAttribute("user") UserDto user) {
        userService.vote(restaurantId, user.getEmail());
        return "redirect:/user/home";
    }
}
