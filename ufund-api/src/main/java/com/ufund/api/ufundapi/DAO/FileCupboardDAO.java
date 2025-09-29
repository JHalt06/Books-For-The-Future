package com.ufund.api.ufundapi.DAO;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.Model.Cupboard;
import com.ufund.api.ufundapi.Model.Need;

public class FileCupboardDAO implements CupboardDAO {

    private final ObjectMapper objectMapper;
    private  final File file;
    private Cupboard cupboard;

    /**
     * 
     * @param filePath Path to the JSON file use for persistance. 
     * @throws IOException if an error occurs during file read/write 
     */

    public FileCupboardDAO(@Value("${cupboard.filepath:data/cupboard.json}")String filePath) throws IOException{
        this.objectMapper = new ObjectMapper();
        this.file = new File(filePath);
        loadCupboard();
    }

    /**
     * Adds a new need to the cupboard inventory if it doesnt already exists 
     * Assigns a new ID and persist the change to file. 
     * @param need the Need object to add.
     * @return the created Need with assigned ID.
     * @throws IOException if the cupboard cannot be saved. 
     * @Throws IllegalArgumentException if a Need with same name already exists. 
     */
    @Override
    public synchronized Need addNeed(Need need) throws IOException {
        if(needExistByName(need.getName())){
            throw new IllegalArgumentException("Need with this name already exists.");
        }
        //assign new ID(incremntal)
        List<Need> needs = cupboard.getInventory();
        long newID = needs.size() + 1;
        need.setId(newID);

        //add to cupboard and save
        cupboard.addNeed(need);
        saveCupboard();

        return need; 
    }

    /**
     * Checks wheather a Need with the specified name exists in the cupboard inventory
     * @param name the name of the Need to check
     * @return true if a need with the same name exist, false otherwise. 
     */
    @Override
    public boolean needExistByName(String name) {
       return cupboard.hasNeedByName(name);
    }

    /**
     * Loads the cupboard data from the file, or initialize a new cupboard if the files doesn't exist
     * @throws IOExceptiom if an error occurs while reading the file 
     */
    private void loadCupboard() throws IOException{
        if(file.exists()){
            this.cupboard = objectMapper.readValue(file, Cupboard.class);
        }
        else{
            this.cupboard = new Cupboard();
            saveCupboard();
            
        }
    }

    /**
     * Saves the current cupboard data to a JSON file.
     * @throws IOException if an error occurs while writng the file. 
     */
    private void saveCupboard() throws IOException{
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, cupboard);
    }



    


    
}
