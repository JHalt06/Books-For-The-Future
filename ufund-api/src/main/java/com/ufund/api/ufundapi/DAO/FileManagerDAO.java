package com.ufund.api.ufundapi.DAO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.Model.Manager;

@Repository
public class FileManagerDAO implements ManagerDAO{
    private static final String MANAGER_FILE = "data/manager.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Manager getManager() throws IOException {
        File file = new File(MANAGER_FILE);

        if (!file.exists()){ //if the file does not exist, create it with default manager access creds
            Manager defaultManager = new Manager("manager", "manager123");
            objectMapper.writeValue(file, defaultManager); //Method that can be used to serialize any Java value as JSON output, written to File provided.
            return defaultManager;
        }
        
        //if the file does exist, read it
        String content = Files.readString(Paths.get(MANAGER_FILE));//Reads all content from a file into a string
        return objectMapper.readValue(content, Manager.class);//Method to deserialize JSON content from given JSON content String.
    }
}
