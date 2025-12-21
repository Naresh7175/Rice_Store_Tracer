package com.ricestore.controller;

import com.ricestore.model.Sale;
import com.ricestore.service.ReportService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/sales")
    public List<Sale> getSales(
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Long customerId
    ) {
        return reportService.getSales(month, year, customerId);
    }

    @GetMapping("/revenue")
    public Double getRevenue(
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Long customerId
    ) {
        return reportService.getRevenue(month, year, customerId);
    }

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardStats(
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year
    ) {
        return reportService.getDashboardStats(month, year);
    }
}

