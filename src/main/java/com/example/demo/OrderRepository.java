package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findByStatusAndDateRange(
            @Param("status") String status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<Order> findByCustomerId(Long customerId);

    @Query("SELECT SUM(o.amount) FROM Order o")
    Double getTotalOrderValue();

    @Query("SELECT o.customer.id, SUM(o.amount) FROM Order o GROUP BY o.customer.id")
    List<Object[]> getTotalOrderValueByCustomer();
}