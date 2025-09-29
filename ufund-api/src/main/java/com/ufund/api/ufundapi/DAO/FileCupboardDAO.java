package com.ufund.api.ufundapi.DAO;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.Model.Cupboard;
import com.ufund.api.ufundapi.Model.Need;

public class FileCupboardDAO implements CupBoardDAO {

    private final ObjectMapper objectMapper;
    private  File file;
    private Cupboard cupboard;

    @Value("${cupboard.filepath:data/cupboard.json}")
    private String filePath;

    public FileCupboardDAO(String filePath) throws IOException{
        this.objectMapper = new ObjectMapper();
        this.file = new File(filePath);
    }

    @Override
    public synchronized Need addNeed(Need need) throws IOException {
        if(needExistByName(need.getName())){
            throw new IllegalArgumentException("Need with this name already exists.");
        }
        List<Need> needs = cupboard.getInventory();
        long newID = needs.size() + 1;
        need.setId(newID);

        //add to cupboard and save
        cupboard.addNeed(need);

        return need; 
    }

    /**
     * Checks wheather a Need with the specified name exists in the cupboard inventory
     */
    @Override
    public boolean needExistByName(String name) {
       return cupboard.hasNeedByName(name);
    }

    


    
}
