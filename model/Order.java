package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private int orderId;
    private Date orderDate;
    private List<Product> items;
    private double totalAmount;
    private Voucher appliedVoucher;
    private String status;
    
    public Order(int orderId, List<Product> items, double totalAmount, Voucher voucher){
        this.orderId = orderId;
        this.orderDate = new Date();
        this.items = new ArrayList<>(items);
        this.totalAmount = totalAmount;
        this.appliedVoucher = voucher;
        this.status = "Processing";
    }
    
    // Getters
    public int getOrderId() { return orderId; }
    public Date getOrderDate() { return orderDate; }
    public List<Product> getItems() { return new ArrayList<>(items); }
    public double getTotalAmount() { return totalAmount; }
    public Voucher getAppliedVoucher() { return appliedVoucher; }
    public String getStatus() { return status; }
    
    public void setStatus(String status) {
        this.status = status;
    }
}