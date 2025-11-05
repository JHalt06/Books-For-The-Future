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
        User user = userService.getUser(username);

        if(user != null){
            if(user.getPassword().equals(password)){
                return user;
            }
            else{
                throw new IllegalAccessError("Incorrect Passowrd");
            }
        }

        if(!username.equalsIgnoreCase("admin")){
            User newUser = User.create(username, password);
            return userService.createUser(newUser.getUsername(), newUser.getPassword());

        }
        throw new IllegalAccessException("Username not allowed or already exists");
        
        
    }
}


