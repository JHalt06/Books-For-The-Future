package com.ufund.api.ufundapi.DAO;

import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.Model.Inventory;
import com.ufund.api.ufundapi.Model.Need;

public interface InventoryDAO {

    Need addNeed(Need need) throws IOException;
    
    boolean needExistByName(String name);

    List<Need> getNeedByName(String name);

    Inventory getInventory() throws IOException; 

    boolean deleteNeed(long id) throws IOException;//

    Need getNeedByID(long id);

    boolean updateNeed(Need updatedNeed);

}
