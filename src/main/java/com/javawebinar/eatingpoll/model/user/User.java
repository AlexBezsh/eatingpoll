package com.javawebinar.eatingpoll.model.user;

import com.javawebinar.eatingpoll.model.AbstractEntity;
import com.javawebinar.eatingpoll.model.Restaurant;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user")
public class User extends AbstractEntity {

    @Email
    @NotBlank
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 5, max = 60)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @JoinColumn(name = "chosen_restaurant_id")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Restaurant chosenRestaurant;

    public User() {}

    public User(Long id, String name, String email, String password, Role role, Restaurant chosenRestaurant) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.role = role;
        this.chosenRestaurant = chosenRestaurant;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setChosenRestaurant(Restaurant chosenRestaurant) {
        this.chosenRestaurant = chosenRestaurant;
    }

    public Restaurant getChosenRestaurant() {
        return chosenRestaurant;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name=" + name +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
