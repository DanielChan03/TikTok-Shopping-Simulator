package model;

import java.util.Date;

public class Transaction {
    private Date date;
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.date = new Date();
        this.type = type;
        this.amount = amount;
    }

    public Date getDate() { return date; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
}
