package com.ufund.api.ufundapi.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.DAO.CupboardDAO;

@RestController
@RequestMapping("/cupboard")
public class CupboardContoller {

    //Constructor injection of DAO
    private final CupboardDAO cupboardDAO;

    public CupboardContoller(CupboardDAO cupboardDAO) {
        this.cupboardDAO = cupboardDAO;
    }
    


    public ResponseEntity<Object> createNeed(){
        
    }
}
