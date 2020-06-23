package com.javawebinar.eatingpoll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.javawebinar.eatingpoll.model.Restaurant;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, JpaSpecificationExecutor<Restaurant> {

    boolean existsByName(String name);

    @Query(value = "SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.dishes")
    List<Restaurant> findAll();

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM restaurant WHERE id=?", nativeQuery = true)
    void deleteById(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Restaurant r SET r.votesCount = 0")
    void setAllVotesCountToZero();

    @Modifying
    @Query("UPDATE Restaurant r SET r.votesCount = r.votesCount + 1 WHERE r.id = ?1")
    void incrementVotesCount(Long id);

    @Modifying
    @Query("UPDATE Restaurant r SET r.votesCount = r.votesCount - 1 WHERE r.id = ?1")
    void decrementVotesCount(Long id);

    @Query(value = "SELECT u.chosenRestaurant FROM User u WHERE u.email = ?1")
    Restaurant getChosenRestaurantFromUserByEmail(String email);
}
