package com.ufund.api.ufundapi.DAO;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.Controller.CupboardController;
import com.ufund.api.ufundapi.Model.Cupboard;
import com.ufund.api.ufundapi.Model.Need;

public class CupboardControllerTest {
    private CupboardController controller;
    private TestDAO dao;
//The test setup:cannot instantiate the CupboardDAO.
    class TestDAO implements CupboardDAO{
        private Cupboard cupboard = new Cupboard();

        @Override
        public Cupboard getCupboard(){
            return cupboard;
        }

        @Override
        public boolean deleteNeed(long id){
            List<Need> inventory = cupboard.getInventory();
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
    }
    
    @BeforeEach
    void setup(){
        dao = new TestDAO();
        controller = new CupboardController(dao);
    }
//The Tests:
    @Test
    void testGetCupboardEmpty() throws IOException{
        List<Need> result = controller.getCupboard().getBody();
        assertEquals(0, result.size());
    }

    @Test
    void testGetCupboardWithProducts() throws IOException{
        dao.getCupboard().addNeed(new Need(1L, "Pencils", 2, 3.0));
        dao.getCupboard().addNeed(new Need(2L, "Books", 1, 10.0));
        List<Need> result = controller.getCupboard().getBody();

        assertEquals(2, result.size());
        assertEquals("Pencils", result.get(0).getName());
        assertEquals("Books", result.get(1).getName());
    }   

    @Test
    void testDeleteNeedSuccessful() throws IOException{
        dao.getCupboard().addNeed(new Need(1L, "Pencils", 2, 3.0));
        assertEquals(204, controller.deleteNeed(1L).getStatusCodeValue()); //is there another way of doing this?
    }

    @Test
    void testDeleteNeedNotFound() throws IOException{
        assertEquals(404, controller.deleteNeed(99L).getStatusCodeValue()); //is there another way of doing this?
    }

}
