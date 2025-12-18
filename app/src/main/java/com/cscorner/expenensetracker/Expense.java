package com.cscorner.expenensetracker;

public class Expense {
    private String id;
    private double amount;
    private String currency;
    private String category;
    private String remark;
    private String date;

    public Expense() { }

    public Expense(double amount, String currency, String category, String remark, String date) {
        this.amount = amount;
        this.currency = currency;
        this.category = category;
        this.remark = remark;
        this.date = date;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
