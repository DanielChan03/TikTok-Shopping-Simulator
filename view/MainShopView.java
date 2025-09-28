package view;

import controller.ShopController;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import model.*;

public class MainShopView extends JFrame {
    private ShopController controller;
    private JLabel userInfoLabel;
    private JTextArea cartArea;
    private JLabel totalLabel;
    private JComboBox<String> cardComboBox;
    private DefaultComboBoxModel<String> cardModel;
    private JTabbedPane tabbedPane;
    private JTextArea historyArea;

    // Unicode icons for buttons
    private final String SHOPBAG_ICON = "👜";
    private final String CART_ICON = "🛒";
    private final String WALLET_ICON = "💰";
    private final String HISTORY_ICON = "📋";
    private final String LOGOUT_ICON = "🚪";
    private final String ADD_ICON = "➕";
    private final String REMOVE_ICON = "➖";
    private final String TRASH_ICON = "🗑️";
    private final String CHECKOUT_ICON = "✅";
    private final String VOUCHER_ICON = "🎫";
    private final String REFRESH_ICON = "🔄";
    private final String CARD_ICON = "💳";
    private JButton profileBtn;

    public MainShopView(ShopController controller) {
        // Allows Mac and Win to show the same GUI
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.controller = controller;
        // initializeProductCatalog();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("🛍️ TikTok Shop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        // Create icon-based tabbed interface
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Add tab change listener to refresh when tabs are selected
        addTabChangeListener();

        // Shop Tab with icon
        tabbedPane.addTab(SHOPBAG_ICON + " Shop", createShopPanel());

        // Cart Tab with icon
        tabbedPane.addTab(CART_ICON + " Cart", createCartPanel());

        // Wallet Tab with icon
        tabbedPane.addTab(WALLET_ICON + " Wallet", createWalletPanel());

        // Order History Tab with icon
        tabbedPane.addTab(HISTORY_ICON + " Order", createOrderHistoryPanel());

         // Transaction History Tab with icon
        tabbedPane.addTab(CARD_ICON + " Transactions", createTransactionHistoryPanel());
         // Add tab change listener
        addTabChangeListener();

        add(tabbedPane, BorderLayout.CENTER);

        // User info panel with better styling
        JPanel userPanel = createUserPanel();
        add(userPanel, BorderLayout.NORTH);
    }

    public void refreshForNewUser() {
        if (tabbedPane != null) tabbedPane.setSelectedIndex(0);
        // Clear all user-specific data from the view
        updateCartDisplay();
        updateUserInfo();

        // Rebuild wallet card list for the new user
        populateCardComboBoxForCurrentUser();

        // Refresh the wallet tab
        refreshTab("Wallet", createWalletPanel());

        // (new) refresh/clear history text for the new user
        updateHistoryAreaForCurrentUser();

        // Clear any cached product data if needed
        // if (productCatalog != null) {
            // initializeProductCatalog();
        // }
    }

