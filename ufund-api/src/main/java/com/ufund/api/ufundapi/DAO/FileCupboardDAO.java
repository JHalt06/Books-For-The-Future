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


    /**
     *
     * @param filePath Path to the JSON file use for persistance.
     * @throws IOException if an error occurs during file read/write
     */

    @Autowired
    public FileCupboardDAO(@Value("${cupboard.filepath}")String filePath) throws IOException{
        this.objectMapper = new ObjectMapper();
        this.file = new File(filePath);
        loadCupboard();
    }

    public FileCupboardDAO() {
        this.objectMapper = new ObjectMapper();
        this.file = new File("data/cupboard.json");
        try {
            loadCupboard();
        } catch (IOException e) {
            System.out.println("Error loading cupboard data from cupboard.json");
        }
    }


    /**
     * Loads the inventory data from the file, or initialize a new inventory if the files doesn't exist
     * @throws IOException if an error occurs while reading the file
     */
    private void loadCupboard() throws IOException{
        if(file.exists() && file.length() > 0){
            this.cupboard = objectMapper.readValue(file, Cupboard.class);
            System.out.println("cupboard loaded from file: " + file.getPath());
        }
        else{
            this.cupboard = new Cupboard();
            saveCupboard();
            System.out.println("New inventory created and save:  " + file.getPath());

        }
    }

    /**
     * Saves the current cupboard data to a JSON file.
     * @throws IOException if an error occurs while writing the file.
     */
    private void saveCupboard() throws IOException{
        System.out.println("Saving cupboard instance: " + cupboard);
        System.out.println("Cupboard contents before save:");
        for (Need n : cupboard.getCupboard()) {
            System.out.println(" - ID: " + n.getId() + ", Name: " + n.getName() + ", Qty: " + n.getquantity());
        }

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, cupboard);
        System.out.println("Cupboard contents after save:");
        for (Need n : cupboard.getCupboard()) {
            System.out.println(" - ID: " + n.getId() + ", Name: " + n.getName() + ", Qty: " + n.getquantity());
        }
        System.out.println("Saved to: " + file.getAbsolutePath());
    }


    /**
     * Returns the latest cupboard
     * @throws IOException if an error occurs while loading the cupboard.
     */
    @Override
    public synchronized Cupboard getCupboard() throws IOException{
        return cupboard;
    }

    /**
     * Searches the cupboard for a specific need, if found it's deleted and saves the updated cupboard to a JSON file.
     * @param id the id of the need to delete.
     * @throws IOException if an error occurs saving the cupboard, saveCupboard().
     */
    @Override
    public synchronized boolean deleteNeed(long id) throws IOException {
        System.out.println("CupboardDAO.deleteNeed called with ID:" + id );
        for(int i=0; i < cupboard.getCupboard().size(); i++){
            Need n = cupboard.getCupboard().get(i);
            if(n.getId() == id){
                cupboard.getCupboard().remove(i); //removes by the index
                saveCupboard(); //Saves the current inventory data to a JSON file.
                System.out.println("Need deleted: " + n.getName() + " (ID: " + n.getId() +")");
                return true; //
            }
        }
        System.out.println("No need found with ID: " + id);
        return false; //returns false if the id cannot be found in the inventory inventory.
    }

    /**
     * Searches the cupboard for a need that matches a given id value.
     * @param id the id of the need to search for.
     * @return the need with matching id if found, null otherwise.
     */
    @Override
    public Need getNeedByID(long id) {
        for (Need need : cupboard.getCupboard()) {
            if (need.getId() == id) {
                return need;
            }
        }
        return null;
    }

    @Override
    public boolean updateNeed(Need updatedNeed) {
        try {
            for (Need need : cupboard.getCupboard()) {
                if (need.getId().longValue() == updatedNeed.getId().longValue()) {
                    System.out.println("need found!");
                    System.out.println(updatedNeed.getName());
                    System.out.println(Integer.valueOf(updatedNeed.getquantity()));

                    if (updatedNeed.getName() != null) {
                        need.setName(updatedNeed.getName());
                        System.out.println("Changed name to " + need.getName());
                    }
                    if (updatedNeed.getquantity() > 0) {
                        need.setquantity(updatedNeed.getquantity());
                        System.out.println("Changed quantity to " + need.getquantity());
                    }

                    System.out.println("Updating cupboard instance: " + cupboard.getCupboard());
                    saveCupboard();
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Adds a new need to the needs cupboard if it doesnt already exists
     * Assigns a new ID and persist the change to file.
     * @param need the Need object to add.
     * @return the created Need with assigned ID.
     * @throws IOException if the cupboard cannot be saved.
     * @Throws IllegalArgumentException if a Need with same name already exists.
     */
    @Override
    public synchronized Need addNeed(Need need) throws IOException {
        if(needExistByName(need.getName())){
            throw new IllegalArgumentException("Need with this name not in");
        }
        //assign new ID(incremental)
        if (need.getId() == null) {
            List<Need> needs = cupboard.getCupboard();
            long newID = needs.stream().mapToLong(Need::getId).max().orElse(0L) + 1;
            need.setId(newID);
        }

        cupboard.addNeed(need);
        saveCupboard();

        System.out.println("New need added: " + need.getName() + "(ID " +
                need.getId() +" )");
        System.out.println("Total items in inventory: " + cupboard.getCupboard().size());

        return need;
    }

    /**
     * Checks whether a Need with the specified name exists in the cupboard
     * @param name the name of the Need to check
     * @return true if a need with the same name exist, false otherwise.
     */
    @Override
    public boolean needExistByName(String name) {
        return cupboard.hasNeedByName(name);
    }

    /**
     * Checks wheather a Need containing the specified query exists in the cupboard
     * @param query the query to search for needs with
     * @return the list of needs if any exist, else an empty list.
     */
    @Override
    public List<Need> getNeedByName(String name) throws IOException {
        loadCupboard();
        return cupboard.getNeedByName(name);
    }

    @Override
    public Need[] searchNeeds(String q) throws IOException {
        loadCupboard();
        return cupboard.getCupboard().stream()
                .filter(i -> i.getName().toLowerCase().contains(q.toLowerCase()))
                .toArray(Need[]::new);
    }
}