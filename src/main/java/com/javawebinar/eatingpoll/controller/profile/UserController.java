package com.javawebinar.eatingpoll.controller.profile;

import com.javawebinar.eatingpoll.exceptions.BadRequestException;
import com.javawebinar.eatingpoll.exceptions.EntityNotFoundException;
import com.javawebinar.eatingpoll.model.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;

import static com.javawebinar.eatingpoll.util.AppUtil.decode;
import static com.javawebinar.eatingpoll.util.AppUtil.parseId;

@Controller
@RequestMapping(value = "/user")
public class UserController extends BasicProfilesController {

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
    public String deleteProfile(@RequestParam String userId, @RequestParam("userEmail") String email, @RequestParam("userPassword") String encodedPassword) {
        User user = userRepository.findOneByEmailAndPassword(email, decode(encodedPassword));
        if (user == null) throw new EntityNotFoundException("Wrong email or password received");
        if (user.getId().equals(parseId(userId))) super.deleteUser(userId);
        else throw new BadRequestException("You can't delete user with id=" + userId);
        return "redirect:/";
    }

    @Transactional
    @RequestMapping(value = "/vote")
    public String userVote(@RequestParam String restaurantId, @RequestParam("userEmail") String email, @RequestParam("userPassword") String encodedPassword) {
        vote(restaurantId, email, decode(encodedPassword));
        return "redirect:/user/home?userEmail=" + email + "&userPassword=" + encodedPassword;
    }
}