    private void addTabChangeListener() {
    tabbedPane.addChangeListener(e -> {
        int selectedIndex = tabbedPane.getSelectedIndex();
        if (selectedIndex >= 0) {
            String title = tabbedPane.getTitleAt(selectedIndex);
            if (title.contains("Wallet")) {
                SwingUtilities.invokeLater(() -> {
                    refreshTab("Wallet", createWalletPanel());
                });
            }
            else if (title.contains("Shop")) {
                SwingUtilities.invokeLater(() -> {
                    refreshTab("Shop", createShopPanel());
                });
            }
        }
    });
}

    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Left side container
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(240, 240, 240));
        
        profileBtn = new JButton("👤 Profile");
        profileBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        profileBtn.setBackground(new Color(70, 130, 180));
        profileBtn.setForeground(Color.WHITE);
        profileBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        profileBtn.addActionListener(e -> openProfileDialog());

        userInfoLabel = new JLabel();
        userInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Add profile button to WEST (left edge) and user info to CENTER (beside it)
        leftPanel.add(profileBtn, BorderLayout.WEST);
        leftPanel.add(userInfoLabel, BorderLayout.CENTER);

        // Right side: Logout button
        JButton logoutBtn = new JButton(LOGOUT_ICON + " Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        logoutBtn.setBackground(new Color(255, 100, 100));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.addActionListener(e -> controller.logout());

        // Add left and right panels to main panel
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(logoutBtn, BorderLayout.EAST);

        return panel;
    }

    private void openProfileDialog() {
        ProfileDialog profileDialog = new ProfileDialog(this, controller);
        profileDialog.setVisible(true);
        updateUserInfo();
    }

    public void updateUserInfo() {
        User user = controller.getModel().getCurrentUser();
        if (user != null) {
            String userInfo = String.format("<html><b>👤 Welcome, %s</b><br>"
                + "💰 RM %.2f | 💳 %d <html>", 
                user.getName(),  
                user.getAccBalance(), 
                user.getCards().size());

            userInfoLabel.setText(userInfo);
        }
    }

        private JPanel createShopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header with simple category filter
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JLabel headerLabel = new JLabel("👜 Available Products", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(headerLabel, BorderLayout.NORTH);

        // Simple category buttons - Only show existing categories
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        // "All" button (always show)
        JButton allButton = new JButton("All");
        allButton.addActionListener(e -> showAllProducts(panel));
        categoryPanel.add(allButton);

        // Add buttons only for categories that exist in the catalog
        java.util.Set<String> existingCategories = new java.util.LinkedHashSet<>();
        for (Product product : controller.getModel().getProductCatalog()) {
            existingCategories.add(product.getCategories());
        }

        for (String category : existingCategories) {
            JButton categoryButton = new JButton(category);
            categoryButton.addActionListener(e -> filterProductsByCategory(panel, category));
            categoryPanel.add(categoryButton);
        }
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JScrollPane categoryScroll = new JScrollPane(categoryPanel,
        JScrollPane.VERTICAL_SCROLLBAR_NEVER,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Slim horizontal scrollbar (e.g., 8px tall)
        categoryScroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8));

        headerPanel.add(categoryScroll, BorderLayout.SOUTH);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Initial product grid (show all)
        showAllProducts(panel);

        return panel;
    }

    private void showAllProducts(JPanel parentPanel) {
        // Remove existing product grid
        if (parentPanel.getComponentCount() > 1) {
            parentPanel.remove(1);
        }

        // Create product grid with ALL products
        JPanel productGrid = new JPanel(new GridLayout(0, 2, 15, 15));
        
        // Get the product catalog and shuffle it
        List<Product> shuffledProducts = new ArrayList<>(controller.getModel().getProductCatalog());
        Collections.shuffle(shuffledProducts); // This shuffles the list randomly

        for (Product product : shuffledProducts) {
            JPanel productCard = createProductCard(product);
            productGrid.add(productCard);
        }

        JScrollPane scrollPane = new JScrollPane(productGrid);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        parentPanel.add(scrollPane, BorderLayout.CENTER);

        parentPanel.revalidate();
        parentPanel.repaint();
    }

    private void filterProductsByCategory(JPanel parentPanel, String category) {
        // Remove existing product grid
        if (parentPanel.getComponentCount() > 1) {
            parentPanel.remove(1);
        }

        // Create product grid with filtered products
        JPanel productGrid = new JPanel(new GridLayout(0, 2, 15, 15));
        
        for (Product product : controller.getModel().getProductCatalog()) {
            if (product.getCategories().equals(category)) {
                JPanel productCard = createProductCard(product);
                productGrid.add(productCard);
            }
        }

        JScrollPane scrollPane = new JScrollPane(productGrid);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        parentPanel.add(scrollPane, BorderLayout.CENTER);

        parentPanel.revalidate();
        parentPanel.repaint();
    }

    private JPanel createProductCard(Product product) {
    JPanel card = new JPanel(new BorderLayout());
    card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));
    card.setBackground(Color.WHITE);

    // Product info
    JLabel nameLabel = new JLabel(product.getName());
    nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

    JLabel priceLabel = new JLabel("RM" + String.format("%.2f", product.getPrice()));
    priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
    priceLabel.setForeground(new Color(0, 100, 0));

    JLabel sellerLabel = new JLabel(product.getSellerName());
    sellerLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
    sellerLabel.setHorizontalAlignment(SwingConstants.RIGHT);

    // Create a main panel for name and price
    JPanel mainContentPanel = new JPanel(new BorderLayout());
    mainContentPanel.add(nameLabel, BorderLayout.NORTH);
    mainContentPanel.add(priceLabel, BorderLayout.CENTER);

    // Create a panel for the seller (to position it at bottom right)
    JPanel sellerPanel = new JPanel(new BorderLayout());
    sellerPanel.add(sellerLabel, BorderLayout.EAST);

    // Combine everything
    JPanel infoPanel = new JPanel(new BorderLayout());
    infoPanel.add(mainContentPanel, BorderLayout.CENTER);  // Name and price in center
    infoPanel.add(sellerPanel, BorderLayout.SOUTH);        // Seller at bottom right
        
    // Add to cart button
    // JButton addButton = new JButton(ADD_ICON + " Add to Cart");
    // addButton.setBackground(new Color(70, 130, 180));
    // addButton.setForeground(Color.WHITE);

    // Store reference to the ORIGINAL product from the catalog

    Product originalProduct = product;
    JButton addButton = new JButton(ADD_ICON + " Add to Cart");

    // updateProductCardButton(card, originalProduct);

    if (originalProduct.getStock() <= 0) {
        // Out of Stock button (disabled)
        addButton = new JButton("Out of Stock");
        addButton.setEnabled(false);
        addButton.setBackground(Color.GRAY);
        addButton.setForeground(Color.LIGHT_GRAY);
    } else {
        // Add to Cart button (enabled)
        addButton = new JButton(ADD_ICON + " Add to Cart");
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
    }

    addButton.addActionListener(e -> {
        int quantity = showQuantityDialog(originalProduct);
        if (quantity > 0) {
    
            // Deduct stock from the ORIGINAL catalog product
            originalProduct.decStock(quantity);

            // Create a new product instance for the cart, but don't pass stock
            // The cart product should just be a copy for display, stock is managed by the original
            Product productToAdd = new Product(originalProduct.getName(), originalProduct.getSellerName(), 
                                             0, originalProduct.getPrice(), originalProduct.getCategory());
            productToAdd.setVal(quantity);

            controller.getModel().getCurrentUser().getCart().addProduct(productToAdd);
            JOptionPane.showMessageDialog(this, "✅ Added to cart: " + originalProduct.getName());
            updateCartDisplay();

            if(originalProduct.getStock() <= 0){
                updateProductCardButton(card, originalProduct);
            }
        } 
    });

    card.add(infoPanel, BorderLayout.CENTER);
    card.add(addButton, BorderLayout.SOUTH);

    return card;
}

    private void updateProductCardButton(JPanel productCard, Product product) {
    // Find the add button in the product card
    Component[] components = productCard.getComponents();
    for (Component comp : components) {
        if (comp instanceof JButton) {
            JButton button = (JButton) comp;
            if (button.getText().contains("Add to Cart") || button.getText().contains("Out of Stock")) {
                if (product.getStock() <= 0) {
                    // Set as "Out of Stock"
                    button.setText("Out of Stock");
                    button.setEnabled(false);
                    button.setBackground(Color.GRAY);
                    button.setForeground(Color.LIGHT_GRAY);
                } else {
                    // Set as "Add to Cart"
                    button.setText(ADD_ICON + " Add to Cart");
                    button.setEnabled(true);
                    button.setBackground(new Color(70, 130, 180));
                    button.setForeground(Color.WHITE);
                }
                break;
            }
        }
    }
}

    private int showQuantityDialog(Product product) {
    // Refresh the product reference to get the latest stock
    Product refreshedProduct = findOriginalProduct(product.getName());
    if (refreshedProduct == null) {
        refreshedProduct = product; // Fallback to the passed product
    }
    
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    
    // Quantity selection components
    JLabel quantityLabel = new JLabel(refreshedProduct.getName() + ", Stock: " + refreshedProduct.getStock());
    quantityLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    
    // Set spinner limits based on CURRENT available stock
    int maxQuantity = Math.min(refreshedProduct.getStock(), 100);
    JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, maxQuantity, 1));
    quantitySpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));

    JPanel spinnerPanel = new JPanel(new FlowLayout());
    spinnerPanel.add(new JLabel("Quantity:"));
    spinnerPanel.add(quantitySpinner);
    
    panel.add(quantityLabel, BorderLayout.NORTH);
    panel.add(spinnerPanel, BorderLayout.CENTER);
    
    // Show dialog with options
    int result = JOptionPane.showConfirmDialog(
        this,
        panel,
        "Select Quantity",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE
    );
    
    if (result == JOptionPane.OK_OPTION) {
        return (int) quantitySpinner.getValue();
    } else {
        return 0;
    }
}

    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JLabel headerLabel = new JLabel(CART_ICON + " Shopping Cart", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(headerLabel, BorderLayout.NORTH);

        // Cart content
        cartArea = new JTextArea(10, 50);
        cartArea.setEditable(false);
        cartArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        cartArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(cartArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Total and buttons panel
        JPanel southPanel = new JPanel(new BorderLayout());

        totalLabel = new JLabel("Total: RM0.00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton removeBtn = createIconButton(REMOVE_ICON + " Remove Item", new Color(220, 100, 100));
        JButton checkoutBtn = createIconButton(CHECKOUT_ICON + " Checkout", new Color(0, 0, 0));
        JButton voucherBtn = createIconButton(VOUCHER_ICON + " Apply Voucher", new Color(0, 0, 0));

        removeBtn.addActionListener(e -> removeItemFromCart());
        checkoutBtn.addActionListener(e -> checkout());
        voucherBtn.addActionListener(e -> applyVoucher());
        // removeVoucherBtn.addActionListener(e -> removeVoucher());

        buttonPanel.add(removeBtn);
        buttonPanel.add(voucherBtn);
        // buttonPanel.add(removeVoucherBtn);
        buttonPanel.add(checkoutBtn);
        

        southPanel.add(totalLabel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(southPanel, BorderLayout.SOUTH);

        return panel;
    }

    public void removeItemFromCart() {
    ShoppingCart cart = controller.getModel().getCurrentUser().getCart();
    
    if (cart.getItems().isEmpty()) {
        JOptionPane.showMessageDialog(this, "❌ Cart is already empty!");
        return;
    }

    // Create list of item names for selection
    String[] itemNames = new String[cart.getItems().size()];
    for (int i = 0; i < cart.getItems().size(); i++) {
        Product cartProduct = cart.getItems().get(i);
        itemNames[i] = String.format("%s (Qty: %d) - RM%.2f", 
            cartProduct.getName(), cartProduct.getQuantity(), cartProduct.getPrice() * cartProduct.getQuantity());
    }

    // Show selection dialog
    String selectedItem = (String) JOptionPane.showInputDialog(
        this,
        "Select item to remove:",
        "Remove Item from Cart",
        JOptionPane.QUESTION_MESSAGE,
        null,
        itemNames,
        itemNames[0]
    );

    if (selectedItem != null) {
        // Find the selected product in the cart
        for (int i = 0; i < cart.getItems().size(); i++) {
            Product cartProduct = cart.getItems().get(i);
            if (selectedItem.contains(cartProduct.getName())) {

                // Extract quantity from the selected item string
                int startIndex = selectedItem.indexOf("Qty: ") + 5;
                int endIndex = selectedItem.indexOf(") - RM");
                String quantityStr = selectedItem.substring(startIndex, endIndex);
                int qtyToRestore = Integer.parseInt(quantityStr);

                // Find the ORIGINAL product from the catalog
                Product originalProduct = findOriginalProduct(cartProduct.getName());
                
                if (originalProduct != null) {
                    // Increase back the stock quantity on the ORIGINAL product
                    originalProduct.incStock(qtyToRestore);
                }
                
                // Remove the item from cart
                cart.getItems().remove(i);
                cart.removeProduct(cartProduct);
                
                // REFRESH THE SHOP TAB
                refreshTab("Shop", createShopPanel());
                
                // If cart becomes empty after removal, remove voucher
                if (cart.getItems().isEmpty() && cart.getAppliedVoucher() != null) {
                    cart.getAppliedVoucher().setApplied(false);
                    cart.removeVoucher();
                    JOptionPane.showMessageDialog(this, 
                         TRASH_ICON + " Item removed. Voucher cancelled since cart is empty.");
                } else {
                    JOptionPane.showMessageDialog(this, TRASH_ICON + " Item removed from cart. Stock restored.");
                }

                updateCartDisplay();
                return;
            }
        }
    }
}
    public void removeAllItems() {
    ShoppingCart cart = controller.getModel().getCurrentUser().getCart();
    
    if (cart.getItems().isEmpty()) {
        return;
    }
    
    // Restore stock for each item in the cart
    for (Product cartProduct : cart.getItems()) {
        // Find the ORIGINAL product from the catalog
        Product originalProduct = findOriginalProduct(cartProduct.getName());
        
        if (originalProduct != null) {
            // Increase back the stock quantity on the ORIGINAL product
            int qtyToRestore = cartProduct.getQuantity();
            originalProduct.incStock(qtyToRestore);
        }
    }
    
    // Clear the cart completely
    cart.getItems().clear();
    
    // Remove voucher if applied
    if (cart.getAppliedVoucher() != null) {
        cart.getAppliedVoucher().setApplied(false);
        cart.removeVoucher();
    }
}

    // Add this helper method to find the original product from the catalog
    private Product findOriginalProduct(String productName) {
        for (Product product : controller.getModel().getProductCatalog()) {
            if (product.getName().equals(productName)) {
                return product;
            }
        }
        return null;
    }

    // ---------------- REFRESH HELPER ---------------- //
    private void refreshTab(String tabKeyword, JPanel newPanel) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getTitleAt(i).contains(tabKeyword)) {
                tabbedPane.setComponentAt(i, newPanel);
                break;
            }
        }
    }
    // Update your createWalletPanel method to ensure fresh start each time:
    private JPanel createWalletPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel(WALLET_ICON + " Wallet Management", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(headerLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(4, 1, 10, 10));

        // Card management section
        JPanel cardSection = new JPanel(new BorderLayout());
        cardSection.setBorder(BorderFactory.createTitledBorder("💳 Card Management"));

        JPanel cardInputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        cardInputPanel.add(new JLabel("Card Number:"));
        JTextField cardField = new JTextField();
        cardField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JTextField textField = (JTextField) input;
                String text = textField.getText();
                return text.matches("\\d{0,16}");
            }
        });
        cardInputPanel.add(cardField);

        cardInputPanel.add(new JLabel("Card Nickname:"));
        JTextField nicknameField = new JTextField();
        cardInputPanel.add(nicknameField);

        JButton addCardBtn = createIconButton(ADD_ICON + " Add Card", new Color(70, 130, 180));
        addCardBtn.addActionListener(e -> {
            String cardNum = cardField.getText().trim();
            String nickname = nicknameField.getText().trim();
            
            // Validate card number length
            if (cardNum.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ Please enter a card number!");
                return;
            }
            
            if (cardNum.length() != 16) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Card number must be exactly 16 digits!\n" +
                    "Current length: " + cardNum.length() + " digits", 
                    "Invalid Card Number", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!cardNum.matches("\\d{16}")) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Card number must contain only digits (0-9)!", 
                    "Invalid Card Number", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            User currentUser = controller.getModel().getCurrentUser();
            if (currentUser != null) {
                currentUser.addCard(cardNum);
                
                String displayText = nickname.isEmpty() ? formatCardNumber(cardNum) : nickname + " (" + formatCardNumber(cardNum) + ")";
                cardModel.addElement(displayText);
                
                cardField.setText("");
                nicknameField.setText("");
                JOptionPane.showMessageDialog(this, "✅ Card added successfully!");
                updateUserInfo();
            }
        });

        cardSection.add(cardInputPanel, BorderLayout.NORTH);
        cardSection.add(addCardBtn, BorderLayout.SOUTH);

        // Card selection section - CRITICAL: Create NEW components each time
        JPanel cardSelectPanel = new JPanel(new BorderLayout());
        cardSelectPanel.setBorder(BorderFactory.createTitledBorder("💳 Select Card for Top-up"));

        // Create brand new model and combo box
        cardModel = new DefaultComboBoxModel<>();
        cardComboBox = new JComboBox<>(cardModel);
        cardSelectPanel.add(cardComboBox, BorderLayout.CENTER);

        // Load current user's cards into the NEW model
        loadCurrentUserCards();

        // Top-up section
        JPanel topupSection = new JPanel(new GridLayout(2, 2, 5, 5));
        topupSection.setBorder(BorderFactory.createTitledBorder("💰 Top-up Balance"));

        topupSection.add(new JLabel("Amount:"));
        JTextField amountField = new JTextField();
        topupSection.add(amountField);

        JButton topUpBtn = createIconButton("💳 Top Up", new Color(40, 160, 40));
        topUpBtn.addActionListener(e -> {
            if (cardComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "❌ Please add and select a card first!");
                return;
            }

            String amountText = amountField.getText().trim();
            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ Please enter an amount!");
                return;
            }

            try {
                double amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "❌ Amount must be greater than 0!");
                    return;
                }

                int selectedIndex = cardComboBox.getSelectedIndex();
                User user = controller.getModel().getCurrentUser();
                
                if (user != null && selectedIndex >= 0 && selectedIndex < user.getCards().size()) {
                    String cardNum = user.getCards().get(selectedIndex);
                    
                    double balanceBefore = user.getAccBalance();
                    user.topUp(amount, cardNum);
                    double balanceAfter = user.getAccBalance();
                    
                    if (balanceAfter > balanceBefore) {

                        Transaction transaction = new Transaction("Top-up", amount);
                        user.addTransaction(transaction); 

                        updateUserInfo();
                        amountField.setText("");
                        JOptionPane.showMessageDialog(this, "✅ Top-up successful! Added: RM" + amount);
                    } else {
                        JOptionPane.showMessageDialog(this, "❌ Top-up failed! Balance did not change.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Invalid card selection!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Invalid amount! Please enter a valid number.");
            }
        });

        topupSection.add(new JLabel());
        topupSection.add(topUpBtn);

        contentPanel.add(cardSection);
        contentPanel.add(cardSelectPanel);
        contentPanel.add(topupSection);

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    public void loadCurrentUserCards() {
    if (cardModel != null) {
        cardModel.removeAllElements();
        User user = controller.getModel().getCurrentUser();
        if (user != null && user.getCards() != null) {  
            for (String card : user.getCards()) {
                String formattedCard = formatCardNumber(card);
                cardModel.addElement(formattedCard);
            }
            
            if (user.getCards().size() > 0) {
                cardComboBox.setSelectedIndex(0);
            }
        }
    }
}

    private String formatCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() != 16) {
            return cardNumber;
        }
        return cardNumber.replaceAll("(\\d{4})(\\d{4})(\\d{4})(\\d{4})", "$1-$2-$3-$4");
    }

    private JPanel createOrderHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel(HISTORY_ICON + " Order History", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(headerLabel, BorderLayout.NORTH);

        historyArea = new JTextArea(15, 60); 
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Consolas", Font.PLAIN, 11));

        JButton refreshBtn = createIconButton(REFRESH_ICON + " Refresh History", new Color(100, 100, 100));
        refreshBtn.addActionListener(e -> refreshOrderHistory(historyArea));

        panel.add(new JScrollPane(historyArea), BorderLayout.CENTER);
        panel.add(refreshBtn, BorderLayout.SOUTH);

        // Initialize once for the current user
        updateHistoryAreaForCurrentUser();

        return panel;
    }
    
    private void updateHistoryAreaForCurrentUser() {
        if (historyArea == null) return;
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        User u = controller.getModel().getCurrentUser();
        if (u == null || u.getOrderHistory().isEmpty()) {
            sb.append("No order history found.\n");
            sb.append("Complete your first purchase to see orders here.");
        } else {
            sb.append("Your Order History:\n");
            sb.append("====================\n\n");
            for (Order order : u.getOrderHistory()) {
                sb.append(String.format("Order ID: #%d\n", order.getOrderId()));
                sb.append(String.format("Buyer: %s\n", u.getName()));
                sb.append(String.format("Address: %s\n", u.getAddress()));
                sb.append(String.format("Date: %s\n", sdf.format(order.getOrderDate())));
                sb.append(String.format("Total: RM%.2f\n", order.getTotalAmount()));
                sb.append(String.format("Status: %s\n", order.getStatus()));
                if (order.getAppliedVoucher() != null) {
                    sb.append(String.format("Voucher: -RM%.2f\n", order.getAppliedVoucher().getPrice()));
                }
                sb.append("Items:\n");
                for (Product item : order.getItems()) {
                    sb.append(String.format("  • %s - RM%.2f x %d\n", item.getName(), item.getPrice(), item.getQuantity()));
                }
                sb.append("--------------------\n\n");
            }
        }
        historyArea.setText(sb.toString());
    }

    private JButton createIconButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setFocusPainted(false);
        return button;
    }

    private void updateCartDisplay() {
        StringBuilder sb = new StringBuilder();
        ShoppingCart cart = controller.getModel().getCurrentUser().getCart();

        if (cart.getItems().isEmpty()) {
            sb.append("Your cart is empty.\n");
            sb.append("Visit the Shop tab to add products.");
        } else {
            sb.append("Items in your cart:\n");
            sb.append("====================\n");

            for (Product product : cart.getItems()) {
                sb.append(String.format("• %s\n", product.getName()));
                sb.append(String.format("  Price: RM%.2f\n", product.getPrice()));
                sb.append(String.format("  Quantity: %d\n\n", product.getQuantity()));
            }

            if (cart.getAppliedVoucher() != null) {
                sb.append(String.format("Voucher Applied: -RM%.2f\n", cart.getAppliedVoucher().getPrice()));
                sb.append("====================\n");
            }
        }

        cartArea.setText(sb.toString());
        totalLabel.setText(String.format("Total: RM%.2f", cart.getTotalPrice()));
    }

    private void refreshOrderHistory(JTextArea historyArea) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (controller.getModel().getCurrentUser().getOrderHistory().isEmpty()) {
            sb.append("No order history found.\n");
            sb.append("Complete your first purchase to see orders here.");
            JOptionPane.showMessageDialog(this, "📦 No order is placed!");
        } else {
            sb.append("Your Order History:\n");
            sb.append("====================\n\n");

            for (Order order : controller.getModel().getCurrentUser().getOrderHistory()) {
                sb.append(String.format("Order ID: #%d\n", order.getOrderId()));
                sb.append(String.format("Buyer: %s\n", controller.getModel().getCurrentUser().getName()));
                sb.append(String.format("Address: %s\n", controller.getModel().getCurrentUser().getAddress()));
                sb.append(String.format("Date: %s\n", sdf.format(order.getOrderDate())));
                sb.append(String.format("Total: RM%.2f\n", order.getTotalAmount()));
                sb.append(String.format("Status: %s\n", order.getStatus()));

                if (order.getAppliedVoucher() != null) {
                    sb.append(String.format("Voucher: -RM%.2f\n", order.getAppliedVoucher().getPrice()));
                }
                sb.append("Items:\n");
                for (Product item : order.getItems()) {
                    sb.append(String.format("  • %s - RM%.2f x %d\n", item.getName(), item.getPrice(), item.getQuantity()));
                }
                sb.append("--------------------\n\n");
            }
        }

        historyArea.setText(sb.toString());
    }

    private JPanel createTransactionHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel header = new JLabel("💳 Wallet Transaction History", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(header, BorderLayout.NORTH);

        JTextArea transactionArea = new JTextArea(15, 60);
        transactionArea.setEditable(false);
        transactionArea.setFont(new Font("Consolas", Font.PLAIN, 11));

        JButton refreshBtn = createIconButton("🔄 Refresh Transactions", new Color(100, 100, 100));
        refreshBtn.addActionListener(e -> updateTransactionArea(transactionArea));

        panel.add(new JScrollPane(transactionArea), BorderLayout.CENTER);
        panel.add(refreshBtn, BorderLayout.SOUTH);

        // Initialize with current user's data
        updateTransactionArea(transactionArea);

        return panel;
    }

    private void updateTransactionArea(JTextArea area) {
        User user = controller.getModel().getCurrentUser();
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (user == null || user.getTransactionHistory().isEmpty()) {
            sb.append("No wallet transactions found.\n");
        } else {
            sb.append("Your Wallet Transactions:\n");
            sb.append("=========================\n\n");
            for (Transaction t : user.getTransactionHistory()) {
                if( t.getType().equals("Paid")){
                sb.append(String.format("[%s] %s      :  -RM%.2f\n",
                        sdf.format(t.getDate()), t.getType(), t.getAmount()));
                }
                else {
                     sb.append(String.format("[%s] %s:  +RM%.2f\n",
                        sdf.format(t.getDate()), t.getType(), t.getAmount()));
                }
            }
        }
        area.setText(sb.toString());
    }

    private void checkout() {
        User user = controller.getModel().getCurrentUser();
        ShoppingCart cart = controller.getModel().getCurrentUser().getCart();

        if (user.getAccBalance() < cart.getTotalPrice()) {
            JOptionPane.showMessageDialog(this,
                    String.format("❌ Insufficient balance!\nAvailable: RM%.2f\nYou need RM%.2f more",
                            user.getAccBalance(), cart.getTotalPrice() - user.getAccBalance()));
            return;
        }

        if (user.getAddress() == null || user.getAddress().isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Address must be filled before placing order!");
            return;
        }

        if (cart.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Cart is empty!");
            return;
        }

        // Create order
        Order order = new Order(user.nextOrderId(), cart.getItems(), cart.getTotalPrice(), cart.getAppliedVoucher());
        user.addOrder(order);
        user.deductBalance(cart.getTotalPrice());
    
        Transaction transaction = new Transaction("Paid", cart.getTotalPrice() );
        user.addTransaction(transaction); 

        // remove used voucher
        cart.removeUsedVoucher(cart.getAppliedVoucher());
        cart.clearCart();
        
        updateCartDisplay();
        updateUserInfo();
        // Refresh the shop tab to update stock status
        refreshTab("Shop", createShopPanel());

        JOptionPane.showMessageDialog(this,
                String.format("✅ Order placed successfully!\nOrder ID: #%d\nTotal: RM%.2f",
                        order.getOrderId(), order.getTotalAmount()));
    }

    private void applyVoucher() {
    ShoppingCart cart = controller.getModel().getCurrentUser().getCart();
    
    if (cart.getItems().isEmpty()) {
        JOptionPane.showMessageDialog(this, "🎫 Cart is Empty");
        return;
    }
    
    // Get available vouchers from cart
    List<Voucher> vouchers = cart.getAvailableVouchers();
    Voucher currentlyApplied = cart.getAppliedVoucher();
    
    // Create dialog for voucher selection
    JDialog voucherDialog = new JDialog(this, "Voucher Management", true);
    voucherDialog.setLayout(new BorderLayout());
    voucherDialog.setSize(400, 350);
    voucherDialog.setLocationRelativeTo(this);
    
    // Header - show current applied voucher
    String headerText = currentlyApplied != null ? 
        "🎫 Currently Applied: RM" + currentlyApplied.getPrice() + " OFF" : 
        "🎫 Select a Voucher";
    JLabel headerLabel = new JLabel(headerText, JLabel.CENTER);
    headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
    voucherDialog.add(headerLabel, BorderLayout.NORTH);
    
    // Voucher selection panel
    JPanel voucherPanel = new JPanel();
    voucherPanel.setLayout(new BoxLayout(voucherPanel, BoxLayout.Y_AXIS));
    voucherPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    ButtonGroup voucherGroup = new ButtonGroup();
    JRadioButton noVoucherButton = new JRadioButton("No Voucher");
    voucherGroup.add(noVoucherButton);
    
    JPanel noVoucherCard = new JPanel(new BorderLayout());
    noVoucherCard.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    noVoucherCard.add(noVoucherButton, BorderLayout.CENTER);
    noVoucherCard.setPreferredSize(new Dimension(350, 40));
    
    voucherPanel.add(noVoucherCard);
    voucherPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    
    // Select "No Voucher" by default if no voucher is applied
    if (currentlyApplied == null) {
        noVoucherButton.setSelected(true);
    }
    
    for (Voucher voucher : vouchers) {
        JRadioButton radioButton = new JRadioButton(
            String.format("RM%.0f OFF - %s (Expires: %s)", 
                voucher.getPrice(), voucher.getRemark(), voucher.getExpiryDate())
        );
        radioButton.setActionCommand(voucher.getCode() + "");
        voucherGroup.add(radioButton);
        
        // Select this voucher if it's currently applied
        if (currentlyApplied != null && currentlyApplied.getCode() == voucher.getCode()) {
            radioButton.setSelected(true);
        }
        
        JPanel voucherCard = new JPanel(new BorderLayout());
        voucherCard.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        voucherCard.add(radioButton, BorderLayout.CENTER);
        voucherCard.setPreferredSize(new Dimension(350, 40));
        
        voucherPanel.add(voucherCard);
        voucherPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    }
    
    JScrollPane scrollPane = new JScrollPane(voucherPanel);
    voucherDialog.add(scrollPane, BorderLayout.CENTER);
    
    // Dynamic action button
    JPanel buttonPanel = new JPanel();
    JButton actionButton = new JButton();
    JButton cancelButton = new JButton("Cancel");
    
    // Update button text based on selection
    Runnable updateButtonText = () -> {
        String selectedCode = voucherGroup.getSelection() != null ? 
            voucherGroup.getSelection().getActionCommand() : null;
        
        if (selectedCode == null || noVoucherButton.isSelected()) {
            actionButton.setText("Done");
        } else {
                actionButton.setText("Apply Voucher");
        }
    };
    
    // Add listeners to all radio buttons to update button text dynamically
    noVoucherButton.addActionListener(e -> updateButtonText.run());
    for (Component comp : voucherPanel.getComponents()) {
        if (comp instanceof JPanel) {
            JPanel card = (JPanel) comp;
            Component[] cardComps = card.getComponents();
            for (Component cardComp : cardComps) {
                if (cardComp instanceof JRadioButton) {
                    ((JRadioButton) cardComp).addActionListener(e -> updateButtonText.run());
                }
            }
        }
    }
    
    // Initial button setup
    updateButtonText.run();
    
    actionButton.addActionListener(e -> {
        String selectedCode = voucherGroup.getSelection() != null ? 
            voucherGroup.getSelection().getActionCommand() : null;
        
        if (selectedCode == null || noVoucherButton.isSelected()) {
            // Remove voucher
            cart.removeVoucher();
            updateCartDisplay();
            voucherDialog.dispose();
            JOptionPane.showMessageDialog(this, "✅ No voucher selected.");
        } else {
            // Apply voucher
            Voucher selectedVoucher = null;
            for (Voucher voucher : vouchers) {
                if (String.valueOf(voucher.getCode()).equals(selectedCode)) {
                    selectedVoucher = voucher;
                    break;
                }
            }
            
            if (selectedVoucher != null) {
            
                    cart.applyVoucher(selectedVoucher);
                    
                    updateCartDisplay();
                    voucherDialog.dispose();
                    JOptionPane.showMessageDialog(this, 
                        "✅ Voucher applied! RM" + selectedVoucher.getPrice() + " discount added.");
            }
        }
    });
    
    cancelButton.addActionListener(e -> voucherDialog.dispose());
    
    buttonPanel.add(actionButton);
    buttonPanel.add(cancelButton);
    voucherDialog.add(buttonPanel, BorderLayout.SOUTH);
    
    voucherDialog.setVisible(true);
}

    private void populateCardComboBoxForCurrentUser() {
        User user = controller.getModel().getCurrentUser();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        if (user != null && user.getCard_num() != null && !user.getCard_num().isEmpty()) {
            for (String card : user.getCard_num()) {
                model.addElement(card);
            }
        } else {
            model.addElement("No cards");
        }
        cardComboBox.setModel(model);
        cardComboBox.setSelectedIndex(0);
    }
}
