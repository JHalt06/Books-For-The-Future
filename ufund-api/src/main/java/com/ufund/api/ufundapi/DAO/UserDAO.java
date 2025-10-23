package com.ufund.api.ufundapi.DAO;

import java.io.IOException;

import com.ufund.api.ufundapi.Model.User;


public interface UserDAO {
    User getUser(String username) throws IOException;

    int getUserCount() throws IOException;

    User[] getUsers() throws IOException;

    boolean deleteUser(String username) throws IOException;

    User updateUser(User user) throws IOException;
}
