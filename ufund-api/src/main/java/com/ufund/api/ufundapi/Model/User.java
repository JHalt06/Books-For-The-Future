package com.ufund.api.ufundapi.Model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    public enum UserRole {
        HELPER,
        MANAGER
    }

    @JsonProperty("username")
    private final String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("basket")
    private final List<Long> basket; // basket of need ids
    @JsonProperty("role")
    private final UserRole role;

    /**
     * Creates a new user with a given username, password, funding basket and UserRole.
     * 
     * @param username The username of the user to create.
     * @param password The password of the user to create.
     * @param basket The basket of the user to create.
     * @param userRole the UserRole of the user to create.
     */
    public User(@JsonProperty("username") String username, @JsonProperty("password") String password,
            @JsonProperty("basket") List<Long> basket, @JsonProperty("role") UserRole userRole) {
        this.username = username;
        this.basket = basket;
        this.password = password;
        this.role = userRole;
    }

    public static User create(String username, String password) {
        return new User(
                username,
                password,
                new ArrayList<>(),
                UserRole.HELPER);
    }

    public String getUsername() {
        return username;
    }
   

    /**
     * Adds a need ID of a given need to the user's basket.
     * @param need The need to add to the user's basket.
     */
    public void addToBasket(Need need) {
        basket.add(need.getId());
    }

    public Long[] getBasket() {
        return basket.toArray(Long[]::new);
    }

    /**
     * Given the ID of a need, attempts to remove it from the basket.
     * @param needID The ID of the need to remove.
     */
    public void removeBasketNeed(Integer needID) {
        basket.remove(Integer.toUnsignedLong(needID));
    }

    public UserRole getRole() {
        return role;
    }

    public void copyPassword(User other) {
        this.password = other.password;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public String toString() {
        return "Username: " + this.username + "Role: " + this.role + ", Basket: " + this.basket;
    }

}
