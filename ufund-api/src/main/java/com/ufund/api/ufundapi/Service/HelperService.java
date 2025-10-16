package com.ufund.api.ufundapi.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.DAO.CupboardDAO;
import com.ufund.api.ufundapi.DAO.FileCupboardDAO;
import com.ufund.api.ufundapi.DAO.FileInventoryDAO;
import com.ufund.api.ufundapi.DAO.InventoryDAO;
import com.ufund.api.ufundapi.Model.Need;

@Service
public class HelperService {
    //helper CRUD operations
    private final CupboardDAO cupboardDao = new FileCupboardDAO();
    private final InventoryDAO inventoryDao = new FileInventoryDAO();

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

    public CupboardDAO getCupboardDao() {
        return cupboardDao;
    }

    public InventoryDAO getInventoryDao() {
        return inventoryDao;
    }
}
