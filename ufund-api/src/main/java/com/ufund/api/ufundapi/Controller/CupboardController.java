package com.ufund.api.ufundapi.Controller;

import java.util.logging.Logger;

import com.ufund.api.ufundapi.DAO.CupboardDAO;

public class CupboardController {
    private final CupboardDAO cupboardDAO;
    private static final Logger LOG = Logger.getLogger(CupboardController.class.getName());

    public CupboardController(CupboardDAO cupboardDAO) {
        this.cupboardDAO = cupboardDAO;
    }
}
