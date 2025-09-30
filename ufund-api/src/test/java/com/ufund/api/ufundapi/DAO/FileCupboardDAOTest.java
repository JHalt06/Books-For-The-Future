package com.ufund.api.ufundapi.DAO;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.Model.Need;

public class FileCupboardDAOTest {

    private File tempfile;
    private FileCupboardDAO cupboardDAO;

    @BeforeEach
    void setup() throws IOException{
        //create a temp file for isolated data
        tempfile = File.createTempFile("test-cupboard", ".json");
        tempfile.deleteOnExit(); //Auto delete after test

        ObjectMapper mapper = new ObjectMapper();

        cupboardDAO = new FileCupboardDAO(tempfile.getAbsolutePath(), mapper );
        //Initailize 

    }

    @Test 
    void testAddNeed_success() throws IOException{
        Need newNeed = new Need( "Backpack", 5,12.5);
        Need cretaeNeed = cupboardDAO.addNeed(newNeed);

        assertNotNull(cretaeNeed);
        assertEquals("Backpack", cretaeNeed.getName());
        assertEquals(5,cretaeNeed.getquantity());
        assertEquals(12.5, cretaeNeed.getFundingAmount());
        assertTrue(cretaeNeed.getId() > 0);
    }
}
