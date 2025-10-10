package com.ufund.api.ufundapi.Controller;

import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
        if (helperService.addNeed(need) != null) {
            return new ResponseEntity<>(need, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/need")
    public ResponseEntity<Object> removeNeed(@RequestBody Need need){
        LOG.info("DELETE /cupboard/need" + need);
        if (helperService.removeNeed(need) == true) {
            return ResponseEntity.noContent().build();
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
}
