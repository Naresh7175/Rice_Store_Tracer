package com.ricestore.service;

import com.ricestore.model.Sale;
import com.ricestore.repository.SaleRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

  @Autowired
  private SaleRepository saleRepository;

  /* ===================== DATE RANGE (SINGLE SOURCE) ===================== */

  private LocalDateTime[] resolveDateRange(Integer month, Integer year) {

    LocalDate today = LocalDate.now();

    int effectiveMonth = (month != null) ? month : today.getMonthValue();
    int effectiveYear = (year != null) ? year : today.getYear();

    LocalDateTime start = LocalDateTime.of(
        effectiveYear, effectiveMonth, 1, 0, 0, 0
    );

    LocalDateTime end = start
        .with(TemporalAdjusters.lastDayOfMonth())
        .withHour(23).withMinute(59).withSecond(59);

    return new LocalDateTime[]{ start, end };
  }

  /* ===================== SALES ===================== */

  public List<Sale> getSales(Integer month, Integer year, Long customerId) {

    LocalDateTime[] range = resolveDateRange(month, year);
    LocalDateTime start = range[0];
    LocalDateTime end = range[1];

    if (customerId != null) {
      return saleRepository
          .findByCustomerIdAndSaleDateBetween(customerId, start, end);
    }

    return saleRepository.findBySaleDateBetween(start, end);
  }

  /* ===================== REVENUE ===================== */

  public Double getRevenue(Integer month, Integer year, Long customerId) {

    LocalDateTime[] range = resolveDateRange(month, year);
    LocalDateTime start = range[0];
    LocalDateTime end = range[1];

    Double revenue;

    if (customerId != null) {
      revenue = saleRepository
          .getRevenueByCustomerBetween(customerId, start, end);
    } else {
      revenue = saleRepository.getRevenueBetween(start, end);
    }

    return revenue != null ? revenue : 0.0;
  }

  /* ===================== DASHBOARD ===================== */

  public Map<String, Object> getDashboardStats(Integer month, Integer year) {

    LocalDateTime[] range = resolveDateRange(month, year);
    LocalDateTime start = range[0];
    LocalDateTime end = range[1];

    Double revenue = saleRepository.getRevenueBetween(start, end);
    Double bagsSold = saleRepository.getBagsSoldBetween(start, end);
    Double pendingMoney = saleRepository.getTotalPendingMoneyBetween(start, end);

    Map<String, Object> stats = new HashMap<>();
    stats.put("revenue", revenue != null ? revenue : 0.0);
    stats.put("bagsSold", bagsSold != null ? bagsSold : 0.0);
    stats.put("pendingMoney", pendingMoney != null ? pendingMoney : 0.0);

    return stats;
  }
}
