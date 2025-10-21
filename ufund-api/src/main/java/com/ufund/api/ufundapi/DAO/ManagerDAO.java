package com.ufund.api.ufundapi.DAO;

import java.io.IOException;

import com.ufund.api.ufundapi.Model.Manager;

public interface ManagerDAO {
    Manager getManager() throws IOException;
}
