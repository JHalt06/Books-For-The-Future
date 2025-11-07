package com.ufund.api.ufundapi.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufund.api.ufundapi.DAO.FileAuthDAO;
import com.ufund.api.ufundapi.DAO.UserFileDAO;
import com.ufund.api.ufundapi.Model.User;
import com.ufund.api.ufundapi.Service.AuthService;
import com.ufund.api.ufundapi.Service.UserService;

public class AuthControllerTest {
    /**
     * - Login/logout procedures
     */
    
    private AuthController controller;
    private AuthService authService;
    private UserService userService;
    private File userFile;
    private File authsFile;

    @BeforeEach
    void setup() throws IOException{
        userFile = File.createTempFile("test-users", ".json");
        authsFile = File.createTempFile("test-auths", ".json");

        Files.writeString(userFile.toPath(), "[]");
        Files.writeString(authsFile.toPath(), "[]");
        userFile.deleteOnExit();
        authsFile.deleteOnExit();
        
        ObjectMapper objMapper = new ObjectMapper();
        objMapper.registerModule(new JavaTimeModule());

        UserFileDAO userDAO = new UserFileDAO(userFile.getAbsolutePath(), new ObjectMapper());
        FileAuthDAO authDAO = new FileAuthDAO(objMapper, authsFile.getAbsolutePath());

        userService = new UserService(userDAO);
        authService = new AuthService(userService, authDAO);
        controller = new AuthController(authService);
    }

    @Test
    void testLogin_Valid() throws IOException {
        HashMap<String,String> map = new HashMap<>();
        userService.createUser("WalterWhite", "bluecrystal");
        map.put("username", "WalterWhite");
        map.put("password", "bluecrystal");
        
        ResponseEntity<User> response = controller.login(map);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLogin_UNAUTHORIZED() throws IOException {
        HashMap<String,String> map = new HashMap<>();
        userService.createUser("WalterWhite", "bluecrystal");
        map.put("username", "Jesse");
        map.put("password", "bluecrystal");
        
        ResponseEntity<User> response = controller.login(map);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    // @Test
    // void testLogin_IOEXCEPTION() throws IOException {
    // }
}
