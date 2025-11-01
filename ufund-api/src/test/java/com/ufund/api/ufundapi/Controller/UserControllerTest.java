package com.ufund.api.ufundapi.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.DAO.UserFileDAO;
import com.ufund.api.ufundapi.Model.User;
import com.ufund.api.ufundapi.Model.User.UserRole;
import com.ufund.api.ufundapi.Service.UserService;

public class UserControllerTest {
    /**
     * - Add need to basket
     * - Remove need from basket
     * - Checkout needs
     * - etc
     */

    private UserController controller;
    private UserService userService;
    private File userFile;

    @BeforeEach
    void setup() throws IOException{
        userFile = File.createTempFile("test-users", ".json");
        Files.writeString(userFile.toPath(), "[]");
        userFile.deleteOnExit();

        UserFileDAO userDAO = new UserFileDAO(userFile.getAbsolutePath(), new ObjectMapper());
        userService = new UserService(userDAO);
        controller = new UserController(userService);
    }

    @Test
    void testUpdateUser_Valid() throws IOException {
        userService.createUser("WalterWhite", "bluecrystal");
        User user = new User("WalterWhite", "felina", new ArrayList<>(), UserRole.HELPER);

        ResponseEntity<Object> response = controller.updateUser(user, "WalterWhite");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateUser_BAD_REQUEST() throws IOException {
        userService.createUser("WalterWhite", "bluecrystal");
        User user = new User("WalterWhite", "felina", new ArrayList<>(), UserRole.HELPER);

        ResponseEntity<Object> response = controller.updateUser(user, "Heisenburg");
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdateUser_IOEXCEPTION() throws IOException{
        UserService us = mock(UserService.class);
        UserController uc = new UserController(us);
        userService.createUser("WalterWhite", "bluecrystal");
        User user = new User("WalterWhite", "felina", new ArrayList<>(), UserRole.HELPER);

        doThrow(new IOException("Simulated error"))
            .when(us)
            .updateUser(any(), anyString());
        

        ResponseEntity<Object> response = uc.updateUser(user, "Heisenburg");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testCreateUser_Valid() throws IOException {
        HashMap<String,String> map = new HashMap<>();
        map.put("username", "WalterWhite");
        map.put("password", "bluecrystal");

        ResponseEntity<Object> response = controller.createUser(map);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testCreateUser_CONFLICT() throws IOException {
        HashMap<String,String> map = new HashMap<>();
        map.put("username", "WalterWhite");
        map.put("password", "bluecrystal");

        controller.createUser(map);

        HashMap<String,String> map2 = new HashMap<>();
        map2.put("username", "WalterWhite");
        map2.put("password", "felina");

        ResponseEntity<Object> response = controller.createUser(map2);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testCreateUser_IOEXCEPTION() throws IOException{
        UserService us = mock(UserService.class);
        UserController uc = new UserController(us);

        doThrow(new IOException("Simulated error"))
            .when(us)
            .createUser(anyString(), anyString());
        
        HashMap<String,String> map = new HashMap<>();
        map.put("username", "WalterWhite");
        map.put("password", "bluecrystal");
        ResponseEntity<Object> response = uc.createUser(map);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetUser_Valid() throws IOException {
        userService.createUser("WalterWhite", "bluecrystal");
        ResponseEntity<User> response = controller.getUser("WalterWhite");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetUser_NOTFOUND() throws IOException {
        userService.createUser("WalterWhite", "bluecrystal");
        ResponseEntity<User> response = controller.getUser("Jesse");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetUser_IOEXCEPTION() throws IOException{
        UserService us = mock(UserService.class);
        UserController uc = new UserController(us);
        us.createUser("WalterWhite", "bluecrystal");

        doThrow(new IOException("Simulated error"))
            .when(us)
            .getUser(anyString());

        ResponseEntity<User> response = uc.getUser("WalterWhite");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
