package com.ufund.api.ufundapi.DAO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.Model.User;
import com.ufund.api.ufundapi.Model.User.UserRole;

@Repository
public class UserFileDAO implements UserDAO {

    private final Map<String, User> users;
    private final ObjectMapper objectMapper;
    private final String filename;

    public UserFileDAO(@Value("${users.filepath}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        users = new HashMap<>();
        loadUsers();
    }

    /**
     * Loads the users from the file
     * @throws IOException If an error occurs while reading the file
     */
    private void loadUsers() throws IOException {
        users.clear();

        File file = new File(filename);
        if (!file.exists()) {
            // If the file does not exist, check if the directory exists.
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs(); // Create the 'data' directory if it doesn't exist.
            }
            // Create the file with an empty JSON array.
            objectMapper.writeValue(file, new User[0]);
        }

        User[] usersArray = objectMapper.readValue(file, User[].class);

        for (User user : usersArray) {
            users.put(user.getUsername(), user);
        }
    }

    /**
     * Saves the users to users.json
     * @throws IOException If an error occurs while reading the file
     */
    private void saveUsers() throws IOException {
        objectMapper.writeValue(new File(filename), users.values());
    }

    @Override
    public User[] getUsers() {
        synchronized (users) {
            return users.values().toArray(User[]::new);
        }
    }

    @Override
    public int getUserCount() {
        synchronized (users) {
            return users.size();
        }
    }

    /**
     * Obtains a user object given the username of a user.
     * @param username The username of the user to obtain.
     */
    @Override
    public User getUser(String username) {
        synchronized (users) {
            return users.getOrDefault(username, null);
        }
    }

    @Override
    public boolean deleteUser(String username) throws IOException {
        synchronized (users) {
            if (users.containsKey(username)) {
                users.remove(username);
                saveUsers();
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public User updateUser(User user) throws IOException {
        synchronized (users) {
            if (users.containsKey(user.getUsername())) {
                var old = users.put(user.getUsername(), user);
                user.copyPassword(old);
                objectMapper.writeValue(new File(filename), users.values());
                return user;
            } else {
                return null;
            }
        }
    }

    @Override
    public User createUser(String username, String password) throws IOException {
        synchronized (users) {
            if (users.containsKey(username)) {
                return null;
            }
            User newUser = new User(username, password, new ArrayList<>(), UserRole.HELPER);
            users.put(username, newUser);
            saveUsers();
            return newUser;
        }
    }
}
