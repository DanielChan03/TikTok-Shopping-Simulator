package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.JOptionPane;

import model.*;
import view.*;

public class ShopController {
    private Model model;
    private LoginView loginView;
    private MainShopView mainView;
    private Map<String, User> userDatabase; // Store users by email
    private Map<String, String> pendingResets = new HashMap<>(); // phone -> OTP
    
    public ShopController(Model model) {
        this.model = model;
        this.userDatabase = new HashMap<String, User>();
        initializeDemoUsers();  // add demo users
        // model.initializeProductCatalog();
        showLoginView();
    }

    public Model getModel() {
        return model;
    }
    
     private void initializeDemoUsers() {
        // Create different demo users
        User user1 = new User("John Doe", "johndoe@gmail.com", "password", "0123456789");
        user1.setAddress("123 Main St");
        user1.addCard("1111222233334444");
        user1.topUp(1000.0, "1111222233334444"); // Add demo balance
        userDatabase.put("johndoe@gmail.com", user1);
        
        User user2 = new User("Sarah", "sarah@gmail.com", "password", "0198765432");
        user2.setAddress("456 Oak Ave");
        user2.addCard("5555666677778888");
        user2.topUp(500.0, "5555666677778888");
        userDatabase.put("sarah@gmail.com", user2);
    }

    private void onUserSessionStart(User u) {
        // 1) Set the current user in the model
        model.setCurrentUser(u);

        // 2) Ensure MainShopView exists
        if (mainView == null) {
            mainView = new MainShopView(this);
        }

        // 3) Show main view
        mainView.setVisible(true);

        // 4) IMPORTANT: refresh UI so Wallet/ComboBox is rebuilt per current user
        // (this should repopulate cards, labels, cart, etc.)
        mainView.refreshForNewUser();

        // 5) (Optional) hide/dispose login window if it’s still open
        if (loginView != null) {
            loginView.dispose();
            loginView = null;  
        }
    }

    public void showLoginView() {
        loginView = new LoginView(this);
        loginView.setVisible(true);
    }
    
    public void showMainView() {
        if (mainView == null) {
            mainView = new MainShopView(this);
        }
        mainView.setVisible(true);
        // Always refresh the UI with the current user (instead of just updateUserInfo)
        mainView.refreshForNewUser();
    }
    
    public boolean login(String email, String password) {
        User user = userDatabase.get(email);
        if (user != null && user.getPassword().equals(password)) {
            onUserSessionStart(user); 
            return true;
        }
        return false;
    }
    
    public boolean register(String name, String email, String password, String phone) {
        if (userDatabase.containsKey(email)) {
            JOptionPane.showMessageDialog(null, "❌ Email already registered!", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false; // User already exists
        }
        
        // Validate input
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(null, "❌ Please fill in all fields!", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!Validation.isValidEmail(email)) {
            JOptionPane.showMessageDialog(null, "❌ Please enter a valid email!", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validation.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(null, "❌ Please enter a valid phone number!", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        User newUser = new User(name, email, password, phone);
        userDatabase.put(email, newUser);
        onUserSessionStart(newUser); 
        return true;
    }

    public void logout() {

        User currentUser = model.getCurrentUser();
        // Restore stock for all cart items before logging out
        if (currentUser != null && mainView != null) {
            mainView.removeAllItems(); // This will restore all stock
            currentUser.clearSessionData();
        }

        if (mainView != null) mainView.refreshForNewUser();
        
        model.setCurrentUser(null);

        if (mainView != null) {
            mainView.setVisible(false);
            mainView.dispose();
            mainView = null;
        }

        // keep the reference so onUserSessionStart can dispose it next time
        loginView = new LoginView(this);
        loginView.setVisible(true);
    }

    // Method 1: Initiate password reset (send OTP)
    public boolean initiatePasswordReset(String phone) {
        // Check if phone exists
        boolean phoneExists = userDatabase.values().stream()
            .anyMatch(user -> phone.equals(user.getPhone_num()));
        
        if (!phoneExists) {
            JOptionPane.showMessageDialog(null, "❌ No account found with this phone number!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Generate OTP
        Random random = new Random();
        String otp = String.format("%06d", random.nextInt(1000000));
        
        // Store OTP for verification later
        pendingResets.put(phone, otp);
        
        // Show in terminal (instead of sending SMS)
        System.out.println("Password reset OTP for " + phone + ": " + otp);
        
        return true;
    }

    // Method 2: Verify OTP and reset password
    public boolean verifyOTPAndResetPassword(String phone, String verification_code, String newPassword) {
        // Get stored OTP
        String storedOTP = pendingResets.get(phone);
        
        if (storedOTP == null) {
            JOptionPane.showMessageDialog(null, "❌ No pending password reset for this phone!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Verify OTP
        if (storedOTP.equals(verification_code)) {
            // Find user and update password
            for (User user : userDatabase.values()) {
                if (user.getPhone_num().equals(phone)) {
                    user.setPassword(newPassword);
                    pendingResets.remove(phone); // Clear used OTP
                    return true;
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "❌ Incorrect Verification Code!", "Verification Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return false;
    }

    public boolean updateUserEmail(User user, String newEmail) {
        if (user == null || newEmail == null || newEmail.isBlank()) return false;

        // Reject if new email is already used by another account
        if (userDatabase.containsKey(newEmail)) return false;

        // Remove old mapping and add the new one
        userDatabase.remove(user.getEmail());
        user.setEmail(newEmail);
        userDatabase.put(newEmail, user);
        return true;
    }
    
    public static void main(String[] args) {
        Model model = new Model();
        new ShopController(model);
    }
}