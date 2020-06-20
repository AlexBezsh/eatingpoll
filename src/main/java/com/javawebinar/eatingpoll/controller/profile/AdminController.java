package com.javawebinar.eatingpoll.controller.profile;

import com.javawebinar.eatingpoll.transfer.UserDto;
import com.javawebinar.eatingpoll.util.AppUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController extends BasicProfilesController {

    private final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @RequestMapping("/home")
    public ModelAndView home(@SessionAttribute("user") UserDto user) {
        return modelAndViewForHomePage(user);
    }

    @GetMapping("/update")
    public ModelAndView updateProfile(@SessionAttribute("user") UserDto user) {
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

    @Transactional
    @RequestMapping("/users/delete/{id}")
    public String deleteUserByAdmin(@PathVariable String id) {
        userService.deleteUserById(AppUtil.parseId(id));
        return "redirect:/admin/users";
    }

    @Transactional
    @RequestMapping("/vote")
    public String vote(@RequestParam String restaurantId, @SessionAttribute("user") UserDto user) {
        userService.vote(restaurantId, user.getEmail());
        return "redirect:/admin/home";
    }

    @Transactional
    @RequestMapping("/discard")
    public String discardResults() {
        logger.info("discarding results of voting");
        userService.discardResults();
        return "redirect:/admin/home";
    }
}
