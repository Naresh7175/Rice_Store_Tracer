package com.ricestore.service;

import com.ricestore.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Service
public class ReportService {

    @Autowired
    private SaleRepository saleRepository;

    public java.util.Map<String, Object> getDashboardStats() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0)
                .withSecond(0);
        LocalDateTime endOfMonth = now; // Now

        Double revenue = saleRepository.getRevenueBetween(startOfMonth, endOfMonth);
        Double bagsSold = saleRepository.getBagsSoldBetween(startOfMonth, endOfMonth);
        Double pendingMoney = saleRepository.getTotalPendingMoney();

        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("revenue", revenue != null ? revenue : 0.0);
        stats.put("bagsSold", bagsSold != null ? bagsSold : 0.0);
        stats.put("pendingMoney", pendingMoney != null ? pendingMoney : 0.0);

        return stats;
    }

    public java.util.List<com.ricestore.model.Sale> getSales(String period, Integer monthParams, Integer yearParams) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start;
        LocalDateTime end = now;

        if (monthParams != null && yearParams != null) {
            // Custom Month Filter
            start = LocalDateTime.of(yearParams, monthParams, 1, 0, 0, 0);
            end = start.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);
        } else {
            String effectivePeriod = (period != null) ? period : "monthly";
            start = getStartDateForPeriod(effectivePeriod, now);
        }

        return saleRepository.findBySaleDateBetween(start, end);
    }

    public Double getRevenue(String period, Integer monthParams, Integer yearParams) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start;
        LocalDateTime end = now;

        if (monthParams != null && yearParams != null) {
            // Custom Month Filter
            start = LocalDateTime.of(yearParams, monthParams, 1, 0, 0, 0);
            end = start.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);
        } else {
            start = getStartDateForPeriod(period, now);
        }

        Double revenue = saleRepository.getRevenueBetween(start, end);
        return revenue != null ? revenue : 0.0;
    }

    private LocalDateTime getStartDateForPeriod(String period, LocalDateTime now) {
        LocalDateTime start;
        switch (period.toLowerCase()) {
            case "monthly":
                start = now.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0);
                break;
            case "quarterly":
                int month = now.getMonthValue();
                int quarterStartMonth = month - (month - 1) % 3;
                start = now.withMonth(quarterStartMonth).with(TemporalAdjusters.firstDayOfMonth()).withHour(0)
                        .withMinute(0).withSecond(0);
                break;
            case "yearly":
                start = now.with(TemporalAdjusters.firstDayOfYear()).withHour(0).withMinute(0).withSecond(0);
                break;
            default:
                // Default to monthly if invalid or other
                start = now.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0);
        }
        return start;
    }
}
