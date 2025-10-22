package com.ufund.api.ufundapi.Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.DAO.FileCupboardDAO;
import com.ufund.api.ufundapi.DAO.FileInventoryDAO;
import com.ufund.api.ufundapi.Model.Need;
import com.ufund.api.ufundapi.Service.HelperService;

public class CupboardControllerTest {
    private CupboardController controller;
    private HelperService helperService;
    private File cupboardFile;
    private File inventoryFile;

    @BeforeEach
    void setup() throws IOException{
        cupboardFile = File.createTempFile("test-cupboard", ".json");
        inventoryFile = File.createTempFile("test-inventory", ".json");
        cupboardFile.deleteOnExit();
        inventoryFile.deleteOnExit();

        helperService = new HelperService(new FileCupboardDAO(cupboardFile.getAbsolutePath()), new FileInventoryDAO(inventoryFile.getAbsolutePath())); //???
        controller = new CupboardController(helperService);
    }

    @Test
    void testBrowseNeedsEmpty(){
        ResponseEntity<Object> response = controller.browseNeeds();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(((List<?>) response.getBody()).isEmpty()); //????
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

    // @Test
    // void testAddNeedToBasketSuccess(){

    //     Need need = new Need(1L, "Pens", 11, 2.0);
    //     helperService.addNeed(need);
    //     ResponseEntity<Need> response = controller.addNeedToBasket(1L); //Extension of HttpEntity that adds an HttpStatusCode status code. Used in RestTemplate as well as in @Controller methods.
    //     assertEquals(HttpStatus.CREATED, response.getStatusCode());
    //     assertEquals("Pens", ((Need) response.getBody()).getName());
    // }

    @Test
    void testAddNeedToBasketSuccess()throws IOException{
        Need need = new Need(1L, "Pens", 11,2.0);
        Need addedNeed = helperService.getInventoryDao().addNeed(need);
        ResponseEntity<Need> response = controller.addNeedToBasket(addedNeed.getId());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Pens", response.getBody().getName());
    }
    

    @Test
    void testAddNeedToBasketConflict() throws IOException{
        Need need = new Need(1L, "Pens", 11, 2.0);
        helperService.addNeed(need);
        helperService.addNeedToBasket(1L); //moves to inventory
        ResponseEntity<Need> response = controller.addNeedToBasket(1L); //Extension of HttpEntity that adds an HttpStatusCode status code. Used in RestTemplate as well as in @Controller methods.
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testRemoveNeedFromBasketNotFound(){
        ResponseEntity<Void> response = controller.removeNeedFromBasket(99L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testRemoveNeedFromBasketValid(){
        ResponseEntity<Void> response = controller.removeNeedFromBasket(99L);
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
        Need need = new Need(null,"Laptop", 5, 1000.0);
        Need addedNeed = helperService.addNeed(need);

        Need updatedNeed = new Need(addedNeed.getId(), "Laptop Pro", 10,1200.0);
        ResponseEntity<Object> response = controller.updateNeed(updatedNeed);
        Need returnedNeed = (Need) response.getBody();
        assertNotNull(returnedNeed);
        assertEquals("Laptop Pro", returnedNeed.getName());
        assertEquals(10,returnedNeed.getquantity());
        assertEquals(1200.0, returnedNeed.getFundingAmount());


    }








}
