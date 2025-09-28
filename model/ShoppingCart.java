package model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<Product> items;
    private Voucher appliedVoucher;
    private List<Voucher> availableVouchers; // Add this line
    
    public ShoppingCart() {
        this.items = new ArrayList<>();
        this.availableVouchers = new ArrayList<>(); // Add this line
        initializeVouchers(); // Add this line
    }
    
    // Add this method to initialize available vouchers
    private void initializeVouchers() {
        availableVouchers.add(new Voucher(5.0, true, "RM5 Off", "2025-12-31"));
        availableVouchers.add(new Voucher(10.0, true, "RM10 Off", "2025-12-31"));
        availableVouchers.add(new Voucher(15.0, true, "RM15 Off", "2025-12-31"));
        availableVouchers.add(new Voucher(20.0, true, "RM20 Off", "2025-12-31"));
    }
    
    // Add this getter method
    public List<Voucher> getAvailableVouchers() {
        return new ArrayList<>(availableVouchers);
    }
    
    // Rest of your existing methods remain the same
    public void addProduct(Product product) {
        items.add(product);
    }
    
    public void removeProduct(Product product) {
        items.remove(product);
    }
    
    public List<Product> getItems() {
        return new ArrayList<>(items);
    }
    
    public double getTotalPrice() {
        double total = items.stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();
        if (appliedVoucher != null) {
            total -= appliedVoucher.getPrice();
        }
        return Math.max(total, 0);
    }
    
    public void applyVoucher(Voucher voucher) {
        if(!items.isEmpty())
            this.appliedVoucher = voucher;
    }
    
    public void removeVoucher() {
        this.appliedVoucher = null;
    }

    public void removeUsedVoucher(Voucher usedVoucher){
        availableVouchers.remove(usedVoucher);
    }
    
    public Voucher getAppliedVoucher() {
        return appliedVoucher;
    }
    
    public void clearCart() {
        items.clear();
        appliedVoucher = null;
    }
}