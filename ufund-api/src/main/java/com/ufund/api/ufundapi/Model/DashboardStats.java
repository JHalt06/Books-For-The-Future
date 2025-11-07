package com.ufund.api.ufundapi.Model;
import com.fasterxml.jackson.annotation.JsonProperty;


public class DashboardStats {

    @JsonProperty("totalUsers")
    private int totalUsers;

    @JsonProperty("totalNeeds")
    private int totalNeeds;

    @JsonProperty("totalFunding")
    private double totalFunding;

    public DashboardStats(int totalUsers, int totalNeeds, double totalFunding) {
        this.totalUsers = totalUsers;
        this.totalNeeds = totalNeeds;
        this.totalFunding = totalFunding;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public int getTotalNeeds() {
        return totalNeeds;
    }

    public void setTotalNeeds(int totalNeeds) {
        this.totalNeeds = totalNeeds;
    }

    public double getTotalFunding() {
        return totalFunding;
    }

    public void setTotalFunding(double totalFunding) {
        this.totalFunding = totalFunding;
    }
}