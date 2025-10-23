package com.ufund.api.ufundapi.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ManagerTest {

    @Test
    void testDefaultConstructor(){
        Manager manager = new Manager();
        assertNull(manager.getUsername());
        assertNull(manager.getPassword());
    }
    @Test
    void testParamterizedConstructor(){
        Manager manager = new Manager("Admin", "Lego1945");
        assertEquals("Admin", manager.getUsername());
        assertEquals("Lego1945", manager.getPassword());
    }
    @Test
    void testSetUsername(){
        Manager manager = new Manager();
        manager.setUsername("Kreekcraft");
        assertEquals("Kreekcraft", manager.getUsername());
    }

    @Test
    void testSetPassword(){
         Manager manager = new Manager();
         manager.setPassword("HomeBoy798");
         assertEquals("HomeBoy798", manager.getPassword());
    }

    @Test
    void testUpdateFields(){
         Manager manager = new Manager("Initailx" ,"InitalY");
         manager.setUsername("Update1");
         manager.setPassword("Update2");

         assertEquals("Update1", manager.getUsername());
         assertEquals("Update2", manager.getPassword());

    }


}
