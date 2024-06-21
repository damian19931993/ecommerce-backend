package com.mystartup.ecommerce.controller;

import com.mystartup.ecommerce.entity.Order;
import com.mystartup.ecommerce.entity.User;
import com.mystartup.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@AuthenticationPrincipal User user,
                                             @RequestParam String shippingAddress,
                                             @RequestParam String shippingCity,
                                             @RequestParam String shippingCountry,
                                             @RequestParam String paymentMethod) {
        Order order = orderService.createOrderFromCart(user.getId(), shippingAddress, shippingCity, shippingCountry, paymentMethod);
        return ResponseEntity.ok(order);
    }
}
