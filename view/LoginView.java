package view;

import controller.ShopController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {

    private ShopController controller;
    
    public LoginView(ShopController controller) {
        this.controller = controller;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("🛍️ TikTok Shop - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300); // Increased height to accommodate new button
        setLocationRelativeTo(null);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Login Panel
        tabbedPane.addTab("Login", createLoginPanel());
        
        // Register Panel
        tabbedPane.addTab("Register", createRegisterPanel());
        
        add(tabbedPane);
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Main login form
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        formPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        formPanel.add(emailField);
        
        formPanel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        formPanel.add(passwordField);
        
        // Empty space for alignment
        formPanel.add(new JLabel());
        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                
                if (controller.login(email, password)) {
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(LoginView.this, "Invalid credentials!");
                }
            }
        });
        formPanel.add(loginBtn);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        // Forgot Password button at the bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton forgotPasswordBtn = new JButton("Forgot Password?");
        forgotPasswordBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showForgotPasswordDialog();
            }
        });
        bottomPanel.add(forgotPasswordBtn);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Email:"));
        JTextField regEmailField = new JTextField();
        panel.add(regEmailField);
        
        panel.add(new JLabel("Password:"));
        JPasswordField regPasswordField = new JPasswordField();
        panel.add(regPasswordField);
        
        panel.add(new JLabel("Phone:"));
        JTextField phoneField = new JTextField();
        panel.add(phoneField);
        
        JButton registerBtn = new JButton("Register");
        registerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = regEmailField.getText();
                String password = new String(regPasswordField.getPassword());
                String phone = phoneField.getText();
                
                if (controller.register(name, email, password, phone)) {
                    JOptionPane.showMessageDialog(null, "Registration successful!");
                    // no need for setVisible(false)/dispose here—controller already disposed it
                }
            }
        });
        
        panel.add(new JLabel());
        panel.add(registerBtn);
        
        return panel;
    }
    
    private void showForgotPasswordDialog() {
        JDialog forgotPasswordDialog = new JDialog(this, "Forgot Password", true);
        forgotPasswordDialog.setSize(400, 300);
        forgotPasswordDialog.setLocationRelativeTo(this);
        forgotPasswordDialog.setLayout(new BorderLayout(10, 10));
        forgotPasswordDialog.setResizable(false);
        
        // Main content panel
        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Phone number field
        mainPanel.add(new JLabel("Phone Number:"));
        JTextField phoneField = new JTextField();
        mainPanel.add(phoneField);
        
        // OTP field with Send button
        mainPanel.add(new JLabel("Verification Code:"));
        JPanel otpPanel = new JPanel(new BorderLayout(5, 0));
        JTextField otpField = new JTextField();
        JButton sendOtpBtn = new JButton("Send");
        sendOtpBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String phone = phoneField.getText().trim();
                if (phone.isEmpty()) {
                    JOptionPane.showMessageDialog(forgotPasswordDialog, 
                        "❌ Please enter your phone number first!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Call controller to send OTP
                if (controller.initiatePasswordReset(phone)) {
                    JOptionPane.showMessageDialog(forgotPasswordDialog, 
                        "✅ OTP sent! Check terminal for verification code.", "OTP Sent", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        otpPanel.add(otpField, BorderLayout.CENTER);
        otpPanel.add(sendOtpBtn, BorderLayout.EAST);
        mainPanel.add(otpPanel);
        
        // New password field
        mainPanel.add(new JLabel("New Password:"));
        JPasswordField newPasswordField = new JPasswordField();
        mainPanel.add(newPasswordField);
        
        // Empty space for alignment
        mainPanel.add(new JLabel());
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton resetBtn = new JButton("Reset Password");
        resetBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String phone = phoneField.getText().trim();
                String otp = otpField.getText().trim();
                String newPassword = new String(newPasswordField.getPassword()).trim();
                
                if (phone.isEmpty() || otp.isEmpty() || newPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(forgotPasswordDialog, 
                        "❌ Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Call controller to verify OTP and reset password
                if (controller.verifyOTPAndResetPassword(phone, otp, newPassword)) {
                    JOptionPane.showMessageDialog(forgotPasswordDialog, 
                        "✅ Password reset successfully! You can now login with your new password.", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    forgotPasswordDialog.dispose();
                }
            }
        });
        
        buttonPanel.add(resetBtn, BorderLayout.WEST);
        mainPanel.add(buttonPanel);

        forgotPasswordDialog.add(mainPanel, BorderLayout.SOUTH);
        forgotPasswordDialog.setVisible(true);
    }
}