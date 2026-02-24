package com.example.event_business_server.order;

public class OrderItemDTO {
    private String productKey;
    private String productName;
    private String orderSize;
    private int quantity;
    private double price;

    public OrderItemDTO(String productKey, String productName, String orderSize, int quantity, double price) {
        this.productKey = productKey;
        this.productName = productName;
        this.orderSize = orderSize;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderSize() {
        return orderSize;
    }

    public void setOrderSize(String orderSize) {
        this.orderSize = orderSize;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
