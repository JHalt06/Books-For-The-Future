package com.ufund.api.ufundapi.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class NeedTest {
    @Test
    public void testConstructorWithId(){
        Need need = new Need(1L, "Notebooks", 11, 5.0);

        assertEquals(1L, need.getId());
        assertEquals("Notebooks", need.getName());
        assertEquals(11, need.getquantity());
        assertEquals(5.0, need.getFundingAmount());
    }

    @Test
    public void testConstructorWithNoId(){
        Need need = new Need("Notepads", 22, 3.5);
        assertNull(need.getId()); //assertNull: tests if the needs ID is Null
        assertEquals("Notepads", need.getName());
        assertEquals(22, need.getquantity());
        assertEquals(3.5, need.getFundingAmount());
    }

    @Test
    public void testSettersAndGetters(){ //Tests to make sure the setters can update a Needs info
        Need need = new Need();
        need.setId(2L);
        need.setName("Paper");
        need.setquantity(900);
        need.setFundingAmount(12.75);

        assertEquals(2L, need.getId());
        assertEquals("Paper", need.getName());
        assertEquals(900, need.getquantity());
        assertEquals(12.75, need.getFundingAmount());

    }

}
