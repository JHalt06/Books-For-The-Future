package com.ufund.api.ufundapi.Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.DAO.FileCupboardDAO;
import com.ufund.api.ufundapi.DAO.UserFileDAO;
import com.ufund.api.ufundapi.Model.Need;
import com.ufund.api.ufundapi.Service.HelperService;
import com.ufund.api.ufundapi.Service.NotificationService;
import com.ufund.api.ufundapi.Service.UserService;

public class CupboardControllerTest {
    private CupboardController controller;
    private HelperService helperService;
    private NotificationService notificationService;
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
        notificationService = mock(NotificationService.class);

        controller = new CupboardController(helperService, notificationService);
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
    void testCreateNeed_CONFLICK(){
        Need need = new Need(1L, "Notepad", 10, 5.0);
        Need dupe = new Need(2L, "Notepad", 25, 5.0);
        controller.createNeed(need); //Extension of HttpEntity that adds an HttpStatusCode status code. Used in RestTemplate as well as in @Controller methods.

        ResponseEntity<Object> response = controller.createNeed(dupe); //Extension of HttpEntity that adds an HttpStatusCode status code. Used in RestTemplate as well as in @Controller methods.
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
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

    @Test
    void testCheckoutNeed_InvalidILLEGALACCESS() throws IOException{
        Need need = new Need(998L, "Test Tubes",50,1.0);
        helperService.addNeed(need);
        List<Map<String, Integer>> needs = new ArrayList<>();
        HashMap<String,Integer> map = new HashMap<>();
        map.put("needID", 998);
        map.put("quantity", -1);
        needs.add(map);

        ResponseEntity<Object> response = controller.checkoutNeed(needs);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testUpdateNeedByID_Valid() throws IOException{
        Need need = new Need(99L, "Laptop", 5, 1000.0);
        helperService.addNeed(need);

        Need updatedNeed = new Need(99L, "Laptop Pro", 10,1200.0);
        long id = need.getId();
        ResponseEntity<Object> response = controller.updateNeedById(id, updatedNeed);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateNeedByID_NOTFOUND() throws IOException{
        Need need = new Need(99L, "Laptop", 5, 1000.0);
        helperService.addNeed(need);

        Need updatedNeed = new Need(98L, "Laptop Pro", 10,1200.0);
        long id = need.getId();
        ResponseEntity<Object> response = controller.updateNeedById(id, updatedNeed);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateNeed_IOEXCEPTION() throws IOException{
        HelperService hs = mock(HelperService.class);
        NotificationService ns = new NotificationService();
        CupboardController cc = new CupboardController(hs, ns);

        Need updatedNeed = new Need(99L, "Laptop Pro", 10,1200.0);

        doThrow(new IOException("Simulated error"))
                .when(hs)
                .updateNeed(any(Need.class));

        ResponseEntity<Object> response = cc.updateNeedById(99L, updatedNeed);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testSearchNeeds_Valid() throws IOException{
        Need need1 = new Need(99L, "Laptop", 5, 1000.0);
        Need need2 = new Need(98L, "iPhone", 5, 1000.0);
        Need need3 = new Need(97L, "Samsung", 5, 1000.0);
        Need need4 = new Need(96L, "Flip Phone", 5, 1000.0);
        helperService.addNeed(need1);
        helperService.addNeed(need2);
        helperService.addNeed(need3);
        helperService.addNeed(need4);

        ResponseEntity<Need[]> response = controller.searchNeeds("Phone");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void testSearchNeeds_NOTFOUND() throws IOException{
        Need need1 = new Need(99L, "Laptop", 5, 1000.0);
        Need need2 = new Need(98L, "iPhone", 5, 1000.0);
        Need need3 = new Need(97L, "Samsung", 5, 1000.0);
        Need need4 = new Need(96L, "Flip Phone", 5, 1000.0);
        helperService.addNeed(need1);
        helperService.addNeed(need2);
        helperService.addNeed(need3);
        helperService.addNeed(need4);

        ResponseEntity<Need[]> response = controller.searchNeeds("Macbook");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().length);
    }

    @Test
    void testSearchNeeds_IOEXCEPTION() throws IOException{
        FileCupboardDAO mockDao = mock(FileCupboardDAO.class);
        HelperService hs = new HelperService(mockDao);
        NotificationService ns = new NotificationService();
        CupboardController cc = new CupboardController(hs, ns);

        doThrow(new IOException("Simulated error"))
                .when(mockDao)
                .searchNeeds(anyString());

        ResponseEntity<Need[]> response = cc.searchNeeds("Phone");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetNeed_Valid() throws IOException{
        Need need1 = new Need(99L, "Laptop", 5, 1000.0);
        Need need2 = new Need(98L, "iPhone", 5, 1000.0);
        helperService.addNeed(need1);
        helperService.addNeed(need2);


        ResponseEntity<Need> response = controller.getNeed(98L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetNeed_NOTFOUND() throws IOException{
        Need need1 = new Need(99L, "Laptop", 5, 1000.0);
        Need need2 = new Need(98L, "iPhone", 5, 1000.0);
        helperService.addNeed(need1);
        helperService.addNeed(need2);

        ResponseEntity<Need> response = controller.getNeed(100L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testRemoveNeed_Valid() throws IOException{
        Need need1 = new Need(99L, "Laptop", 5, 1000.0);
        helperService.addNeed(need1);

        ResponseEntity<Object> response = controller.removeNeed(99L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testRemoveNeed_IOEXCEPTION() throws IOException{
        FileCupboardDAO mockDao = mock(FileCupboardDAO.class);
        HelperService hs = new HelperService(mockDao);
        NotificationService ns = new NotificationService();
        CupboardController cc = new CupboardController(hs, ns);

        doThrow(new IOException("Simulated error"))
                .when(mockDao)
                .deleteNeed(anyLong());

        ResponseEntity<Object> response = cc.removeNeed(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
