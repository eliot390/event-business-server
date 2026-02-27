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
        System.out.println("Order items: " + order.getItems());
        sendBusinessEmail(order);
        sendCustomerEmail(order);
    }

    private void sendBusinessEmail(OrderRequest order) throws Exception {
        /* Send order email to me */
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(notifyEMail);
        helper.setSubject("New Toasted Order Received: " + order.getName());

        String html = buildBusinessEmailBody(order);
        helper.setText(html, true);

        mailSender.send(message);
    }

    private void sendCustomerEmail(OrderRequest order) throws Exception {
        /* Send order email to customer */
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(order.getEmail());
        helper.setSubject("Your order with Toasted is in!");

        String html = buildCustomerEmailBody(order);
        helper.setText(html, true);

        mailSender.send(message);
    }

    private String buildBusinessEmailBody(OrderRequest order){
        StringBuilder itemsHtml = new StringBuilder();
        double total = 0;

        String addressHtml = "";

        if (order.getDeliveryAddress() != null && !order.getDeliveryAddress().isBlank()) {
            addressHtml = "<br/>Delivery address: %s".formatted(order.getDeliveryAddress());
        }

        for(OrderItemDTO item : order.getItems()){
            double line = item.getQuantity() * item.getPrice();
            total += line;

            itemsHtml.append("""
                <tr style="background:white;">
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

        return """
            <div style="background:#EEFBFA; border-color:#CBF3F0; border-style:solid; border-width:2px; font-family: Arial, sans-serif; max-width:800px; margin:auto; padding:10px 20px;">
               <h2 style="color:#2EC4B6;">New order for %s</h2>
               <p>Delivery method: %s%s</p>
               <table width="100%%" style="border-collapse:collapse;">
                   <thead>
                   <tr style="background:#FFE4C2; color:#2EC4B6; border-color:#FF9F1C; border-style:solid; border-width:2px;">
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
                <h3 style="text-align:right; margin-top:20px;">Order Total: $%.2f</h3>
            </div>
        """.formatted(
                firstName(order.getName()),
                order.getDeliveryMethod(),
                addressHtml,
                itemsHtml.toString(),
                total);
    }

    private String firstName(String name){
        return name.trim().split("\\s+")[0];
    }

    private String buildCustomerEmailBody(OrderRequest order){
        StringBuilder itemsHtml = new StringBuilder();
        double total = 0;

        for(OrderItemDTO item : order.getItems()){
            double line = item.getQuantity() * item.getPrice();
            total += line;

            itemsHtml.append("""
                <tr style="background:white;">
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

        return """
            <div style="background:#EEFBFA; border-color:#CBF3F0; border-style:solid; border-width:2px; font-family: Arial, sans-serif; max-width:800px; margin:auto; padding:10px 20px;">
               <h2 style="color:#2EC4B6;">Thank you for your order %s!</h2>
               <p>We've received your order. Here are the details:</p>

               <table width="100%%" style="border-collapse:collapse;">
                   <thead>
                   <tr style="background:#FFE4C2; color:#2EC4B6; border-color:#FF9F1C; border-style:solid; border-width:2px;">
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
        
                <h3 style="text-align:right; margin-top:20px;">Order Total: $%.2f</h3>
        
                <p style="margin-top:20px;">We’ll contact you soon with pickup or delivery details.</p>
                <p style="color:#718096; font-size:12px;">If you have questions, reply to this email.</p>
            </div>
        """.formatted(firstName(order.getName()), itemsHtml.toString(), total);
    }
}
