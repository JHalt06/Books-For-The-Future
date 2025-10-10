package com.ufund.api.ufundapi.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cupboard {
    private final long id;
    private final ArrayList<Need> cupboardNeeds;

    public Cupboard() {
        this.id = 2L;
        this.cupboardNeeds = new ArrayList<>();
    }
    
    public long getId() {
        return id;
    }
    public void addNeed(Need need){
        this.cupboardNeeds.add(need);
    }

    public void removeNeed(Need need){
        this.cupboardNeeds.remove(need);
    }

    public boolean hasNeedByName(String name){
            return cupboardNeeds.stream().anyMatch(n-> 
            n.getName().equalsIgnoreCase(name));
    }

    public List<Need> getCupboard() {
        return cupboardNeeds;
    }

    public List<Need> getNeedByName(String name) {
        List<Need> needs = cupboardNeeds.stream()
                            .filter(need -> need.getName().contains(name))
                            .collect(Collectors.toList());
        return needs;
    }
    
}
