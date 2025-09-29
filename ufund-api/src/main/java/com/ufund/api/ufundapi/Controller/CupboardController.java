package com.ufund.api.ufundapi.Controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.DAO.CupboardDAO;
import com.ufund.api.ufundapi.Model.Need;

import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/cupboard")
public class CupboardController {

    //Constructor injection of DAO
    private final CupboardDAO cupboardDAO;
    private static final Logger LOG = Logger.getLogger(CupboardController.class.getName());

    public CupboardController(CupboardDAO cupboardDAO) {
        this.cupboardDAO = cupboardDAO;
    }
    

    @PostMapping("/need")
    public ResponseEntity<Object> createNeed(@RequestBody Need need){

        LOG.info("POST /cupboard/need " + need);
        try {
            //Check if need exist 
            if(cupboardDAO.needExistByName(need.getName())){
                LOG.info("Need with name '" + need.getName() + "' already exists");
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            Need createNeed = cupboardDAO.addNeed(need);
            
            return new ResponseEntity<>(createNeed, HttpStatus.CREATED);
            
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(),e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
