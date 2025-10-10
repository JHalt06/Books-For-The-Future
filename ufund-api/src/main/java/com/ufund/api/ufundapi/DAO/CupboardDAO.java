package com.ufund.api.ufundapi.DAO;

import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.Model.Cupboard;
import com.ufund.api.ufundapi.Model.Need;

public interface CupboardDAO {
    Need addNeed(Need need) throws IOException;
    
    boolean needExistByName(String name);

    List<Need> getNeedByName(String name);

    Cupboard getCupboard() throws IOException; 

    boolean deleteNeed(long id) throws IOException;//

    Need getNeedByID(String id);

    boolean updateNeed(Need updatedNeed);
}
