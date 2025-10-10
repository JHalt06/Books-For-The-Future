package com.ufund.api.ufundapi.Service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.DAO.FileInventoryDAO;
import com.ufund.api.ufundapi.DAO.InventoryDAO;
import com.ufund.api.ufundapi.Model.Need;

@Service
public class ManagerService { // also Developer/Admin?
    //developer CRUD operations
    // private final CupboardDAO cupboardDao = new FileCupboardDAO();
    private final InventoryDAO inventoryDao = new FileInventoryDAO();
    
    // all CRUD operations are contained in this file, but the logic is held in the Cupboard/Inventory.java model files
    // not sure if saveCupboard() and saveInventory() need to be in here

    public Need findNeedById(String id) {
        return inventoryDao.getNeedByID(id);
    }

    public boolean deleteNeed(String id) {
        try {
            return inventoryDao.deleteNeed(Long.parseLong(id));
        } catch (NumberFormatException | IOException nfE) {
            return false;
        }
    }

    public Need addNeed(Need need) {
        try {
            return inventoryDao.addNeed(need);
        } catch (IOException e) {
            return null;
        }
    }

    public boolean updateNeed(Need updatedNeed) {
        return inventoryDao.updateNeed(updatedNeed);
    }
}
