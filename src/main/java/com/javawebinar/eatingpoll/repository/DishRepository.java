package com.javawebinar.eatingpoll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.javawebinar.eatingpoll.model.Dish;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DishRepository extends JpaRepository<Dish, Long>, JpaSpecificationExecutor<Dish> {

    boolean existsByNameAndRestaurantId(String name, Long id);

    @Modifying
    @Query(value = "DELETE FROM dish WHERE id=?", nativeQuery = true)
    void deleteById(Long id);
}
