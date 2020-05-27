package com.javawebinar.eatingpoll.model.user;

import com.javawebinar.eatingpoll.model.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User extends AbstractEntity {

    @Email
    @NotBlank
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 5, max = 20)
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "chosen_restaurant_id")
    @JoinColumn(table = "restaurants", name = "id")
    private Long chosenRestaurantId; //https://stackoverflow.com/questions/6311776/hibernate-foreign-keys-instead-of-entities

    public User() {}

    public User(Long id, String name, String email, String password, Role role, Long chosenRestaurantId) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.role = role;
        this.chosenRestaurantId = chosenRestaurantId;
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

    public boolean hasVoted() {
        return chosenRestaurantId != null;
    }

    public void setChosenRestaurantId(Long chosenRestaurantId) {
        this.chosenRestaurantId = chosenRestaurantId;
    }

    public Long getChosenRestaurantId() {
        return chosenRestaurantId;
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
                ", chosenRestaurantId=" + chosenRestaurantId +
                '}';
    }
}
