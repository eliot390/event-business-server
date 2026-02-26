package com.example.event_business_server.notifications;

import com.example.event_business_server.order.OrderItemDTO;
import com.example.event_business_server.order.OrderRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.orders.notifyEmail:}")
    private String notifyEMail;

    @Value("${app.mail.from:${spring.mail.username}}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderEmails(OrderRequest order) throws Exception {
        sendBusinessEmail(order);
        sendCustomerEmail(order);
    }

    private void sendBusinessEmail(OrderRequest order) throws Exception {
        /* Send order email to me */
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromEmail);
        helper.setTo(notifyEMail);
        helper.setSubject("New Toasted Order Received: " + order.getName());
        helper.setText(buildBusinessEmailBody(order));

        mailSender.send(message);
    }

    private void sendCustomerEmail(OrderRequest order) throws Exception {
        /* Send order email to customer */
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromEmail);
        helper.setTo(order.getEmail());
        helper.setSubject("Your order is in");
        helper.setText(buildCustomerEmailBody(order));
        mailSender.send(message);
    }

    private String buildBusinessEmailBody(OrderRequest order){
        return buildCommonBody(order, true);
    }

    private String buildCustomerEmailBody(OrderRequest order){
        StringBuilder itemsHtml = new StringBuilder();
        double total = 0;

        for(OrderItemDTO item : order.getItems()){
            double line = item.getQuantity() * item.getPrice();
            total += line;

            itemsHtml.append("""
                <tr>
                    <td style="padding:8px;">%s</td>
                    <td style="padding:8px;">%s</td>
                    <td style="padding:8px;">%d</td>
                    <td style="padding:8px;">$%.2f</td>
                </tr>
              """.formatted(
                 item.getProductName(),
                 item.getOrderSize(),
                 item.getQuantity(),
                 line
            ));
        }

        return
        """
            <div style="font-family: Arial, sans-serif; max-width:600px; margin:auto;">
                <h2 style="color:#2f855a;">Thank you for your order, %s!</h2>
                <p>We’ve received your dessert order. Here are the details:</p>
        
                <table width="100%%" style="border-collapse:collapse;">
                    <thead>
                        <tr style="background:#f6e05e;">
                            <th align="left" style="padding:8px;">Item</th>
                            <th align="left" style="padding:8px;">Size</th>
                            <th align="left" style="padding:8px;">Qty</th>
                            <th align="left" style="padding:8px;">Total</th>
                        </tr>
                    </thead>
                    <tbody>
                      %s
                    </tbody>
                </table>
        
                <h3 style="margin-top:20px;">Order Total: $%.2f</h3>
        
                <p style="margin-top:20px;">We’ll contact you soon with pickup or delivery details.</p>
                <p style="color:#718096; font-size:12px;">If you have questions, reply to this email.</p>
            </div>
        """.formatted(order.getName(), itemsHtml.toString(), total);
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
