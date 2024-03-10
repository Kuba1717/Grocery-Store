package com.example.sklep.model;

import javafx.beans.property.SimpleIntegerProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Product {
    private final String productName;
    private final LocalDate expirationDate;
    private final String category;
    private final Integer quantity;
    private LocalDateTime deliveryTime;


    public Product(String productName, LocalDate expirationDate, String category, int quantity) {
        this.productName = productName;
        this.expirationDate = expirationDate;
        this.category = category;
        this.quantity = quantity;

    }



    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }


    public String getProductName() {
        return productName;
    }

    public LocalDate expirationDateProperty() {
        return expirationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public String getCategory() {
        return category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer quantityProperty() {
        return quantity;
    }
    public Integer orderSecondsLeftProperty() {
        Integer secondsLeft =0;
        LocalDateTime now = LocalDateTime.now();
        long secondsUntilExpiration = now.until(deliveryTime, ChronoUnit.SECONDS);
        secondsLeft = Integer.valueOf((int)secondsUntilExpiration);
        System.out.println(now);
        return secondsLeft;
    }
}
