package com.ufund.api.ufundapi.DAO;

import java.io.IOException;

import com.ufund.api.ufundapi.Model.Need;

public interface CupboardDAO {

    Need addNeed(Need need) throws IOException;
    
    boolean needExistByName(String name);
}
