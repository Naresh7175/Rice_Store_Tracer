package com.ricestore.controller;

import com.ricestore.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:4200")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/revenue")
    public Double getRevenue(@RequestParam String period,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        return reportService.getRevenue(period, month, year);
    }

    @GetMapping("/sales")
    public java.util.List<com.ricestore.model.Sale> getSales(@RequestParam(required = false) String period,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        return reportService.getSales(period, month, year);
    }

    @GetMapping("/dashboard")
    public java.util.Map<String, Object> getDashboardStats() {
        return reportService.getDashboardStats();
    }
}
