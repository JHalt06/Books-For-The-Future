package com.ufund.api.ufundapi.Service;

import com.ufund.api.ufundapi.Model.DashboardStats;
import com.ufund.api.ufundapi.Model.Need;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DashboardService {

    private final UserService userService;
    private final HelperService helperService;

    @Autowired
    public DashboardService(UserService userService, HelperService helperService) {
        this.userService = userService;
        this.helperService = helperService;
    }

    public DashboardStats getDashboardStats() throws IOException {
        int totalUsers = userService.getUserCount();
        List<Need> needs = helperService.getNeeds();
        int totalNeeds = needs != null ? needs.size() : 0;
        double totalFunding = needs != null ? needs.stream().mapToDouble(Need::getFundingAmount).sum() : 0.0;

        return new DashboardStats(totalUsers, totalNeeds, totalFunding);
    }
}
