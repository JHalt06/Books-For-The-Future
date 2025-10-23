package com.ufund.api.ufundapi.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Cupboard {
    @JsonProperty("id")
    private final long id;

    @JsonProperty("cupboard")
    private ArrayList<Need> cupboard;

    public Cupboard() {
        this.id = 2L;
        this.cupboard = new ArrayList<>();
    }
    
    public long getId() {
        return id;
    }
    public void addNeed(Need need){
        this.cupboard.add(need);
    }

    public void removeNeed(Need need){
        this.cupboard.remove(need);
    }

    public boolean hasNeedByName(String name){
            return cupboard.stream().anyMatch(n-> 
            n.getName().equalsIgnoreCase(name));
    }

    public List<Need> getCupboard() {
        return cupboard;
    }

    public List<Need> getNeedByName(String name) {
        List<Need> needs = cupboard.stream()
                            .filter(need -> need.getName().contains(name))
                            .collect(Collectors.toList());
        return needs;
    }
    
}
