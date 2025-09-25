package com.ufund.api.ufundapi.Model;

public class Need {

    private long id;
    private String name; 
    private int quanity;
    private double fundingAmount;

    public Need(){}

    public Need(Long id, String name, int quantity, double fundingAmount){
        this.id = id;
        this.name = name;
        this.quanity = quantity;
        this.fundingAmount = fundingAmount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuanity() {
        return quanity;
    }

    public void setQuanity(int quanity) {
        this.quanity = quanity;
    }

    public double getFundingAmount() {
        return fundingAmount;
    }

    public void setFundingAmount(double fundingAmount) {
        this.fundingAmount = fundingAmount;
    }

}
