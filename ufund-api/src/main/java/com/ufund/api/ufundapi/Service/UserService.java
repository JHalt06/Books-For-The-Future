package com.ufund.api.ufundapi.Service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.DAO.UserDAO;
import com.ufund.api.ufundapi.Model.User;

@Service
public class UserService {
    private final UserDAO userDAO;
    final HelperService helperService;
    final ManagerService managerService;

    public UserService(UserDAO userDao, HelperService helperService, ManagerService managerService) {
        this.userDAO = userDao;
        this.helperService = helperService;
        this.managerService = managerService;
    }

    /**
     * Retrieves a user with the given username from the UserDAO.
     *
     * @param username The username of the user to retrieve.
     * @return The user that was retrieved.
     * @throws IOException If an error occurs while saving the file 
     */
    public User getUser(String username) throws IOException {
        User user = userDAO.getUser(username);
        if (user == null) {
            return null;
        }
        return user;
    }

    /**
     * Returns the total number of users
     *
     * @return The number of users
     * @throws IOException If there was any problems saving the file
     */
    public int getUserCount() throws IOException {
        return userDAO.getUserCount();
    }

    /**
     * Deletes a user in the UFund system.
     * @param username The username of the user to delete.
     * @return true if the user was deleted, false otherwise.
     * @throws IOException If there was any problems saving the file
     */
    public boolean deleteUser(String username) throws IOException {
        return userDAO.deleteUser(username);
    }


    /**
     * Updates a user in the UFund system.
     * @param user The user object of the user being updated.
     * @param username The username of the desired user to update.
     * @return The updated User object.
     * @throws IOException If there was any problems saving the file
     */
    public User updateUser(User user, String username) throws IOException {
        if (!user.getUsername().equals(username)) {
            throw new IllegalArgumentException();
        }
        return userDAO.updateUser(user);
    }
}
