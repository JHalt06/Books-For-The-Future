package com.ufund.api.ufundapi.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ufund.api.ufundapi.Service.ManagerService;

public class ManagerController {
    private final ManagerService managerService;

    public ManagerController(ManagerService managerService){
        this.managerService = managerService;
    }

    @PostMapping("/manager")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) { 
        boolean isValid = managerService.validateManagerLogin(username, password);

        if (isValid) {
            return ResponseEntity.ok("Manager login was successful - manager privileges granted.");
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid manager credentials");
         }
    }
}
