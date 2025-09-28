package model;

public class Product {
    private String seller_name;
    private String name;
    private int stock;
    private int code;  // Remove static, make instance variable
    private static int codeCounter = 0;  // Separate counter
    private int quantity;
    private double price;
    private int category;

    public Product(String name, String seller_name, int stock, double price, int category){
        this.code = ++codeCounter;  // Proper unique code generation
        this.seller_name = seller_name;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.quantity = 0;  // Initialize quantity
        this.category = category;
    }

    public void setVal(int val){
        this.quantity = val;
    }

    public void decStock(int quantity){
        stock = stock - quantity;
    }

    public void incStock(int quantity){
        stock = stock + quantity;
    }

    public String getName(){ return name;}
    public double getPrice(){ return price;}
    public int getCode() { return code;}
    public int getQuantity() { return quantity;}
    public String getSellerName(){ return seller_name;}
    public int getStock(){ return stock;}
    public int getCategory() {return category;}

    public String getCategories(){
        switch(category){
            case 1: 
            return "Electronics";
            case 2:
            return "Fashion";
            case 3:
            return "Food & Groceries";
            case 4:
            return "Toys & Games";
            case 5:
            return "Beauty & Personal Care";
            case 6:
            return "Sports & Outdoors";
            case 7:
            return "Home & Kitchen";
            case 8:
            return "Stationery";
            default:
            return "Others";
        }
    }

    public void orderDetails(){
        System.out.println("Code "+ code);
        System.out.println("Product Name: " + getName());
        System.out.println("Price: " + getPrice());
        System.out.println("Quantity: "+ getQuantity());
    }
    
    @Override
    public String toString() {
        return name + " - $" + price + " (Qty: " + quantity + ")";
    }
}