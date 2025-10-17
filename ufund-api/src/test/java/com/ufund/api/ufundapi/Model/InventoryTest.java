package com.ufund.api.ufundapi.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InventoryTest {

    private Inventory inventory; 
    private Need need1; 
    private Need need2; 

    @BeforeEach
    public void setUp(){
        inventory = new Inventory();
        need1 = new Need(2343L, "Backpack",7,15.43);
        need2 = new Need(32456L, "pencil case", 45, 9.34);

    }

    @Test
    public void testConstructorInitalFieldsCorrectly(){
        assertEquals(1L, inventory.getId());
        assertTrue(inventory.getInventory().isEmpty());
    }
    @Test
    public void testAddNeedAddItemToInventory(){
        inventory.addNeed(need1);
        assertEquals(1,inventory.getInventory().size());
        assertTrue(inventory.getInventory().contains(need1));
    }
    @Test
    public void testRemoveNeedRemovesItemsFromInventory(){
        inventory.addNeed(need2);
        inventory.removeNeed(need2);
        assertFalse(inventory.getInventory().contains(need2));

    }
    
    @Test
    public void testHasNeedByNameRetursnTrueIfExists(){
        inventory.addNeed(need1);
        assertTrue(inventory.hasNeedByName("Backpack"));
    }

    @Test
    public void testHasNeedByNameReturnsFalseIfNotFound(){
        assertFalse(inventory.hasNeedByName("Pens"));
    }

    @Test
    public void testGetNeedByNameReturnsMathcingNeeds(){
        inventory.addNeed(need1);
        inventory.addNeed(need2);

        List<Need> result = inventory.getNeedByName("case");
        assertEquals(1,result.size());
        assertEquals("pencil case", result.get(0).getName());
    }

    @Test
    public void testGetNeedByNameReturnsEmptyListIfNoMatch(){
        inventory.addNeed(need1);
        List<Need> result = inventory.getNeedByName("xyz");
        assertTrue(result.isEmpty());
    }
}
