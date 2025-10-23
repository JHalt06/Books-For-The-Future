package com.ufund.api.ufundapi.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.Service.ManagerService;

public class ManagerControllerTest {
    private ManagerController controller;

    @BeforeEach
    void setup(){
        ManagerService studService = new ManagerService(){
            @Override    
            public boolean validateManagerLogin(String username, String password){
                return "manager".equals(username) && "pass123".equals(password);

            }
        };
        controller = new ManagerController(studService);
    }

    @Test
    void testLogin_Success(){
        ResponseEntity<String> response = controller.login("manager", "pass123");
        assertEquals(200, response.getStatusCodeValue()); //should work
        assertEquals("Manager login was successful - manager privileges granted.", response.getBody()); //check the right message is sent
    }

    @Test
    void testLogin_Failure(){
        ResponseEntity<String> response = controller.login("manager", "wrongpassword");
        assertEquals(401, response.getStatusCodeValue()); //should work
        assertEquals("Invalid manager credentials", response.getBody()); //check the right message is sent
    }
}

