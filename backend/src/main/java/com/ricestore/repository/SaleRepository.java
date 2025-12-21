package com.ricestore.repository;

import com.ricestore.model.Sale;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    /* ===================== SALES ===================== */

    List<Sale> findBySaleDateBetween(
        LocalDateTime start,
        LocalDateTime end
    );

    List<Sale> findByCustomerIdAndSaleDateBetween(
        Long customerId,
        LocalDateTime start,
        LocalDateTime end
    );

    /* ===================== REVENUE ===================== */
    // Revenue = totalAmount - discount (as per your existing logic)

    @Query("""
        SELECT SUM(s.totalAmount - s.discount)
        FROM Sale s
        WHERE s.saleDate BETWEEN :start AND :end
    """)
    Double getRevenueBetween(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    @Query("""
        SELECT SUM(s.totalAmount - s.discount)
        FROM Sale s
        WHERE s.customer.id = :customerId
          AND s.saleDate BETWEEN :start AND :end
    """)
    Double getRevenueByCustomerBetween(
        @Param("customerId") Long customerId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    /* ===================== BAGS SOLD ===================== */
    // Only physical bags count (unit = 'BAG' or legacy null)

    @Query("""
        SELECT SUM(si.quantity)
        FROM SaleItem si
        WHERE (si.unit IS NULL OR si.unit = 'BAG')
          AND si.sale.saleDate BETWEEN :start AND :end
    """)
    Double getBagsSoldBetween(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    /* ===================== PENDING MONEY ===================== */

    @Query("""
        SELECT SUM(s.balance)
        FROM Sale s
        WHERE s.saleDate BETWEEN :start AND :end
    """)
    Double getTotalPendingMoneyBetween(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );
}
