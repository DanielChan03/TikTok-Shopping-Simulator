package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {

    private String name;
    private String email;
    private String password;
    private String gender;
    private String phone_num;
    private String address;
    private double acc_balance;
    private List<String> card_num; 
    private List<Order> orderHistory;
    private ShoppingCart cart;
    private int orderCounter = 0;
     private List<Transaction> transactionHistory;

    public User(String name, String email, String password, String phone_num){
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone_num = phone_num;
        this.acc_balance = 0;
        this.card_num = new ArrayList<String>();
        this.orderHistory = new ArrayList<>();
        this.cart = new ShoppingCart();
        this.transactionHistory = new ArrayList<>();
    }

    public User(String email, String password, String phone_num){
        this.email = email;
        this.password = password;
        this.phone_num = phone_num;
        this.acc_balance = 0;
        this.card_num = new ArrayList<String>();
        this.orderHistory = new ArrayList<>();
        this.cart = new ShoppingCart();  
    }

    public void setGender(String gender){
        this.gender = gender;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public boolean hasCard(String cardNum) { 
        return card_num.contains(cardNum); 
    }

    public boolean addCard(String cardNum){
        if (cardNum == null || cardNum.isBlank()) return false;
        if (card_num.contains(cardNum)) return false;
        return card_num.add(cardNum);
    }

    public boolean topUp(double amount, String cardNum){
        if (amount <= 0) return false;
        if (!card_num.contains(cardNum)) return false;
        this.acc_balance += amount;
        return true;
    }

    public void addOrder(Order order) {
        orderHistory.add(order);
    }
    
    public List<Order> getOrderHistory() {
        return new ArrayList<>(orderHistory);
    }

    // Getters
    public String getName(){ return name;}
    public String getEmail(){ return email; }
    public String getPassword(){ return password; }
    public String getGender(){ return gender; }
    public String getPhone_num(){ return phone_num; }
    public String getAddress(){ return address; }
    public double getAccBalance(){ return acc_balance; }
    public ArrayList<String> getCards(){return new ArrayList<>(this.card_num); }
    public ArrayList<String> getCard_num() {return new ArrayList<>(this.card_num); }
    
    // Add these setters
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNum(String phoneNum) { this.phone_num = phoneNum; }
    public void setPassword(String password) { this.password = password; }

    // Enhanced card deletion method
    public boolean deleteCard(String cardNum) {
        return card_num.remove(cardNum);
    }

    // Get user info as string for display
    public String getUserInfoSummary() {
        return String.format("Email: %s\nPhone: %s\nGender: %s\nAddress: %s\nBalance: $%.2f\nCards: %d",
                email, phone_num, gender != null ? gender : "Not set", 
                address != null ? address : "Not set", acc_balance, card_num.size());
    }

    public boolean deductBalance(double amount) {
        if (amount < 0 || amount > acc_balance) return false;
        acc_balance -= amount;
        return true;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void clearSessionData() {
        if (cart != null) {
            cart.clearCart();
        }
        // leave orderHistory and cards as account data
    }

    public int nextOrderId() {
        return ++orderCounter;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            this.transactionHistory.add(transaction);
        }
    }

    public List<Transaction> getTransactionHistory() {
        List<Transaction> copy = new ArrayList<>(transactionHistory);
        Collections.reverse(copy); 
        return copy;
    }
}