package com.javawebinar.eatingpoll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.javawebinar.eatingpoll.model.user.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    List<User> getByIdBetween(Long from, Long to); //for mock users
    boolean existsByEmail(String email);
    User findOneByEmail(String email);
    void deleteByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE User u set u.name = ?1, u.password = ?2 where u.email = ?3")
    void updateUserProfileByEmail(String name, String password, String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE User u SET u.chosenRestaurant = null")
    void clearAllChosenRestaurants();

    @Modifying
    @Query(value = "UPDATE User SET chosen_restaurant_id = ?1 WHERE email = ?2", nativeQuery = true)
    void setChosenRestaurant(Long id, String email);
}
