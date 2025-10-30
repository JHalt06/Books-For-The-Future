package com.ufund.api.ufundapi.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.DAO.UserFileDAO;
import com.ufund.api.ufundapi.Model.Need;
import com.ufund.api.ufundapi.Service.HelperService;
import com.ufund.api.ufundapi.Service.UserService;

public class CupboardControllerTest {
    private CupboardController controller;
    private HelperService helperService;
    private UserService userService;
    private File cupboardFile;
    private File inventoryFile;

    @BeforeEach
    void setup() throws IOException{
        cupboardFile = File.createTempFile("test-cupboard", ".json");
        inventoryFile = File.createTempFile("test-inventory", ".json");
        cupboardFile.deleteOnExit();
        inventoryFile.deleteOnExit();

        helperService = new HelperService(cupboardFile.getPath());
        userService = new UserService(new UserFileDAO("../data/users.json", new ObjectMapper()));
        controller = new CupboardController(helperService);
    }

    @Test
    void testBrowseNeedsEmpty(){
        ResponseEntity<Object> response = controller.browseNeeds();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(((List<?>) response.getBody()).isEmpty());
    }

    @Test
    void testCreateNeedSuccess(){
        Need need = new Need(1L, "Notepad", 10, 5.0);
        ResponseEntity<Object> response = controller.createNeed(need); //Extension of HttpEntity that adds an HttpStatusCode status code. Used in RestTemplate as well as in @Controller methods.
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Notepad", ((Need) response.getBody()).getName());
    }

    @Test
    void testRemoveNeedNotFound(){
        Need need = new Need(99L, "Xbox", 1, 1.0);
        ResponseEntity<Object> response = controller.removeNeed(need); //Extension of HttpEntity that adds an HttpStatusCode status code. Used in RestTemplate as well as in @Controller methods.
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateNeed_NOTFOUND() throws IOException{
        Need need = new Need(998L, "pencil",1,1.0);
        ResponseEntity<Object> response = controller.updateNeed(need);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test 
    void testUpdateNeed_Success() throws IOException{
        Need need = new Need("Laptop", 5, 1000.0);
        Need addedNeed = helperService.addNeed(need);

        Need updatedNeed = new Need(1L, "Laptop Pro", 10,1200.0);
        ResponseEntity<Object> response = controller.updateNeed(updatedNeed);
        Need returnedNeed = (Need) response.getBody();
        assertNotNull(returnedNeed);
        assertEquals("Laptop Pro", returnedNeed.getName());
        assertEquals(10,returnedNeed.getquantity());
        assertEquals(1200.0, returnedNeed.getFundingAmount());


    }

    @Test
    void testCheckoutNeed_Valid() throws IOException{
        Need need = new Need(998L, "Test Tubes",50,1.0);
        helperService.addNeed(need);
        List<Map<String, Integer>> needs = new ArrayList<>();
        HashMap<String,Integer> map = new HashMap<>();
        map.put("needID", need.getId().intValue());
        map.put("quantity", 20);
        needs.add(map);

        ResponseEntity<Object> response = controller.checkoutNeed(needs);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCheckoutNeed_InvalidBAD() throws IOException{
        Need need = new Need(998L, "Test Tubes",50,1.0);
        helperService.addNeed(need);
        List<Map<String, Integer>> needs = new ArrayList<>();
        HashMap<String,Integer> map = new HashMap<>();
        map.put("needID", 997);
        map.put("quantity", 20);
        needs.add(map);

        ResponseEntity<Object> response = controller.checkoutNeed(needs);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
