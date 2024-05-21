package com.example.egringotts;

import java.util.Date;

public class transaction {
    private String username;
    private String receiverUsername;
    private Double amount;
    private String currency;
    private String category;
    private Date date;

    public transaction(String username, String receiverUsername, Double amount, String currency, String category) {
        this.username = username;
        this.receiverUsername = receiverUsername;
        this.amount = amount;
        this.currency = currency;
        this.category = category;
        this.date = new Date();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }
}
