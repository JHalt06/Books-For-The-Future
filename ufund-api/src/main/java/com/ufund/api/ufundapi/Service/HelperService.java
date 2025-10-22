package com.ufund.api.ufundapi.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.DAO.CupboardDAO;
import com.ufund.api.ufundapi.DAO.FileCupboardDAO;
import com.ufund.api.ufundapi.DAO.FileInventoryDAO;
import com.ufund.api.ufundapi.DAO.InventoryDAO;
import com.ufund.api.ufundapi.Model.Need;

@Service
public class HelperService {
    //helper CRUD operations
    private final CupboardDAO cupboardDao;
    private final InventoryDAO inventoryDao;

    @Autowired
    public HelperService(CupboardDAO cupboardDAO, InventoryDAO inventoryDAO){
        this.cupboardDao = cupboardDAO;
        this.inventoryDao = inventoryDAO;
    }

    public HelperService(){
        this.cupboardDao = new FileCupboardDAO();
        this.inventoryDao = new FileInventoryDAO();
    }

    public Need addNeed(Need need) {
            try {
                return cupboardDao.addNeed(need);
            } catch (IOException e) {
                System.out.println("Error saving need to file");
            }
        System.out.println("Need not found inside cupboard");
        return null;
    }

    public boolean removeNeed(Need need) {
        if (cupboardDao.needExistByName(need.getName())) {
            try {
                inventoryDao.addNeed(need);
                return cupboardDao.deleteNeed(need.getId());
            } catch (IOException e) {
                System.out.println("Error saving need to file");
            }
        }
        System.out.println("Need not found inside cupboard");
        return false;
    }

    public List<Need> getNeeds() {
        try {
            return cupboardDao.getCupboard().getCupboard();
        } catch (IOException ex) {
            System.out.println("Error retrieving file contents");
        }
        System.out.println("Error getting needs from cupboard, or doesn't exist");
        return null;
    }

    // Same thing but uses ID instead of name
    public Need addNeedToBasket(long id) throws IOException {
        //if need is already in basket then it causes conflict
        if (cupboardDao.getNeedByID(String.valueOf(id)) != null){
            return null;
        }

        Need needInventory = inventoryDao.getNeedByID(String.valueOf(id));
        if (needInventory != null){
            cupboardDao.addNeed(needInventory);
            inventoryDao.deleteNeed(id);
           
            return needInventory;
        }
        return null;
    }

    public boolean removeNeedFromBasket(long id) throws IOException {
        Need needFromCupboard = cupboardDao.getNeedByID(String.valueOf(id));
        if (needFromCupboard != null) {
            inventoryDao.addNeed(needFromCupboard);
            return cupboardDao.deleteNeed(id);
        }
        return false; // if need not found in cupbord
    }
    public boolean updateNeed(Need updatedNeed) throws IOException{
        return cupboardDao.updateNeed(updatedNeed);
       
    }



    public CupboardDAO getCupboardDao() {
        return cupboardDao;
    }

    public InventoryDAO getInventoryDao() {
        return inventoryDao;
    }
}
