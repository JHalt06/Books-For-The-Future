package com.ufund.api.ufundapi.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FundingBasket {
    private String helperName;
    private List<Need> needs = new ArrayList<>();
    private boolean finalized;

    public FundingBasket(){}

    public FundingBasket(String helperName){
        this.helperName = helperName;
    }

    public FundingBasket(String helperName, List<Need> intialNeeds){
        this.helperName = helperName;
        if(intialNeeds != null){
            this.needs.addAll(intialNeeds);
        }
    }

    public String getHelperName(){
        return helperName;
    }

    public void setHelperName(String helperName){
        this.helperName = helperName;
    }

    public List<Need> getNeeds(){
        return needs;
    }

    public void setNeeds(List<Need> needs){
        this.needs = needs;
    }

    public boolean isFinalized(){
        return finalized;
    }

    public void setFinalized(boolean finalized){
        this.finalized = finalized;
    }

    public void addNeed(Need need){
        needs.add(need);
    }

    public void removeNeed(Long id){
        Iterator<Need> iterate = needs.iterator();
        while(iterate.hasNext()){
            Need n = iterate.next();
            if(n.getId().equals(id)){
                iterate.remove();
                return;
            }
        }
    }

}