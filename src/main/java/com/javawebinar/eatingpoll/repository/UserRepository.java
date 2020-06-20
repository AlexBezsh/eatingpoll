package com.javawebinar.eatingpoll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.javawebinar.eatingpoll.model.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User getById(Long id);
    boolean existsByEmail(String email);
    User findOneByEmail(String email);
    void deleteByEmail(String email);
    List<User> findAllByChosenRestaurantId(Long chosenRestaurantId);
}
