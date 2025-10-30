package com.ufund.api.ufundapi.Controller;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.Model.User;
import com.ufund.api.ufundapi.Service.AuthService;

@RestController
@RequestMapping("")
public class AuthController {
    private static final Logger LOG = Logger.getLogger(AuthController.class.getName());
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Attempts to log in a given user to the UFund website.
     * @param params JSON containing (username: str, password: str)
     * @return User object of the logged in user, error otherwise.
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody Map<String, String> params) {
        LOG.log(Level.INFO, "POST /login body={" + params + "}");
        String username = params.get("username");
        String password = params.get("password");
        try {
            User result = authService.login(username, password);
            if (result != null) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            throw new IllegalAccessException();
        } catch (IllegalAccessException ex) {
            LOG.log(Level.WARNING, ex.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
