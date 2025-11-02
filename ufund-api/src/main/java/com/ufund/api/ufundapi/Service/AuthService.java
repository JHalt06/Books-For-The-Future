package com.ufund.api.ufundapi.Service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.Model.User;

@Service
public class AuthService {
    private final UserService userService;

    @Autowired
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    

    /**
     * Attempts to log in to the UFund service given a username and password.
     *
     * @param username The username of the user logging in.
     * @param password The password of the user logging in.
     * @return true or throws IllegalAccessException depending on success of the attempt.
     */
    public User login(String username, String password) throws IllegalAccessException, IOException {
        var user = userService.getUser(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new IllegalAccessException("Incorrect username or password");
        }
        return user;
    }
}


