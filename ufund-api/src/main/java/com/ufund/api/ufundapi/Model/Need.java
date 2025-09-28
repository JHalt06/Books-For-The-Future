package com.ufund.api.ufundapi.Model;

public class Need {

    private long id;
    private String name; 
    private int quantity;
    private double fundingAmount;

    public Need(){}

    public Need(Long id, String name, int quantity, double fundingAmount){
        this.id = id;
        this.name = name;
        this.quantity = quantity;
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

    public int getquantity() {
        return quantity;
    }

    public void setquantity(int quantity) {
        this.quantity = quantity;
    }

    public double getFundingAmount() {
        return fundingAmount;
    }

    public void setFundingAmount(double fundingAmount) {
        this.fundingAmount = fundingAmount;
    }

}
