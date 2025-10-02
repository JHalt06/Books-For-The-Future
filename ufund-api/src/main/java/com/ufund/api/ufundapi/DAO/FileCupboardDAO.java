package com.ufund.api.ufundapi.DAO;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.Model.Cupboard;
import com.ufund.api.ufundapi.Model.Need;

@Repository
public class FileCupboardDAO implements CupboardDAO {

    private final ObjectMapper objectMapper;
    private  final File file;
    private Cupboard cupboard;

    @Autowired
    public FileCupboardDAO(ObjectMapper objectMapper,
        @Value("${cupboard.filepath:data/cupboard.json}") String filePath) throws IOException {
        this.objectMapper = objectMapper;
        this.file = new File(filePath);
        loadCupboard();
    }

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
     * Purpose of Unit test 
     * @param filePath Path to the JSON file use for persistance. 
     * @param objectMapper reading the JSON file 
     * @throws IOException if an error occurs during file read/write 
     */
    public FileCupboardDAO(String filePath,ObjectMapper objectMapper) throws IOException{
        this.objectMapper = objectMapper;
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
        //assign new ID(incremental)
        List<Need> needs = cupboard.getInventory();
        long newID = needs.stream().mapToLong(Need::getId).max().orElse(0L) +1;
        need.setId(newID);

        //add to cupboard and save
        cupboard.addNeed(need);
        saveCupboard();

        System.out.println("New need added: " + need.getName() + "(ID " +
            need.getId() +" )");
        System.out.println("Total items in inventory: " + cupboard.getInventory().size());

        return need; 
    }

    /**
     * Checks whether a Need with the specified name exists in the cupboard inventory
     * @param name the name of the Need to check
     * @return true if a need with the same name exist, false otherwise. 
     */
    @Override
    public boolean needExistByName(String name) {
        return cupboard.hasNeedByName(name);
    }

    /**
     * Checks wheather a Need containing the specified query exists in the cupboard inventory
     * @param query the query to search for needs with
     * @return the list of needs if any exist, else an empty list.
     */
    @Override
    public List<Need> getNeedByName(String name) {
        return cupboard.getNeedByName(name);
    }

    /**
     * Loads the cupboard data from the file, or initialize a new cupboard if the files doesn't exist
     * @throws IOException if an error occurs while reading the file 
     */
    private void loadCupboard() throws IOException{
        if(file.exists() && file.length() > 0){
            this.cupboard = objectMapper.readValue(file, Cupboard.class);
            System.out.println("Cupboard loaded from file: " + file.getPath());
        }
        else{
            this.cupboard = new Cupboard();
            saveCupboard();
            System.out.println("New Cupboard created and save:  " + file.getPath());
            
        }
    }

    /**
     * Saves the current cupboard data to a JSON file.
     * @throws IOException if an error occurs while writing the file. 
     */
    private void saveCupboard() throws IOException{
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, cupboard);
    }


    /**
     * Returns the latest cupboard
     * @throws IOException if an error occurs while loading the cupboard.
     */
    @Override
    public synchronized Cupboard getCupboard() throws IOException{
        loadCupboard();//makes sure the data is the latest
        return cupboard;
    }

    /**
     * Searches the inventory for a specific need, if found it's deleted and saves the updated cupboard to a JSON file.
     * @throws IOException if an error occurs saving the cupboard, saveCupboard().
     */
    @Override
    public synchronized boolean deleteNeed(long id) throws IOException {

        for(int i=0; i < cupboard.getInventory().size(); i++){
            Need n = cupboard.getInventory().get(i);
            if(n.getId() == id){
                cupboard.getInventory().remove(i); //removes by the index
                saveCupboard(); //Saves the current cupboard data to a JSON file.
                System.out.println("Need deleted: " + n.getName() + " (ID: " + n.getId() +")");
                return true; //
            }
        }
        return false; //returns false if the id cannot be found in the cupboard inventory.
    }

    


    
    @Override
    public Need getNeedByID(String id) {
        for (Need need : cupboard.getInventory()) {
            if (need.getId().equals(Long.valueOf(id))) {
                return need;
            }
        }
        return null;
    }

    @Override
    public boolean updateNeed(Need updatedNeed) {
        try {
            List<Need> lst = cupboard.getInventory();
            for (Need need : cupboard.getInventory()) {
                if (need.getId().equals(updatedNeed.getId())) {
                    if (updatedNeed.getName() != null) need.setName(updatedNeed.getName());
                    if (updatedNeed.getquantity() > 0) need.setquantity(updatedNeed.getquantity());
                    if (updatedNeed.getFundingAmount() > 0) need.setFundingAmount(updatedNeed.getFundingAmount());
                    saveCupboard();
                    return true;
                }
            }
            return false; 
        } catch (IOException e) {
            return false;
        }
    }

}
