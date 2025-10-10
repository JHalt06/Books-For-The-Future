package com.ufund.api.ufundapi.Model;

public class Need {

    private Long id;
    private String name; 
    private int quantity;
    private double fundingAmount;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "cupboard_id")
    // private Cupboard cupboard;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "inventory_id")
    // private Inventory inventory;


    public Need(){}
    public Need(String name, int quantity, double fundingAmount){
        this.name = name;
        this.quantity = quantity;
        this.fundingAmount = fundingAmount;
    }

    public Need(Long id, String name, int quantity, double fundingAmount){
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.fundingAmount = fundingAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
