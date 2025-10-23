package com.ufund.api.ufundapi.Controller;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ufund.api.ufundapi.DAO.FundingBasketFileDAO;
import com.ufund.api.ufundapi.Model.FundingBasket;
import com.ufund.api.ufundapi.Model.Need;

public class FundingBasketController {
    private final FundingBasketFileDAO basketDAO;

    public FundingBasketController(FundingBasketFileDAO basketDAO){
        this.basketDAO = basketDAO;
    }

    @GetMapping("/{helper}")
    public ResponseEntity<FundingBasket> getBasket(@PathVariable String helper){
        FundingBasket basket = basketDAO.getBasket(helper);
        return ResponseEntity.ok(basket);
    }

    @PostMapping("/{helper}/add")
    public ResponseEntity<FundingBasket> addNeed(
        @PathVariable String helper,
        @RequestBody Need need
    ) throws IOException{
        FundingBasket updated = basketDAO.addNeed(helper, need);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{helper}/remove.{id}") //Remove a need from basket
    public ResponseEntity<FundingBasket> removeNeed(
        @PathVariable String helper,
        @PathVariable Long id
    ) throws IOException { 
        FundingBasket updated = basketDAO.removeNeed(helper, id);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{helper}/finalize")
    public ResponseEntity<String> finalizeBasket(@PathVariable String helper) throws IOException{
        boolean success = basketDAO.finalizeBasket(helper);
        if (success){
            return ResponseEntity.ok("Basket confirmed successfully");
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to finalize basket");
        }
    }

}
