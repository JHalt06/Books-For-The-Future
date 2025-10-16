package com.ufund.api.ufundapi.DAO;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.Model.Inventory;
import com.ufund.api.ufundapi.Model.Need;

@Repository
public class FileInventoryDAO implements InventoryDAO {

    private final ObjectMapper objectMapper;
    private final File file;
    private Inventory inventory;

    /**
     * 
     * @param filePath Path to the JSON file use for persistance. 
     * @throws IOException if an error occurs during file read/write 
     */

    @Autowired
    public FileInventoryDAO(@Value("${inventory.filepath}")String filePath) throws IOException{
        this.objectMapper = new ObjectMapper();
        this.file = new File(filePath);
        loadInventory();
    }

    /**
     * Purpose of Unit test 
     * @param filePath Path to the JSON file use for persistance. 
     * @param objectMapper reading the JSON file 
     * @throws IOException if an error occurs during file read/write 
     */
    public FileInventoryDAO(String filePath,ObjectMapper objectMapper) throws IOException{
        this.objectMapper = objectMapper;
        this.file = new File(filePath);
        loadInventory();
    }

    public FileInventoryDAO() {
        this.objectMapper = new ObjectMapper();
        this.file = new File("data/inventory.json");
        try {
            loadInventory();
        } catch (IOException e) {
            System.out.println("Error loading cupboard data from inventory.json");
        }

    }
    

    /**
     * Adds a new need to the inventory inventory if it doesnt already exists 
     * Assigns a new ID and persist the change to file. 
     * @param need the Need object to add.
     * @return the created Need with assigned ID.
     * @throws IOException if the inventory cannot be saved. 
     * @Throws IllegalArgumentException if a Need with same name already exists. 
     */
    @Override
    public synchronized Need addNeed(Need need) {
        if(needExistByName(need.getName())){
            throw new IllegalArgumentException("Need with this name already exists.");
        }
        //assign new ID(incremental)
        List<Need> needs = inventory.getInventory();
        long newID = needs.stream().mapToLong(Need::getId).max().orElse(0L) +1;
        need.setId(newID);

        //add to inventory and save
        inventory.addNeed(need);
        try {
            saveInventory();
        } catch (IOException e) {
            System.out.println("Error adding need: " + need.getName());
        }

        System.out.println("New need added: " + need.getName() + "(ID " +
            need.getId() +" )");
        System.out.println("Total items in inventory: " + inventory.getInventory().size());

        return need; 
    }

    /**
     * Checks whether a Need with the specified name exists in the inventory inventory
     * @param name the name of the Need to check
     * @return true if a need with the same name exist, false otherwise. 
     */
    @Override
    public boolean needExistByName(String name) {
        return inventory.hasNeedByName(name);
    }

    /**
     * Checks wheather a Need containing the specified query exists in the inventory inventory
     * @param query the query to search for needs with
     * @return the list of needs if any exist, else an empty list.
     */
    @Override
    public List<Need> getNeedByName(String name) {
        return inventory.getNeedByName(name);
    }

    /**
     * Loads the inventory data from the file, or initialize a new inventory if the files doesn't exist
     * @throws IOException if an error occurs while reading the file 
     */
    private void loadInventory() throws IOException{
        if(file.exists() && file.length() > 0){
            this.inventory = objectMapper.readValue(file, Inventory.class);
            System.out.println("inventory loaded from file: " + file.getPath());
        }
        else{
            this.inventory = new Inventory();
            saveInventory();
            System.out.println("New inventory created and save:  " + file.getPath());
            
        }
    }

    /**
     * Saves the current inventory data to a JSON file.
     * @throws IOException if an error occurs while writing the file. 
     */
    private void saveInventory() throws IOException{
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, inventory);
    }


    /**
     * Returns the latest inventory
     * @throws IOException if an error occurs while loading the inventory.
     */
    @Override
    public synchronized Inventory getInventory() throws IOException{
        loadInventory();//makes sure the data is the latest
        return inventory;
    }

    /**
     * Searches the inventory for a specific need, if found it's deleted and saves the updated inventory to a JSON file.
     * @throws IOException if an error occurs saving the inventory, saveInventory().
     */
    @Override
    public synchronized boolean deleteNeed(long id) throws IOException {

        for(int i=0; i < inventory.getInventory().size(); i++){
            Need n = inventory.getInventory().get(i);
            if(n.getId() == id){
                inventory.getInventory().remove(i); //removes by the index
                saveInventory(); //Saves the current inventory data to a JSON file.
                System.out.println("Need deleted: " + n.getName() + " (ID: " + n.getId() +")");
                return true; //
            }
        }
        return false; //returns false if the id cannot be found in the inventory inventory.
    }

    


    
    @Override
    public Need getNeedByID(String id) {
        for (Need need : inventory.getInventory()) {
            if (need.getId().equals(Long.valueOf(id))) {
                return need;
            }
        }
        return null;
    }

    @Override
    public boolean updateNeed(Need updatedNeed) {
        try {
            List<Need> lst = inventory.getInventory();
            for (Need need : inventory.getInventory()) {
                if (need.getId().equals(updatedNeed.getId())) {
                    if (updatedNeed.getName() != null) need.setName(updatedNeed.getName());
                    if (updatedNeed.getquantity() > 0) need.setquantity(updatedNeed.getquantity());
                    if (updatedNeed.getFundingAmount() > 0) need.setFundingAmount(updatedNeed.getFundingAmount());
                    saveInventory();
                    return true;
                }
            }
            return false; 
        } catch (IOException e) {
            return false;
        }
    }
}

