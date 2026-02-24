package com.example.event_business_server.customer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Customer {
    private static final DateTimeFormatter ORDER_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm:ss a");

    private static final DateTimeFormatter DELIVERY_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MM-dd-yyyy");

    private Long customer_id;
    private String first_name;
    private String last_name;
    private String email_address;
    private String phone_number;
    private LocalDateTime order_date = LocalDateTime.now();
    private LocalDate delivery_date;
    private String comment;
    private String delivery_method;
    private String address;

    public Customer(Long customer_id,
                    String first_name,
                    String last_name,
                    String email_address,
                    String phone_number,
                    LocalDateTime order_date,
                    LocalDate delivery_date,
                    String comment,
                    String delivery_method,
                    String address) {
        this.customer_id = customer_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email_address = email_address;
        this.phone_number = phone_number;
        this.order_date = order_date;
        this.delivery_date = delivery_date;
        this.comment = comment;
        this.delivery_method = delivery_method;
        this.address = address;
    }

    public Long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public LocalDateTime getOrder_date() {
        return order_date;
    }

    public void setOrder_date(LocalDateTime order_date) {
        this.order_date = order_date;
    }

    public LocalDate getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(LocalDate delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getFormattedOrderDate() {
        return order_date.format(ORDER_DATE_FORMATTER);
    }

    public String getFormattedDeliveryDate() {
        return delivery_date != null
                ? delivery_date.format(DELIVERY_DATE_FORMATTER)
                : null;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDelivery_method() {
        return delivery_method;
    }

    public void setDelivery_method(String delivery_method) {
        this.delivery_method = delivery_method;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
