package com.ufund.api.ufundapi.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ManagerTest {
    
    @Test
    void testDefaultConstructor(){
        Manager m = new Manager();
        assertNull(m.getUsername());
        assertNull(m.getPassword());
    }

    @Test
    void testParameterizedConstructor(){
        Manager m = new Manager("admin", "pass123");
        assertEquals("admin", m.getUsername());
        assertEquals("pass123", m.getPassword());
    }

    @Test
    void testSetUsernameAndGetUsername(){
        Manager m = new Manager();
        m.setUsername("user1");
        assertEquals("user1", m.getUsername());
    }

    @Test
    void testSetPasswordAndGetPassword(){
        Manager m = new Manager();
        m.setPassword("secret");
        assertEquals("secret", m.getPassword());
    }
}
