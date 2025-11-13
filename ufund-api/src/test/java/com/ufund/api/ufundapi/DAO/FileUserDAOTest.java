package com.ufund.api.ufundapi.DAO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FileUserDAOTest {
    private File tempfile;
    private UserFileDAO userDAO;

    @BeforeEach
    void setup() throws IOException {
        // create a temp file for isolated data
        tempfile = File.createTempFile("test-users", ".json");
        Files.writeString(tempfile.toPath(), "[]");
        tempfile.deleteOnExit(); // Auto delete after test

        userDAO = new UserFileDAO(tempfile.getAbsolutePath(), new ObjectMapper());
    }

    @Test
    void testDeleteUser() throws IOException {
        userDAO.createUser("Heisenburg", "waltuh");
        boolean response = userDAO.deleteUser("Heisenburg");
        assertEquals(response, true);
    }

    @Test
    void testDeleteUser_invalid() throws IOException {
        userDAO.createUser("Heisenburg", "waltuh");
        boolean response = userDAO.deleteUser("Jesse");
        assertEquals(response, false);
    }

    @Test
    void testLoadUsers_newFile() throws IOException {
        UserFileDAO ufd = mock(UserFileDAO.class);
        ufd.loadUsersForTesting();
        int users = ufd.getUserCount();
        assertEquals(users, 0);
    }
}
