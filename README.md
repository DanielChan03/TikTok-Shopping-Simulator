# 🛍️TikTok Shop - Java Swing E-Commerce Application

## **1. Project Overview**

A comprehensive desktop e-commerce application built with Java Swing that simulates TikTok Shop functionality. This application provides a complete online shopping experience with user management, product catalog, shopping cart, wallet system, and order processing.

## **2. Contributors**

* Daniel Chan Zheng Uee - MMU Cyberjaya (Author)
* Chow Shu Ling - MMU Cyberjaya (Collaborator)
* Teo Hock Seng - MMU Cyberjaya (Collaborator)
* Lim Yu Xuan - MMU Cyberjaya (Collaborator)
  
## **3. Features**

### **👤User Authentication**

* User Registration & Login with email/password
* Password Recovery via OTP sent to registered phone number
* Profile Management with personal information and card details

### **👜Shopping Experience**

* Product Catalog with 9 categories and 100+ products
* Category Filtering for easy product discovery
* Shopping Cart with quantity management
* Stock Management with real-time inventory updates

### **💰Wallet & Payment System**

* Virtual Wallet with balance management
* Multiple Card Support for top-ups
* Transaction History tracking
* Voucher System with discount coupons

### **📦Order Management**

* Order Processing with status tracking
* Order History with detailed receipts
* Automatic Stock Deduction upon purchase

## **4. System Architecture**

### **MVC Pattern Implementation**
```
**Model (Data Layer)**
├── User (Customer information)
├── Product (Product catalog)
├── ShoppingCart (Cart management)
├── Order (Order processing)
├── Voucher (Discount system)
└── Transaction (Payment records)

**View (UI Layer)**
├── LoginView (Authentication)
├── MainShopView (Main interface)
└── ProfileDialog (User profile)

**Controller (Business Logic)**
└── ShopController (Orchestrates all operations)
```

## **5. Project Structure**
```
src/
├── controller/
│   └── ShopController.java
├── model/
│   ├── Model.java
│   ├── User.java
│   ├── Product.java
│   ├── ShoppingCart.java
│   ├── Order.java
│   ├── Voucher.java
│   ├── Transaction.java
│   └── Validation.java
└── view/
    ├── LoginView.java
    ├── MainShopView.java
    └── ProfileDialog.java
```

## **6. ⬇️Getting Started**

### **Prerequisites**

* ☕️Java JDK 8 or higher
* Any Java IDE (IntelliJ, Eclipse, VS Code)

### **Installation & Running**

1. Clone the repository
2. Open the project in your preferred IDE
3. Compile and run `ShopController.java`
4. Use demo accounts or register new users

### **Demo Accounts**

* Email: [johndoe@gmail.com](mailto:johndoe@gmail.com) | Password: password
* Email: [sarah@gmail.com](mailto:sarah@gmail.com) | Password: password

## **7. Key Features Detail**

### **🔍Product Categories**

1. Electronics - Smartphones, laptops, accessories
2. Fashion - Clothing, shoes, accessories
3. Food & Groceries - Daily essentials, snacks
4. Toys & Games - Board games, consoles, LEGO
5. Beauty & Personal Care - Skincare, cosmetics
6. Sports & Outdoors - Equipment, apparel
7. Home & Kitchen - Appliances, utensils
8. Stationery - Office supplies, writing tools
9. Others - Miscellaneous items

### **💳Wallet Features**

* Balance Management: Top-up via registered cards
* Card Management: Add/remove payment cards
* Transaction Tracking: Complete history of all financial activities
* Secure Payments: Balance validation before purchases

### **🛒Shopping Cart**

* Real-time Updates: Stock validation during product addition
* Quantity Management: Adjust quantities before checkout
* Voucher Application: Apply discount coupons
* Cart Persistence: Maintains items during session

## **8. Technical Implementation**

### **Data Validation**

* Email Validation: Standard email format verification
* Phone Validation: Malaysian phone number formats (011-XXXXXXX, 01X-XXXXXXX)
* Card Validation: 16-digit card number verification

### **Stock Management**

* Real-time stock deduction/restoration
* Out-of-stock product disabling
* Quantity limits based on available stock

### **Session Management**

* User session persistence
* Automatic cart clearing on logout
* Secure authentication flow

## **9. 🔐Security Features**

* Password protection with OTP recovery
* Card number masking in UI displays
* Session-based authentication
* Input validation and sanitization

## **10. 🔧Development Notes**

This project demonstrates:

* Object-Oriented Programming principles
* MVC architectural pattern
* Java Swing GUI development
* Event-driven programming
* Data persistence and management
* User interface design best practices

---

For any questions or issues regarding this project, please contact the contributors.
