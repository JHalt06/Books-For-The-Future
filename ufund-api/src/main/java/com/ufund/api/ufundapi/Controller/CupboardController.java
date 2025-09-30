package com.ufund.api.ufundapi.Controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.DAO.CupboardDAO;
import com.ufund.api.ufundapi.Model.Need;


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
                String message = "Need with name '" + need.getName() + "' already exists";
                LOG.info(message);
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            Need createNeed = cupboardDAO.addNeed(need);
            String message2 ="Need cretaed successfully: " + createNeed.getName() + "(UD: " + createNeed.getId() + ")";
            LOG.info(message2);
            return new ResponseEntity<>(createNeed, HttpStatus.CREATED);
            
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(),e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/needs/{query}")
    public ResponseEntity<Object> searchNeeds(@RequestBody String query){
        LOG.info("GET /cupboard/needs/" + query);
        try {
            List<Need> needs = cupboardDAO.getNeedByName(query);
            if (!needs.isEmpty()) {
                return new ResponseEntity<>(needs, HttpStatus.OK);
            } else if (needs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                throw new IOException("Invalid search request.");
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(),e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
