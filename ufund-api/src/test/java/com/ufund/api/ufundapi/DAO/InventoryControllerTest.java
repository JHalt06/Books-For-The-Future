package com.ufund.api.ufundapi.DAO;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.Controller.InventoryController;
import com.ufund.api.ufundapi.DAO.InventoryDAO;
import com.ufund.api.ufundapi.Model.Need;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class InventoryControllerTest {
    private InventoryController controller;
    private InventoryDAO dao;
    private File tempfile;

    @BeforeEach
    void setup() throws IOException {
        tempfile = File.createTempFile("test-cupboard", ".json"); //is this supposed to be "test-inventory?"
        tempfile.deleteOnExit(); //Auto delete after test

        dao = new FileInventoryDAO(tempfile.getAbsolutePath());
        controller = new InventoryController(dao);
    }
//The Tests:
    @Test
    void testGetInventoryEmpty() throws IOException{
        List<Need> result = controller.getInventory().getBody();
        assertEquals(0, result.size());
    }

    @Test
    void testGetInventoryWithProducts() throws IOException{
        dao.addNeed(new Need(1L, "Pencils", 2, 3.0));
        dao.addNeed(new Need(2L, "Books", 1, 10.0));
        List<Need> result = controller.getInventory().getBody();

        assertEquals(2, result.size());
        assertEquals("Pencils", result.get(0).getName());
        assertEquals("Books", result.get(1).getName());
    }   

    @Test
    void testDeleteNeedSuccessful() throws IOException{
        dao.addNeed(new Need(1L, "Pencils", 2, 3.0));
        assertEquals(204, controller.deleteNeed(1L).getStatusCode().value());
    }

    @Test
    void testDeleteNeedNotFound() throws IOException{
        assertEquals(404, controller.deleteNeed(99L).getStatusCode().value());
    }

    @Test
    void testCreateNewNeed() throws IOException {
        HttpStatusCode createStatus = controller.createNeed(new Need(1L, "Pencils", 2, 3.0)).getStatusCode();
        assertEquals(HttpStatus.CREATED, createStatus);
        List<Need> needs = controller.getInventory().getBody();
        assertEquals(1, needs.size());
    }

    @Test
    void testCreateExistingNeed() throws IOException {
        controller.createNeed(new Need(1L, "Pencils", 2, 3.0));
        HttpStatusCode createStatus = controller.createNeed(new Need(2L, "Pencils", 2, 3.0)).getStatusCode();
        assertEquals(HttpStatus.CONFLICT, createStatus);
        List<Need> needs = controller.getInventory().getBody();
        assertEquals(1, needs.size());
    }

    @Test
    void testUpdateNeed() throws IOException {
        Need need = new Need(1L, "Pencils", 2, 3.0);
        dao.addNeed(need);
        need.setName("Pens");
        HttpStatusCode updateStatus = controller.updateNeed(1L, need).getStatusCode();
        List<Need> needs = controller.getInventory().getBody();
        assertEquals(HttpStatus.OK, updateStatus);
        Need changedNeed = needs.getFirst();
        assertEquals("Pens", changedNeed.getName());
    }

    @Test
    void testUpdateNeedNotFound(){
        assertEquals(HttpStatus.NOT_FOUND, controller.updateNeed(99L,
                new Need(99L, "Pencils", 2, 3.0)).getStatusCode());
    }

    @Test
    void testSearchNeedsNotFound(){
        assertEquals(HttpStatus.NOT_FOUND, controller.searchNeeds("Pens").getStatusCode());
    }

    @Test
    void testSearchNeeds() throws IOException {
        Need need = new Need(1L, "Pencils", 2, 3.0);
        dao.addNeed(need);
        ResponseEntity<List<Need>> searchResult = controller.searchNeeds("P");
        assertEquals(HttpStatus.OK, searchResult.getStatusCode());
        assertEquals(1, searchResult.getBody().size());
        assertEquals("Pencils", searchResult.getBody().getFirst().getName());

    }

    @Test
    void testGetNeedNotFound(){
        assertEquals(HttpStatus.NOT_FOUND, controller.getNeed(99).getStatusCode());
    }

    @Test
    void testGetNeed() throws IOException {
        Need need = new Need(1L, "Pencils", 2, 3.0);
        dao.addNeed(need);
        ResponseEntity<Need> result = controller.getNeed(1);
        assertEquals(HttpStatus.OK,result.getStatusCode());
        assertEquals(need, result.getBody());
    }
}
