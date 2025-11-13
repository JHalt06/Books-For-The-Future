package com.ufund.api.ufundapi.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.DAO.CupboardDAO;
import com.ufund.api.ufundapi.DAO.FileCupboardDAO;
import com.ufund.api.ufundapi.Model.Need;

@Service
public class HelperService {
    //helper CRUD operations
    private final CupboardDAO cupboardDao;

    @Autowired
    public HelperService(){
        this.cupboardDao = new FileCupboardDAO();
    }

    public HelperService(FileCupboardDAO dao){
        this.cupboardDao = dao;
    }

    public HelperService(String filepath) throws IOException{
        this.cupboardDao = new FileCupboardDAO(filepath);
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
        System.out.println("HelperService.removeNeed called with Need " + need);
        Need existingNeedInCupboard = cupboardDao.getNeedByID(need.getId());
        System.out.println("Existing need found " + existingNeedInCupboard);
        if (existingNeedInCupboard != null) {
             System.out.println("Need exists in cupboard with name: " + need.getName());
            try {
                boolean deleted =  cupboardDao.deleteNeed(existingNeedInCupboard.getId());
                System.out.println("Need deleted from cupboard: " + deleted);
                return deleted;
            } catch (IOException e) {
                System.out.println("Error saving need to file" + e.getMessage());
            }
        }
        else{
            System.out.println("Need not found inside cupboard");
            }
       
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

    public List<Need> searchNeedsByName(String name) {
        try {
            return cupboardDao.getNeedByName(name);
        } catch (IOException e) {
            System.out.println("Error searching for needs by name.");
            return Collections.emptyList();
        }
    }

    public boolean updateNeed(Need updatedNeed) throws IOException{
        return cupboardDao.updateNeed(updatedNeed);
    }

    public CupboardDAO getCupboardDao() {
        return cupboardDao;
    }

    public void checkoutNeed(int id, double fundingAmount) throws IOException, IllegalAccessException {
        if (fundingAmount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        Need need = this.cupboardDao.getNeedByID(id);
        need.setFundingAmount(need.getFundingAmount() + fundingAmount);
        updateNeed(need);
    }
}
