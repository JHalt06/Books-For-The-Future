package com.ufund.api.ufundapi.DAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.Controller.InventoryController;
import com.ufund.api.ufundapi.Model.Inventory;
import com.ufund.api.ufundapi.Model.Need;

public class InventoryControllerTest {
    private InventoryController controller;
    private TestDAO dao;
//The test setup:cannot instantiate the InventoryDAO.
    class TestDAO implements InventoryDAO{
        private Inventory inventory = new Inventory();

        @Override
        public Inventory getInventory(){
            return inventory;
        }

        @Override
        public boolean deleteNeed(long id){
            List<Need> inventory = this.inventory.getInventory();
            for (Need n : new ArrayList<>(inventory)){
                if(n.getId() != null && n.getId().equals(id)){
                    inventory.remove(n);
                    return true;
                }
            }
            return false;
        }

        @Override
        public Need addNeed(Need need) throws IOException {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'addNeed'");
        }

        @Override
        public boolean needExistByName(String name) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'needExistByName'");
        }

        @Override
        public List<Need> getNeedByName(String name) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getNeedByName'");
        }

        @Override
        public Need getNeedByID(String id) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean updateNeed(Need updatedNeed) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'updateNeed'");
        }

    }
    
    @BeforeEach
    void setup(){
        dao = new TestDAO();
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
        dao.getInventory().addNeed(new Need(1L, "Pencils", 2, 3.0));
        dao.getInventory().addNeed(new Need(2L, "Books", 1, 10.0));
        List<Need> result = controller.getInventory().getBody();

        assertEquals(2, result.size());
        assertEquals("Pencils", result.get(0).getName());
        assertEquals("Books", result.get(1).getName());
    }   

    @Test
    void testDeleteNeedSuccessful() throws IOException{
        dao.getInventory().addNeed(new Need(1L, "Pencils", 2, 3.0));
        assertEquals(204, controller.deleteNeed(1L).getStatusCodeValue()); //is there another way of doing this?
    }

    @Test
    void testDeleteNeedNotFound() throws IOException{
        assertEquals(404, controller.deleteNeed(99L).getStatusCodeValue()); //is there another way of doing this?
    }

}
