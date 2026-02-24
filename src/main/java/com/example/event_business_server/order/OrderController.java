package com.example.event_business_server.order;

import com.example.event_business_server.notifications.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final EmailService emailService;

    public OrderController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest order) {
        // Basic validation
        if (order.getEmail() == null || order.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        if (order.getItems() == null || order.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body("Cart is empty");
        }

        // Send emails
        emailService.sendOrderEmails(order);

        return ResponseEntity.ok().body("Order received");
    }
}
