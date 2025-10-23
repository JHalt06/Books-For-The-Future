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

import com.ufund.api.ufundapi.DAO.InventoryDAO;
import com.ufund.api.ufundapi.Model.Inventory;
import com.ufund.api.ufundapi.Model.Need;


@RestController
@RequestMapping("")
public class InventoryController {

    //Constructor injection of DAO

    private final InventoryDAO inventoryDAO;
    private static final Logger LOG = Logger.getLogger(InventoryController.class.getName());

    public InventoryController(InventoryDAO inventoryDAO) {
        this.inventoryDAO = inventoryDAO;
    }
    

    @PostMapping("/need")
    public ResponseEntity<Object> createNeed(@RequestBody Need need){

        LOG.info("POST /inventory/need " + need);
        try {
            //Check if need exist 
            if(inventoryDAO.needExistByName(need.getName())){
                String message = "Need with name '" + need.getName() + "' already exists";
                LOG.info(message);
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            Need createNeed = inventoryDAO.addNeed(need);
            String message2 ="Need created successfully: " + createNeed.getName() + "(UD: " + createNeed.getId() + ")";
            LOG.info(message2);
            return new ResponseEntity<>(createNeed, HttpStatus.CREATED);
            
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(),e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/inventory/needs/{id}")
    public ResponseEntity<Object> updateNeed(@PathVariable long id, @RequestBody Need updatedNeed){
        LOG.info("PUT /inventory/needs/" + id);
        if (inventoryDAO.updateNeed(updatedNeed)) {
            return new ResponseEntity<>(updatedNeed, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/needs")
    public ResponseEntity<List<Need>> searchNeeds(@RequestParam String q){
        LOG.info("GET /inventory/needs/?q=" + q);
        try {
            List<Need> needs = inventoryDAO.getNeedByName(q);
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

    @GetMapping("/inventory/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable long id){
        LOG.info("GET /inventory/" + id);
        Need need = inventoryDAO.getNeedByID(id);
        if (need != null) {
            System.out.println(need);
            return new ResponseEntity<>(need, HttpStatus.OK);
        } else {
            System.out.println("need not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Returns all products in the inventory as a list
     * @throws IOException if an error occurs while getting the inventory, getInventory()
     */
    @GetMapping("/inventory")
    public ResponseEntity<List<Need>> getInventory() throws IOException{
    //Fetch all inventory items (needs)
        Inventory inventory = inventoryDAO.getInventory(); //fetch
        List<Need> products = inventory.getInventory();//get the List<Need> directly
        return ResponseEntity.ok(products); //returns 200, even if it is an empty list. The fetch was successful
    }


    /**
     * Deletes a need by the ID of the product to delete
     * @throws IOException if an error occurs while deleting the need.
     */
    @DeleteMapping("/inventory/{id}") //{id} is a path variable, Spring will extract this part of the URL and pass it into the method
    public ResponseEntity<Void> deleteNeed(@PathVariable long id) throws IOException{
        LOG.info("DELETE /cupboard/inventory/" + id);
        boolean deleted = inventoryDAO.deleteNeed(id);
        if(deleted){
            return ResponseEntity.noContent().build(); //204 No Content, this means that the id was found and is now deleted, delete successful.
        }
        else{
            return ResponseEntity.notFound().build(); //404 Not Found, could not find that id. That id does not exist
        }
    }

}
