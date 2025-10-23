package com.ufund.api.ufundapi.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class FundingBasketTest {
    
    @Test
    void testDefaultConstructor(){
        FundingBasket basket = new FundingBasket();
        assertNull(basket.getHelperName());
        assertNotNull(basket.getNeeds());
        assertTrue(basket.getNeeds().isEmpty());
        assertFalse(basket.isFinalized());
    }
    
    @Test
    void testConstructorWithHelperNameAndNeeds(){
        List<Need> intialNeeds = new ArrayList<>();
        intialNeeds.add(new Need(1L, "Pens", 10, 0));
        intialNeeds.add(new Need(2L, "Pencils", 5, 0));
        
        FundingBasket basket = new FundingBasket("Helper1", intialNeeds);

        assertEquals("Helper2", basket.getHelperName());
        assertEquals(2, basket.getNeeds().size());
        assertEquals("Pens", basket.getNeeds().get(0).getName());
        assertEquals("Pencils", basket.getNeeds().get(1).getName());
    }

    @Test
    void testSettersAndGetters(){
        FundingBasket basket = new FundingBasket();
        basket.setHelperName("Helper3");
        assertEquals("Helper3", basket.getHelperName());

        List<Need> needs = new ArrayList<>();
        needs.add(new Need(3L, "Notebooks", 20, 0));
        basket.setNeeds(needs);
        assertEquals(1, basket.getNeeds().size());
        assertEquals("Notebooks", basket.getNeeds().get(0).getName());

        basket.setFinalized(true);
        assertTrue(basket.isFinalized());
    }

    @Test
    void testAddNeed(){
        FundingBasket basket = new FundingBasket();
        Need n1 = new Need(1L, "Pens", 10, 0);
        Need n2 = new Need(2L, "Pencils", 5, 0);
        basket.addNeed(n1);
        basket.addNeed(n2);

        basket.removeNeed(1L);
        assertEquals(1, basket.getNeeds().size());
        assertEquals(2L, basket.getNeeds().get(0).getId());

        //try remving non exisitent id should not throw
        basket.removeNeed(99L);
        assertEquals(1, basket.getNeeds().size());

    }
}
