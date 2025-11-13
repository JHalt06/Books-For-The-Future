package com.ufund.api.ufundapi.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

public class NeedTest {
    @Test
    public void testDefaultConstructor(){
        Need need = new Need();
        assertNull(need.getName());
        assertNull(need.getId());
        assertEquals(0, need.getFundingAmount());
        assertEquals(0, need.getquantity());
    }

    @Test
    public void testConstructorWithID(){
        Need need = new Need(32465L, "Notebooks", 45, 10.32);
        assertEquals(32465L, need.getId());
        assertEquals("Notebooks", need.getName());
        assertEquals(45, need.getquantity());
        assertEquals(10.32, need.getFundingAmount());
    }

    @Test
    public void testConsturctorWithoutID(){
        Need need = new Need("Backpack", 57, 12.45);
        assertEquals("Backpack", need.getName());
        assertEquals(12.45, need.getFundingAmount());
        assertEquals(57, need.getquantity());
        assertNull(need.getId());
    }


    @Test
    public void testGetterandSetters(){
        Need need = new Need();
        need.setId(100L);
        need.setName("Pencils");
        need.setquantity(15);
        need.setFundingAmount(12.15);

        assertEquals(100L, need.getId());
        assertEquals("Pencils", need.getName());
        assertEquals(15, need.getquantity());
        assertEquals(12.15, need.getFundingAmount());
    }
}
