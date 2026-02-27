package com.example.event_business_server.order;

import com.example.event_business_server.notifications.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/orders")
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
        try {
            emailService.sendOrderEmails(order);
            return ResponseEntity.ok("Order received");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Order received, but failed to send email: " + e.getMessage());
        }
    }
}

