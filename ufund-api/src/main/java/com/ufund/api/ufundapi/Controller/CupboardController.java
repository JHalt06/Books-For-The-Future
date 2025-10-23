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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.Model.Need;
import com.ufund.api.ufundapi.Service.HelperService;

@RestController
@RequestMapping("/cupboard")
public class CupboardController {
    private final HelperService helperService;
    private static final Logger LOG = Logger.getLogger(CupboardController.class.getName());

    public CupboardController(HelperService helperService) {
        this.helperService = helperService;
    }

    @PostMapping("/need")
    public ResponseEntity<Object> createNeed(@RequestBody Need need){
        LOG.info("POST /cupboard/need" + need);
       
        if(helperService.getCupboardDao().needExistByName(need.getName())){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Need addedNeed = helperService.addNeed(need);
        if(addedNeed != null){
            return new ResponseEntity<>(addedNeed, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            
       
    }

    @DeleteMapping("/need")
    public ResponseEntity<Object> removeNeed(@RequestBody Need need){
        LOG.info("DELETE /cupboard/need" + need);
        
        try {
            boolean removed = helperService.removeNeed(need);
            LOG.info("HelperServiceremoveNeed returned: " + removed);
            if(removed){
                return ResponseEntity.noContent().build();

            }
            else{
                return ResponseEntity.notFound().build();
            }
            // if (helperService.removeNeed(need) == true) {
            //     return ResponseEntity.noContent().build();
            // }
            // return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Exception removing need: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/needs")
    public ResponseEntity<Object> browseNeeds(){
        LOG.info("GET /cupboard/needs");
        if (helperService.getNeeds() != null) {
            return new ResponseEntity<>(helperService.getNeeds(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/needs/{id}")
    public ResponseEntity<Need> addNeedToBasket(@PathVariable long id){
        LOG.info("POST /cupboard/needs/" + id);
        try {
            Need newNeed = helperService.addNeedToBasket(id);
            if (newNeed != null){
                // check if need exists in basket
                // returns need if successful
                if (helperService.getCupboardDao().getNeedByID(String.valueOf(id)) != null && helperService.getInventoryDao().getNeedByID(String.valueOf(id)) == null){
                    return new ResponseEntity<>(newNeed, HttpStatus.CREATED);
                }
            }
            // if need exists there is a conflict
            if(helperService.getCupboardDao().getNeedByID(String.valueOf(id)) != null){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e){
            LOG.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/needs/search")
    public ResponseEntity<List<Need>> searchNeedsByName(@RequestParam String name) {
        LOG.info("GET /cupboard/needs/search?name=" + name);
        List<Need> needs = helperService.searchNeedsByName(name);
        return new ResponseEntity<>(needs, HttpStatus.OK);
    }

    @DeleteMapping("/needs/{id}")
    public ResponseEntity<Void> removeNeedFromBasket(@PathVariable long id){
        LOG.info("DELETE /cupboard/needs/" + id);
        try {
            boolean removed = helperService.removeNeedFromBasket(id);
            if (removed) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/need/update")
    public ResponseEntity<Object> updateNeed(@RequestBody Need updateNeed){
        LOG.info("POST /cupboard/need/update" + updateNeed);

        try {
            boolean updated = helperService.updateNeed(updateNeed);
            if(updated){
                return new ResponseEntity<>(updateNeed, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
