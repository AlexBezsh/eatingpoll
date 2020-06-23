package com.javawebinar.eatingpoll.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "restaurant")
public class Restaurant extends AbstractEntity {

    @NotNull
    @Column(name = "votes_count")
    private Integer votesCount = 0;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private List<Dish> dishes;

    public Restaurant () {}

    public Restaurant(Long id, String name, List<Dish> dishes) {
        super(id, name);
        this.dishes = dishes;
    }

    public Integer getVotesCount() {
        return votesCount;
    }

    public void setVotesCount(Integer votesCount) {
        this.votesCount = votesCount;
    }

    public void plusVote() { votesCount++; }

    public void minusVote() { votesCount--; }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name=" + name +
                ", votesCount=" + votesCount +
                '}';
    }
}
