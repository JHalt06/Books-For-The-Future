package com.ufund.api.ufundapi.Controller;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.Model.User;
import com.ufund.api.ufundapi.Service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger LOG = Logger.getLogger(AuthController.class.getName());
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

@GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        LOG.log(Level.INFO, ("GET /users/" + username));
        try {
            User user = userService.getUser(username);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<Object> updateUser(@RequestBody User user, @PathVariable String username) {
        LOG.log(Level.INFO,"PUT /users/" + username + "");
        try {
            user = userService.updateUser(user, username);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException ex) {
            LOG.log(Level.WARNING, ex.getLocalizedMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getLocalizedMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<Object> createUser(@RequestBody Map<String, String> params) {
        LOG.log(Level.INFO, "POST /users "+ params.toString());
        String username = params.get("username");
        String password = params.get("password");

        try {
            User user = userService.createUser(username, password);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IllegalArgumentException ex) {
            LOG.log(Level.WARNING, ex.getLocalizedMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getLocalizedMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
