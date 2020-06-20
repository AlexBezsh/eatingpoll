package com.javawebinar.eatingpoll.transfer;

import com.javawebinar.eatingpoll.model.user.Role;
import com.javawebinar.eatingpoll.model.user.User;

public class UserDto {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private Long chosenRestaurantId;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.chosenRestaurantId = user.getChosenRestaurantId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getChosenRestaurantId() {
        return chosenRestaurantId;
    }

    public void setChosenRestaurantId(Long chosenRestaurantId) {
        this.chosenRestaurantId = chosenRestaurantId;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", chosenRestaurantId=" + chosenRestaurantId +
                '}';
    }
}
