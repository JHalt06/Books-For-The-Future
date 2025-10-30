package com.ufund.api.ufundapi.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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
        if (helperService.removeNeed(need) == true) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/needs/{id}")
    public ResponseEntity<Object> removeNeed(@PathVariable long id){
        LOG.info("DELETE /needs/" + id);
        try {
            if (helperService.getCupboardDao().deleteNeed(id)) {
                return ResponseEntity.noContent().build();
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/needs")
    public ResponseEntity<Object> browseNeeds(){
        LOG.info("GET /cupboard/needs");
        if (helperService.getNeeds() != null) {
            return new ResponseEntity<>(helperService.getNeeds(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/?name={q}")
    public ResponseEntity<Need[]> searchNeeds(@RequestParam String q){
        LOG.info("GET /cupboard/needs/?name=" + q);
        try {
            Need[] needs = helperService.getCupboardDao().searchNeeds(q);
            if (needs.length != 0) {
                return new ResponseEntity<>(needs, HttpStatus.OK);
            } else if (needs.length == 0) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                throw new IOException("Invalid search request.");
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(),e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/checkout")
    public ResponseEntity<Object> checkoutNeed(@RequestBody List<Map<String,Integer>> needs){
        LOG.info("PUT /cupboard/checkout" + needs);
        try {
            // for (Map<String, Integer> map : needs) {
            //     int id = map.get("needID");
            //     if (helperService.getCupboardDao().getNeedByID(id) == null) {
            //         return new ResponseEntity<>("One or more needs are invalid, please refresh.", HttpStatus.BAD_REQUEST);
            //     }
            // }
            for (Map<String, Integer> map : needs) {
                int id = map.get("needID");

                if (helperService.getCupboardDao().getNeedByID(id) == null) {
                    return new ResponseEntity<>("One or more needs are invalid, please refresh.", HttpStatus.BAD_REQUEST);
                }

                int needID = map.get("needID");
                int checkoutAmount = map.get("quantity");
                System.out.println("checking out need " + needID + " " + checkoutAmount);
                helperService.checkoutNeed(needID, checkoutAmount);
                }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            LOG.log(Level.WARNING, e.getLocalizedMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalAccessException e) {
            LOG.log(Level.WARNING, e.getLocalizedMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/need/update")
    public ResponseEntity<Object> updateNeed(@RequestBody Need updateNeed){
        LOG.info("POST /cupboard/need/update" + updateNeed);

        try {
            boolean updated = helperService.updateNeed(updateNeed);
            System.out.println(helperService.getNeeds());
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

    @PutMapping("/needs/{id}")
    public ResponseEntity<Object> updateNeedById(@PathVariable long id, @RequestBody Need need){
        LOG.info("PUT /needs/" + id);
        try {
            boolean updated = helperService.updateNeed(need);
            if(updated){
                return new ResponseEntity<>(need, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/need/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable long id){
        LOG.info("GET /needs/" + id);
        Need need = helperService.getCupboardDao().getNeedByID(id);
        if (need != null) {
            System.out.println(need);
            return new ResponseEntity<>(need, HttpStatus.OK);
        } else {
            System.out.println("need not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
