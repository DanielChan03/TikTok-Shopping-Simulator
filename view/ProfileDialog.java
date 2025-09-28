package view;

import controller.ShopController;
import java.awt.*;
import javax.swing.*;
import model.User;
import model.Validation;

public class ProfileDialog extends JDialog {
    private ShopController controller;
    private JTextField emailField, phoneField, addressField;
    private JRadioButton maleRadioButton, femaleRadioButton;
    private ButtonGroup genderGroup;
    private JList<String> cardList;
    private DefaultListModel<String> cardListModel;

    public ProfileDialog(Frame parent, ShopController controller) {
        super(parent, "👤 Personal Information", true);
        this.controller = controller;
        initializeUI();
        loadUserData();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(500, 600));

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Personal Information Section
        JPanel personalInfoPanel = createPersonalInfoPanel();
        mainPanel.add(personalInfoPanel, BorderLayout.NORTH);

        // Card Management Section
        JPanel cardPanel = createCardPanel();
        mainPanel.add(new JScrollPane(cardPanel), BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("📝 Personal Information"));

        panel.add(new JLabel("📧 Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("📱 Phone:"));
        phoneField = new JTextField();
        panel.add(phoneField);

       

        panel.add(new JLabel("🚻 Gender:"));
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        maleRadioButton = new JRadioButton("Male (M)");
        femaleRadioButton = new JRadioButton("Female (F)");
        
        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadioButton);
        genderGroup.add(femaleRadioButton);
        
        genderPanel.add(maleRadioButton);
        genderPanel.add(femaleRadioButton);
        panel.add(genderPanel);

        panel.add(new JLabel("🏠 Address:"));
        addressField = new JTextField();
        panel.add(addressField);

        return panel;
    }

    private JPanel createCardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("💳 Bank Cards"));

        // Card list
        cardListModel = new DefaultListModel<>();
        cardList = new JList<>(cardListModel);
        cardList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel cardButtonPanel = new JPanel(new FlowLayout());
        // JButton addCardBtn = new JButton("➕ Add New Card");
        JButton deleteCardBtn = new JButton("⛔ Delete Selected Card");

        // addCardBtn.addActionListener(e -> addNewCard());
        deleteCardBtn.addActionListener(e -> deleteSelectedCard());

        // cardButtonPanel.add(addCardBtn);
        cardButtonPanel.add(deleteCardBtn);

        panel.add(new JScrollPane(cardList), BorderLayout.CENTER);
        panel.add(cardButtonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton saveBtn = new JButton("💾 Save Changes");
        JButton cancelBtn = new JButton("❌ Cancel");

        saveBtn.setBackground(new Color(76, 175, 80));
        saveBtn.setForeground(Color.WHITE);
        cancelBtn.setBackground(new Color(244, 67, 54));
        cancelBtn.setForeground(Color.WHITE);

        saveBtn.addActionListener(e -> saveChanges());
        cancelBtn.addActionListener(e -> dispose());

        panel.add(cancelBtn);
        panel.add(saveBtn);

        return panel;
    }

    private void loadUserData() {
        User user = controller.getModel().getCurrentUser();
        if (user != null) {
            emailField.setText(user.getEmail());
            phoneField.setText(user.getPhone_num());
            addressField.setText(user.getAddress() != null ? user.getAddress() : "");

            // Set gender radio button
            if (user.getGender() != null) {
                String gender = user.getGender().toUpperCase();
                if (gender.equals("M") || gender.equals("MALE")) {
                    maleRadioButton.setSelected(true);
                } else if (gender.equals("F") || gender.equals("FEMALE")) {
                    femaleRadioButton.setSelected(true);
                }
            }

            // Load cards
            cardListModel.clear();
            for (String card : user.getCards()) {
                cardListModel.addElement(formatCardNumber(card));
            }
        }
    }

    private void deleteSelectedCard() {
        int selectedIndex = cardList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedCard = cardListModel.get(selectedIndex);
            String plainCardNumber = selectedCard.replaceAll("\\s", "").replaceAll("\\*", "");
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete card ending with " + plainCardNumber.substring(plainCardNumber.length() - 4) + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (controller.getModel().getCurrentUser().deleteCard(
                    controller.getModel().getCurrentUser().getCards().get(selectedIndex))) {
                    cardListModel.remove(selectedIndex);
                    JOptionPane.showMessageDialog(this, "✅ Card removed successfully!");
                    
                    // Notify parent to refresh wallet panel
                    notifyParentCardUpdated();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "❌ Please select a card to remove!");
        }
    }

    private void saveChanges() {
        User user = controller.getModel().getCurrentUser();
        if (user != null) {
            // Validate email
            String email = emailField.getText().trim();
            if (email.isEmpty() || !Validation.isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "❌ Please enter a valid email address!");
                return;
            }

            String phone = phoneField.getText().trim();
            if (!Validation.isValidPhone(phone)) {
                JOptionPane.showMessageDialog(this, "❌ Please enter a valid phone number!");
                return;
            }

            // Get selected gender
            String gender = "";
            if (maleRadioButton.isSelected()) {
                gender = "M";
            } else if (femaleRadioButton.isSelected()) {
                gender = "F";
            }

            if (!email.equals(user.getEmail())) {
                if (!controller.updateUserEmail(user, email)) {
                    JOptionPane.showMessageDialog(this, "❌ This email is already registered!");
                    return;
                }
            }

            // Save changes
            user.setPhoneNum(phoneField.getText().trim());
            user.setGender(gender);
            user.setAddress(addressField.getText().trim());


            JOptionPane.showMessageDialog(this, "✅ Profile updated successfully!");
            dispose();
            // Refresh main view
            if (getParent() instanceof MainShopView) {
                ((MainShopView) getParent()).updateUserInfo();
            }
        }
    }

    private String formatCardNumber(String cardNumber) {
        if (cardNumber.length() == 16) {
            return "**** **** **** " + cardNumber.substring(12);
        }
        return cardNumber; // Return as is if not 16 digits
    }
    // Add this method to notify the parent
private void notifyParentCardUpdated() {
    if (getParent() instanceof MainShopView) {
        ((MainShopView) getParent()).loadCurrentUserCards();
    }
}
}