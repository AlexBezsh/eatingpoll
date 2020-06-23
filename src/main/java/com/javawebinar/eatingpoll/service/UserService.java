package com.javawebinar.eatingpoll.service;

import com.javawebinar.eatingpoll.exceptions.BadRequestException;
import com.javawebinar.eatingpoll.exceptions.EntityNotFoundException;
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
import static com.javawebinar.eatingpoll.util.AppUtil.checkEntity;

@Service
public class UserService {

    private Environment env;
    private static final String PROP_VOTING_FINISH_HOUR = "voting.finish.hour";
    private static final String PROP_VOTING_FINISH_MINUTE = "voting.finish.minute";

    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
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

    public User login(User user) {
        User userFromDB = userRepository.findOneByEmail(user.getEmail());
        if (userFromDB == null || !passwordEncoder.matches(user.getPassword(), userFromDB.getPassword()))
            throw new BadRequestException("Wrong email or password. Please try again");
        return userFromDB;
    }

    public void saveNewUser(User user) {
        if (existsByEmail(user.getEmail())) throw new BadRequestException("User with this email is already registered");
        checkEntity(user, user.getName(), user.getEmail(), user.getPassword(), user.getRole());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
    }

    @Transactional
    public void updateUser(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        String name = user.getName();
        checkEntity(user, name, email, password, user.getRole());
        if (!existsByEmail(email))
            throw new EntityNotFoundException("There is no user with email=" + email + " in repository");
        userRepository.updateUserProfileByEmail(name, passwordEncoder.encode(password), email);
    }

    @Transactional
    public void discardResults() {
        restaurantRepository.setAllVotesCountToZero();
        userRepository.clearAllChosenRestaurants();
    }

    @Transactional
    public void vote(String restaurantId, String email) {
        LocalTime votingFinish = LocalTime.of(
                Integer.parseInt(Objects.requireNonNull(env.getProperty(PROP_VOTING_FINISH_HOUR))),
                Integer.parseInt(Objects.requireNonNull(env.getProperty(PROP_VOTING_FINISH_MINUTE))));
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

    public List<UserDto> getAllUsers() {
        List<User> usersFromDB = userRepository.findAll();
        return usersFromDB.stream()
                .filter((a) -> a.getRole() == Role.USER)
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findOneByEmail(email);
        if (user == null) throw new BadRequestException("Wrong email or password");
        return user;
    }

    private boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
