package com.ufund.api.ufundapi.DAO;

import java.io.File;
import java.io.IOException;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
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
    @Test
    void testAddNeed_duplicateNameThrowsException() throws IOException{
        Need need1 = new Need("NoteBook", 10,5.0);
        cupboardDAO.addNeed(need1);
        
        Need need2 = new Need("NoteBook", 8, 4.0);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cupboardDAO.addNeed(need2);
        });

        String expectedMessage = " Need with this name already exists";
        assertTrue(exception.getMessage().contains("Need with this name"));

    }

    @Test
    void testNeedExistByName() throws IOException{
        Need need = new Need("Pencils", 20, 2.5);
        cupboardDAO.addNeed(need);

        assertTrue(cupboardDAO.needExistByName("Pencils"));
        assertTrue(cupboardDAO.needExistByName("pencils"));
        assertFalse(cupboardDAO.needExistByName("Crayons"));
    }

    @Test
    void testPersistanceToFile() throws IOException{
        Need need = new Need(null, "Crayons", 12,3.5);
        cupboardDAO.addNeed(need);

        //Recreate DAO using same file path
        ObjectMapper mapper = new ObjectMapper();
        FileCupboardDAO reloadDAO = new FileCupboardDAO(tempfile.getAbsolutePath(), mapper);
        
        assertTrue(reloadDAO.needExistByName("Crayons"));
    }
}
