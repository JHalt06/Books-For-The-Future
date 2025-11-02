package com.ufund.api.ufundapi.Service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.DAO.AuthDAO;
import com.ufund.api.ufundapi.Model.Authenticator;
import com.ufund.api.ufundapi.Model.User;

@Service
public class AuthService {
    private final UserService userService;
    private final AuthDAO authDAO;

    @Autowired
    public AuthService(UserService userService, AuthDAO authDAO) {
        this.userService = userService;
        this.authDAO = authDAO;
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
        var auth = Authenticator.generate(username);
        authDAO.addAuth(auth);
        return user;
    }

    /**
     * Logs out a user tied to a given session_key
     * @param session_key the session key of the user to log out
     * @throws IOException any error saving the auth file
     */
    public void logout(String session_key) throws IOException {
        authDAO.removeAuth(session_key);
    }
}


