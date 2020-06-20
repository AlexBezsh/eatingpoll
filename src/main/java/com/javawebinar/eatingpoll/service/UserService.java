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
    private final Object objectForSynchronization = new Object();

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
        return List.of(userRepository.getById(1L), userRepository.getById(2L), userRepository.getById(3L));
    }

    public User login(User user) {
        User userFromDB = userRepository.findOneByEmail(user.getEmail());
        if (userFromDB == null || !passwordEncoder.matches(user.getPassword(), userFromDB.getPassword()))
            throw new BadRequestException("Wrong email or password. Please try again");
        return userFromDB;
    }

    public void saveNewUser(User user) {
       // logger.info("registering user: {}", user);
        if (existsByEmail(user.getEmail())) throw new BadRequestException("User with this email is already registered");
        checkEntity(user, user.getName(), user.getEmail(), user.getPassword(), user.getRole());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
    }

    public void updateUser(User user) {
       // logger.info("updating user with email={}", email);
        String email = user.getEmail();
        checkEntity(user, user.getName(), email, user.getPassword(), user.getRole());
        User userFromDB = userRepository.findOneByEmail(email);
        if (userFromDB == null) throw new EntityNotFoundException("There is no user with email=" + email + " in repository");
        userFromDB.setName(user.getName());
        userFromDB.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(userFromDB);
    }

    public void discardResults() {
      //  logger.info("step one: setting variables \"votesCount\" in all restaurants to zero");
        List<Restaurant> restaurants = restaurantRepository.findAll();
        for (Restaurant restaurant : restaurants) restaurant.setVotesCount(0);
        restaurantRepository.saveAll(restaurants);

       // logger.info("step two: setting chosenRestaurantId's in all users to null");
        List<User> users = userRepository.findAll();
        for (User user : users) user.setChosenRestaurantId(null);
        userRepository.saveAll(users);

    }

    public void vote(String restaurantId, String userEmail) {
        LocalTime votingFinish = LocalTime.of(
                Integer.parseInt(Objects.requireNonNull(env.getProperty(PROP_VOTING_FINISH_HOUR))),
                Integer.parseInt(Objects.requireNonNull(env.getProperty(PROP_VOTING_FINISH_MINUTE))));
        if (LocalTime.now().isAfter(votingFinish)) throw new TimeException("You can't vote after " + votingFinish);

        User user = getUserByEmail(userEmail);
        //logger.info("user: {} choosing restaurant with id={}. Saving process takes three steps", user, restaurantId);

        long parsedRestaurantId = parseId(restaurantId);
        if (restaurantRepository.existsById(parsedRestaurantId)) {
            //logger.info("step one: checking whether user: {} has already voted and decrementing number of votes in previous restaurant", user);
            synchronized (objectForSynchronization) {
                if (user.hasVoted()) {
                    Restaurant previousUserRestaurant = restaurantRepository.findById(user.getChosenRestaurantId()).get();
                    previousUserRestaurant.minusVote();
                    restaurantRepository.saveAndFlush(previousUserRestaurant);
                }
                //logger.info("step two: incrementing number of votes in chosen restaurant");
                Restaurant restaurant = restaurantRepository.findById(parsedRestaurantId).get();
                restaurant.plusVote();
                restaurantRepository.saveAndFlush(restaurant);

               // logger.info("step three: saving new \"chosenRestaurantId\" in user: {}", user);
                user.setChosenRestaurantId(restaurant.getId());
                userRepository.saveAndFlush(user);
            }
        } else
            throw new EntityNotFoundException("There is no restaurant with id=" + parsedRestaurantId + " in repository");
    }

    public void deleteUserById(Long userId) {
        User userFromDB;
        if (userRepository.existsById(userId)) userFromDB = userRepository.findById(userId).get();
        else throw new EntityNotFoundException("There is no user with id=" + userId + " in repository");

        if (userFromDB.hasVoted() && restaurantRepository.existsById(userFromDB.getChosenRestaurantId())) {
            Restaurant restaurantFromDB = restaurantRepository.findById(userFromDB.getChosenRestaurantId()).get();
            restaurantFromDB.minusVote();
            restaurantRepository.saveAndFlush(restaurantFromDB);
        }
        userRepository.deleteById(userId);
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

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
