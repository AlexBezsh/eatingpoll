package com.javawebinar.eatingpoll.service;

import com.javawebinar.eatingpoll.exceptions.BadRequestException;
import com.javawebinar.eatingpoll.exceptions.TimeException;
import com.javawebinar.eatingpoll.model.Restaurant;
import com.javawebinar.eatingpoll.model.user.Role;
import com.javawebinar.eatingpoll.model.user.User;
import com.javawebinar.eatingpoll.repository.RestaurantRepository;
import com.javawebinar.eatingpoll.repository.UserRepository;
import com.javawebinar.eatingpoll.transfer.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.javawebinar.eatingpoll.util.AppUtil.*;

@Service
public class UserService {

    private Environment environment;
    private static final String PROP_VOTING_FINISH_HOUR = "voting.finish.hour";
    private static final String PROP_VOTING_FINISH_MINUTE = "voting.finish.minute";

    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRestaurantRepository(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getMockUsersForStartPage() {
        return userRepository.getByIdBetween(1L, 4L);
    }

    public List<UserDto> getAllUsers() {
        List<User> usersFromDB = userRepository.findAll();
        return usersFromDB.stream()
                .filter((a) -> a.getRole() == Role.USER)
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    public User login(User user) {
        User userFromDB = userRepository.findOneByEmail(user.getEmail());
        if (userFromDB == null || !passwordEncoder.matches(user.getPassword(), userFromDB.getPassword()))
            throw new BadRequestException("Wrong email or password. Please try again");
        return userFromDB;
    }

    @Transactional
    public void saveNewUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) throw new BadRequestException("User with this email is already registered");
        user = getCheckedUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
    }

    @Transactional
    public void updateUser(User user) {
        user = getCheckedUser(user);
        User userFromDB = getUserByEmail(user.getEmail());
        userFromDB.setName(user.getName());
        userFromDB.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(userFromDB);
    }

    @Transactional
    public void discardResults() {
        restaurantRepository.setAllVotesCountToZero();
        userRepository.clearAllChosenRestaurants();
    }

    @Transactional
    public void vote(String restaurantId, String email) {
        LocalTime votingFinish = LocalTime.of(
                Integer.parseInt(Objects.requireNonNull(environment.getProperty(PROP_VOTING_FINISH_HOUR))),
                Integer.parseInt(Objects.requireNonNull(environment.getProperty(PROP_VOTING_FINISH_MINUTE))));
        if (LocalTime.now().isAfter(votingFinish)) throw new TimeException("You can't vote after " + votingFinish);

        Long parsedRestaurantId = parseId(restaurantId);
        Restaurant previousRestaurant = restaurantRepository.getChosenRestaurantFromUserByEmail(email);
        if (previousRestaurant == null) restaurantRepository.incrementVotesCount(parsedRestaurantId);
        else if (parsedRestaurantId.equals(previousRestaurant.getId())) return;
        else {
            restaurantRepository.decrementVotesCount(previousRestaurant.getId());
            restaurantRepository.incrementVotesCount(parsedRestaurantId);
        }
        userRepository.setChosenRestaurant(parsedRestaurantId, email);
    }

    @Transactional
    public void deleteUserByEmail(String email) {
        Restaurant chosenRestaurant = restaurantRepository.getChosenRestaurantFromUserByEmail(email);
        if (chosenRestaurant != null) restaurantRepository.decrementVotesCount(chosenRestaurant.getId());
        userRepository.deleteByEmail(email);
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findOneByEmail(email);
        if (user == null) throw new BadRequestException("There is no user with email=" + email + " in repository");
        return user;
    }
}
