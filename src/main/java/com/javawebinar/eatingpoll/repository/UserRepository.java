package com.javawebinar.eatingpoll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.javawebinar.eatingpoll.model.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findOneByEmailAndPassword(String email, String password);
    User findOneByEmail(String email);
    List<User> findAllByChosenRestaurantId(Long chosenRestaurantId);
}
