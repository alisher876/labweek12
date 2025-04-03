package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;



    @GetMapping("/filter")
    public List<Order> getOrdersByStatusAndDateRange(
            @RequestParam String status,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return orderRepository.findByStatusAndDateRange(status, start, end);
    }

    @GetMapping("/customer/{customerId}")
    public List<Order> getOrdersByCustomer(@PathVariable Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @GetMapping("/total")
    public Double getTotalOrderValue() {
        return orderRepository.getTotalOrderValue();
    }

    @GetMapping("/total-by-customer")
    public Map<Long, Double> getTotalOrderValueByCustomer() {
        List<Object[]> results = orderRepository.getTotalOrderValueByCustomer();
        Map<Long, Double> totals = new HashMap<>();
        for (Object[] result : results) {
            Long customerId = (Long) result[0];
            Double total = (Double) result[1];
            totals.put(customerId, total);
        }
        return totals;
    }
}
