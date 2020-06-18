package com.javawebinar.eatingpoll.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "dish")
public class Dish extends AbstractEntity {

    @NotNull
    @Column(name = "price")
    private Double price;

    @NotNull
    @Column(name = "restaurant_id")
    @JoinColumn(table = "restaurant", name = "id")
    private Long restaurantId;

    public Dish() {}

    public Dish(Long id, String name, Double price, Long restaurantId) {
        super(id, name);
        this.price = price;
        this.restaurantId = restaurantId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name=" + name +
                ", price=" + price +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
