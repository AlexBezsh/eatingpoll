package com.javawebinar.eatingpoll.controller.profile;

import com.javawebinar.eatingpoll.exceptions.BadRequestException;
import com.javawebinar.eatingpoll.exceptions.EntityNotFoundException;
import com.javawebinar.eatingpoll.model.Restaurant;
import com.javawebinar.eatingpoll.model.user.User;
import com.javawebinar.eatingpoll.service.UserService;
import com.javawebinar.eatingpoll.transfer.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class BasicProfilesController {

    private final Logger logger = LoggerFactory.getLogger(BasicProfilesController.class);

    protected UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public ModelAndView startPage() {
        logger.info("start page is loading");
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("users", userService.getMockUsersForStartPage());
        mav.addObject("userToLogin", new User());
        return mav;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, HttpServletRequest httpRequest) {
        logger.info("user: {} is logging in", user);

        User userFromDB = userService.getUserIfExists(user.getEmail(), user.getPassword());
        if (userFromDB == null) throw new BadRequestException("Wrong email or password");

        HttpSession session = httpRequest.getSession();
        session.setAttribute("user", new UserDto(userFromDB));

        return userFromDB.isAdmin() ? "redirect:/admin/home" : "redirect:/user/home";
    }

    @RequestMapping("/mock/login")
    public String loginForMockUsers(@RequestParam String email, HttpServletRequest httpRequest) {
        User user;
        if (email.equals("user1@gmail.com")
            || email.equals("user2@gmail.com")
            || email.equals("admin1@gmail.com"))
            user = userService.getUserByEmail(email);
        else throw new BadRequestException("Invalid mock data");

        if (user == null) throw new EntityNotFoundException("There is no mock user with email " + email + " in database");

        HttpSession session = httpRequest.getSession();
        session.setAttribute("user", new UserDto(user));

        return user.isAdmin() ? "redirect:/admin/home" : "redirect:/user/home";
    }

    @RequestMapping("/register")
    public ModelAndView register() {
        logger.info("loading form page for new user");
        ModelAndView mav = new ModelAndView("newUserForm");
        mav.addObject("user", new User());
        return mav;
    }

    @RequestMapping("/save")
    public void saveUser(@ModelAttribute("user") User user, HttpServletRequest request, HttpServletResponse response) {
        logger.info("saving new user with email: {}", user.getEmail());
        userService.saveNewUser(user);
        request.setAttribute("message", "You have successfully registered. Please log in to access your account");
        try {
            request.getServletContext().getRequestDispatcher(request.getContextPath() + "/").forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/update")
    protected void updateUser(@ModelAttribute("user") User user, HttpServletRequest request, HttpServletResponse response) {
        logger.info("saving updated user with email: {}", user.getEmail());
        userService.updateUser(user);
        request.setAttribute("message", "Your profile has been updated. Please log in to access your account");
        try {
            request.getServletContext().getRequestDispatcher(request.getContextPath() + "/").forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    protected ModelAndView modelAndViewForHomePage(UserDto user) {
        logger.info("loading home page for user: {}", user);
        ModelAndView mav = new ModelAndView(user.isAdmin() ? "adminPage" : "userPage");
        mav.addObject("user", user);
        mav.addObject("restaurants", userService.getAllRestaurants());
        if (user.isAdmin()) mav.addObject("restaurant", new Restaurant());
        return mav;
    }

    protected ModelAndView modelAndViewForUpdatingUser(String email) {
        logger.info("loading update form for user with email: {}", email);
        if (!userService.existsByEmail(email)) throw new BadRequestException("Wrong email");
        ModelAndView mav = new ModelAndView("updateUserForm");
        User user = new User();
        user.setEmail(email);
        mav.addObject("user", user);
        return mav;
    }
}
