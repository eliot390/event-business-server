package com.example.event_business_server.notifications;

import com.example.event_business_server.order.OrderItemDTO;
import com.example.event_business_server.order.OrderRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.order.notifyEmail}")
    private String notifyEMail;

    @Value("{app.mail.from}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderEmails(OrderRequest order){
        /* Send order email to me */
        SimpleMailMessage toBusiness = new SimpleMailMessage();
        toBusiness.setFrom(fromEmail);
        toBusiness.setTo(notifyEMail);
        toBusiness.setSubject("New Toasted Order Received: ");
        toBusiness.setText(buildBusinessEmailBody(order));
        mailSender.send(toBusiness);

        /* Send order email to customer */
        SimpleMailMessage toCustomer = new SimpleMailMessage();
        toCustomer.setFrom(fromEmail);
        toCustomer.setTo(order.getEmail());
        toCustomer.setSubject("Your order is in");
        toCustomer.setText(buildCustomerEmailBody(order));
        mailSender.send(toCustomer);
    }

    private String buildBusinessEmailBody(OrderRequest order){
        return buildCommonBody(order, true);
    }

    private String buildCustomerEmailBody(OrderRequest order){
        return "Thank you for your order " + buildCommonBody(order, false);
    }

    private String buildCommonBody(OrderRequest order, boolean includeCustomerDetails) {
        StringBuilder sb = new StringBuilder();

        if(includeCustomerDetails){
            sb.append("Customer:\n");
            sb.append("Name: ").append(order.getName()).append("\n");
            sb.append("Email: ").append(order.getEmail()).append("\n");
            sb.append("Phone: ").append(order.getPhone()).append("\n\n");
        }

        sb.append("items:\n");
        double computedTotal = 0;

        for(OrderItemDTO item : order.getItems()){
            double line = item.getQuantity() * item.getPrice();
            computedTotal += line;

            sb.append("- ")
                    .append(item.getProductName())
                    .append(" (").append(item.getOrderSize()).append(") ")
                    .append("x").append(item.getQuantity())
                    .append(" @ $").append(String.format("%.2f", item.getPrice()))
                    .append(" = $").append(String.format("%.2f", line))
                    .append("\n");
        }

        sb.append("\nTotal: $").append(String.format("%.2f", computedTotal)).append("\n");

        if (order.getComments() != null && !order.getComments().isBlank()) {
            sb.append("\nNotes:\n").append(order.getComments()).append("\n");
        }

        return sb.toString();
    }
}
