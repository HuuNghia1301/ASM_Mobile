package com.example.asm_ad;

public class Budget {
    private long id;
    private long userId;
    private double amount;
    private String category;
    private String createdAt;

    // Constructor mặc định
    public Budget() {}

    // Constructor đầy đủ
    public Budget(long id, long userId, double amount, String category, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.createdAt = createdAt;
    }

    // Constructor mới cho BudgetAdapter
    public Budget(String category, double amount) {
        this.category = category;
        this.amount = amount;
    }

    // Getter và Setter
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
