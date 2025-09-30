package com.ufund.api.ufundapi.DAO;

import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.Model.Need;

public interface CupboardDAO {

    Need addNeed(Need need) throws IOException;
    
    boolean needExistByName(String name);

    List<Need> getNeedByName(String name);

    Need getNeedByID(String id);
}
