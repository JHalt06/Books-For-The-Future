package com.ufund.api.ufundapi.Controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.DAO.CupboardDAO;
import com.ufund.api.ufundapi.Model.Cupboard;
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

    @PutMapping("/needs/{id}")
    public ResponseEntity<Object> updateNeed(@PathVariable long id, @RequestBody Need updatedNeed){
        LOG.info("PUT /cupboard/needs/" + id);
        Need old = cupboardDAO.getNeedByID(String.valueOf(id));
        if (old != null) {
            if (updatedNeed.getName() != null) {
                old.setName(updatedNeed.getName());
            }
            if (updatedNeed.getquantity() > 0) {
                old.setquantity(updatedNeed.getquantity());
            }
            if (updatedNeed.getFundingAmount() > 0) {
                old.setFundingAmount(updatedNeed.getFundingAmount());
            }

            if (cupboardDAO.updateNeed(updatedNeed)) {
                return new ResponseEntity<>(old, HttpStatus.OK);
            } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/needs")
    public ResponseEntity<Object> searchNeeds(@RequestParam String q){
        LOG.info("GET /cupboard/needs/?q=" + q);
        try {
            List<Need> needs = cupboardDAO.getNeedByName(q);
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

    @GetMapping("/needs/{id}")
    public ResponseEntity<Object> getNeed(@PathVariable String id){
        LOG.info("GET /cupboard/needs/" + id);
        Need need = cupboardDAO.getNeedByID(id);
        if (need != null) {
            return new ResponseEntity<>(need, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Returns all products in the cupboard as a list
     * @throws IOException if an error occurs while getting the cupboard, getCupboard()
     */
    @GetMapping("/cupboard")
    public ResponseEntity<List<Need>> getCupboard() throws IOException{
    //Fetch all cupboard items (needs)
        Cupboard cupboard = cupboardDAO.getCupboard(); //fetch
        List<Need> products = cupboard.getInventory();//get the List<Need> directly
        return ResponseEntity.ok(products); //returns 200, even if it is an empty list. The fetch was successful
    }


    /**
     * Deletes a need by the ID of the product to delete
     * @throws IOException if an error occurs while deleting the need.
     */
    @DeleteMapping("/cupboard/{id}") //{id} is a path variable, Spring will extract this part of the URL and pass it into the method
    public ResponseEntity<Void> deleteNeed(@PathVariable long id) throws IOException{
        boolean deleted = cupboardDAO.deleteNeed(id);
        if(deleted){
            return ResponseEntity.noContent().build(); //204 No Content, this means that the id was found and is now deleted, delete successful.
        }
        else{
            return ResponseEntity.notFound().build(); //404 Not Found, could not find that id. That id does not exist
        }
    }

}
