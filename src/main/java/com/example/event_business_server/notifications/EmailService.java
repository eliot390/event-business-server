package com.example.event_business_server.notifications;

import com.example.event_business_server.order.OrderItemDTO;
import com.example.event_business_server.order.OrderRequest;
import com.resend.Resend;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.resend.services.emails.model.SendEmailRequest;

@Service
public class EmailService {

    private final Resend resend;
    private final String fromEmail;
    private final String notifyEmail;

    public EmailService(
            @Value("${resend.api.key}") String resendApiKey,
            @Value("${app.mail.from}") String fromEmail,
            @Value("${app.orders.notifyEmail}") String notifyEmail
    ) {
        this.resend = new Resend(resendApiKey);
        this.fromEmail = fromEmail;
        this.notifyEmail = notifyEmail;
    }

    public void sendOrderEmails(OrderRequest order) {
        SendEmailRequest businessEmail = SendEmailRequest.builder()
                .from(fromEmail)
                .to(notifyEmail)
                .subject("New Order for " + order.getName() + " - " + order.getOrderID())
                .html(buildBusinessEmailBody(order))
                .build();
        resend.emails().send(businessEmail);

        SendEmailRequest customerEmail = SendEmailRequest.builder()
                .from(fromEmail)
                .to(order.getEmail())
                .subject("Confirmed! We got your order " + order.getOrderID())
                .html(buildCustomerEmailBody(order))
                .build();
        resend.emails().send(customerEmail);
    }

    private String formattedDate(String orderDate) {
        LocalDate date = LocalDate.parse(orderDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return date.format(formatter);
    }

    private String buildBusinessEmailBody(OrderRequest order){
        StringBuilder itemsHtml = new StringBuilder();
        double total = 0;

        String addressHtml = "";

        if (order.getDeliveryAddress() != null && !order.getDeliveryAddress().isBlank()) {
            String address = order.getDeliveryAddress();
            String city = order.getDeliveryCity();
            String zip = order.getDeliveryZip();

            StringBuilder formatted = new StringBuilder();
            formatted.append(address);

            if ((city != null && !city.isBlank()) || (zip != null && !zip.isBlank())) {
                formatted.append("<br/>");

                if (city != null && !city.isBlank()) {
                    formatted.append(city);
                }

                if (zip != null && !zip.isBlank()) {
                    formatted.append(city != null && !city.isBlank() ? ", " : "");
                    formatted.append(zip);
                }
            }

            addressHtml = "<br/>Delivery address:<br/>" + formatted;
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
               <p>Order #: %s</p>
               <p>Scheduled Delivery/Pickup: %s</p>
               <p>Phone: %s</p>
               <p>E-mail: %s</p>
               <p>Comments: %s</p>
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
            order.getName(),
            order.getOrderID(),
            formattedDate(order.getOrderDate()),
            order.getPhone(),
            order.getEmail(),
            order.getComments(),
            order.getDeliveryMethod(), addressHtml,
            itemsHtml.toString(),
            total
        );
    }

    private String firstName(String name){
        return name.trim().split("\\s+")[0];
    }

    private String buildCustomerEmailBody(OrderRequest order){
        StringBuilder itemsHtml = new StringBuilder();
        double total = 0;

        String addressHtml = "";

        if (order.getDeliveryAddress() != null && !order.getDeliveryAddress().isBlank()) {
            String address = order.getDeliveryAddress();
            String city = order.getDeliveryCity();
            String zip = order.getDeliveryZip();

            StringBuilder formatted = new StringBuilder();
            formatted.append(address);

            if ((city != null && !city.isBlank()) || (zip != null && !zip.isBlank())) {
                formatted.append("<br/>");

                if (city != null && !city.isBlank()) {
                    formatted.append(city);
                }

                if (zip != null && !zip.isBlank()) {
                    formatted.append(city != null && !city.isBlank() ? ", " : "");
                    formatted.append(zip);
                }
            }

            addressHtml = "<br/>Delivery Address:<br/>" + formatted;
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
            <div style="background:#EEFBFA; border-color:#CBF3F0; border-style:solid; border-width:2px; font-family: Arial, sans-serif; max-width:800px; margin:auto; padding:2px 20px;">
                 <div>
                     <p style="color:#2EC4B6; font-size:40px; font-weight:bolder; text-align:center; margin:0px;">
                         <img src="https://flourandflask.com/assets/logo-grn-ZqpFJqM0.png" style="width:4rem">
                         <a href="https://www.flourandflask.com" target="_blank" style="text-decoration:none; color:inherit">Flour & Flask</a>
                     </p>
                 </div>
               <h2 style="color:#2EC4B6;">Thank you for your order %s!</h2>
               <p>Order #: %s</p>
               <p>Order Date: %s</p>
               <p>Delivery Method: %s%s</p>

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
               
               <p>Customer Notes: %s</p>
        
               <h3 style="text-align:right; margin-top:20px;">Order Total: $%.2f</h3>
        
               <p style="color:#718096;">If you have questions, please contact us at chefeliotison@gmail.com.</p>
            </div>
        """.formatted(
           firstName(order.getName()),
           order.getOrderID(),
           formattedDate(order.getOrderDate()),
           order.getDeliveryMethod(), addressHtml,
           itemsHtml.toString(),
           order.getComments(),
           total);
    }
}
