package com.ufund.api.ufundapi.DAO;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.Model.FundingBasket;
import com.ufund.api.ufundapi.Model.Need;

@Repository
public class FundingBasketFileDAO {
    private final Map<String, FundingBasket> baskets = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File file;

    public FundingBasketFileDAO(@Value("${baskets.file:data/baskets.json}") String filename) throws IOException{
        this.file = new File(filename);
        load();
    }

    //load all baskets from JSON file if they exist
    private void load() throws IOException{
        if (file.exists() && file.length() > 0){
            FundingBasket[] loaded = objectMapper.readValue(file, FundingBasket[].class);
            for (FundingBasket b : loaded){
                baskets.put(b.getHelperName(), b);
            }
        }
    }

    //sabe all baskets to a JSON file
    private void save() throws IOException{
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, baskets.values());
    }

    //get the basket for a specific helper, make it if doesn't exist
    public FundingBasket getBasket(String helperName) {
        return baskets.computeIfAbsent(helperName, FundingBasket::new); //If the specified key is not already associated with a value (or is mapped to null), attempts to compute its value using the given mapping function and enters it into this map unless null (optional operation).
    }

    
    public FundingBasket addNeed(String helperName, Need need) throws IOException{
        FundingBasket basket = getBasket(helperName);
        basket.addNeed(need);
        baskets.put(helperName, basket);
        save();
        return basket;
    }
    
    public FundingBasket removeNeed(String helperName, Long id) throws IOException {
        FundingBasket basket = getBasket(helperName);
        basket.removeNeed(id);
        baskets.put(helperName, basket);
        save();
        return basket;
    }

    public boolean finalizeBasket(String helperName) throws IOException {
        FundingBasket basket = getBasket(helperName);
        basket.setFinalized(true);
        save();
        return true;
    }

}
