package com.ufund.api.ufundapi.DAO;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.Controller.InventoryController;
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
        tempfile = File.createTempFile("test-cupboard", ".json");
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
        assertEquals(204, controller.deleteNeed(1L).getStatusCode().value()); //is there another way of doing this?
    }

    @Test
    void testDeleteNeedNotFound() throws IOException{
        assertEquals(404, controller.deleteNeed(99L).getStatusCode().value()); //is there another way of doing this?
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

}
