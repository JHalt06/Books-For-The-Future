package com.ufund.api.ufundapi.Model;

import java.util.ArrayList;
import java.util.List;

public class Cupboard {

    private long id; 
    private List<Need> inventory;

    public Cupboard(){
        this.id = 1L;
        this.inventory = new ArrayList<>();

    }

    public long getId() {
        return id;
    }
    public void addNeed(Need need){
        this.inventory.add(need);
    }
    public boolean hasNeedByName(String name){
            return inventory.stream().anyMatch(n-> 
            n.getName().equalsIgnoreCase(name));
    }

    public List<Need> getInventory() {
        return inventory;
    }

   
}
