
package com.ufund.api.ufundapi.Controller;

import com.ufund.api.ufundapi.Model.DashboardStats;
import com.ufund.api.ufundapi.Service.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private static final Logger LOG = Logger.getLogger(DashboardController.class.getName());
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStats> getStats() {
        LOG.info("GET /dashboard/stats");
        try {
            DashboardStats stats = dashboardService.getDashboardStats();
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}