package com.example.event_business_server.order;

import java.util.List;

public class OrderRequest {
    private String name;
    private String email;
    private String phone;
    private String deliveryMethod;
    private String deliveryAddress;
    private String orderDate;
    private String comments;
    private List<OrderItemDTO> items;
    private double total;

    public OrderRequest() {}

    public OrderRequest(
            String name,
            String email,
            String phone,
            String deliveryMethod,
            String deliveryAddress,
            String orderDate,
            String comments,
            List<OrderItemDTO> items,
            double total) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.deliveryMethod = deliveryMethod;
        this.deliveryAddress = deliveryAddress;
        this.orderDate = orderDate;
        this.comments = comments;
        this.items = items;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
