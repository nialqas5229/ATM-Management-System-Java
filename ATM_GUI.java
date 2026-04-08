
package aTMProjec;
import javax.swing.SwingUtilities;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ATM_GUI extends JFrame {
    private Atm_application[] users;
    private Atm_application currentUser;
    private String filename = "accounts.txt";

    public ATM_GUI() {
        users = Atm_application.loadUsersFromFile(filename);
        showLoginScreen();
    }

    private void showLoginScreen() {
        JFrame loginFrame = new JFrame("ATM Login");
        loginFrame.setSize(500, 300);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new GridBagLayout()); // For better centering
        loginFrame.getContentPane().setBackground(Color.GREEN); // Set background color to green

        // Font for labels and fields
        Font labelFont = new Font("Times New Roman", Font.BOLD, 20);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(labelFont);

        JTextField userField = new JTextField(20); // Set text field size

        JLabel passLabel = new JLabel("PIN:");
        passLabel.setFont(labelFont);

        JPasswordField passField = new JPasswordField(10); // Set password field size

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(Color.RED);       // Red background for login button
        loginButton.setForeground(Color.WHITE);     // White text on the button
        loginButton.setFont(new Font("Times New Roman", Font.BOLD, 20)); // Button font

        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(Color.GRAY);       // Exit button background
        exitButton.setForeground(Color.WHITE);      // White text
        exitButton.setFont(new Font("Times New Roman", Font.BOLD, 20));

        // Layout positioning
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0; loginFrame.add(userLabel, gbc);
        gbc.gridx = 1; loginFrame.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; loginFrame.add(passLabel, gbc);
        gbc.gridx = 1; loginFrame.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; loginFrame.add(loginButton, gbc);
        gbc.gridx = 1; loginFrame.add(exitButton, gbc);

        // Button actions
        loginButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            for (Atm_application user : users) {
                if (user.authenticate(username, password)) {
                    currentUser = user;
                    JOptionPane.showMessageDialog(loginFrame, "Login Successful!");
                    loginFrame.dispose();
                    showMainMenu();
                    return;
                }
            }
            JOptionPane.showMessageDialog(loginFrame, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
        });

        exitButton.addActionListener(e -> System.exit(0));

        loginFrame.setLocationRelativeTo(null); // Center the window
        loginFrame.setVisible(true);            // Show the login screen
    }
    
    
    private void showMainMenu() {
        JFrame menuFrame = new JFrame("ATM Main Menu - Welcome " + currentUser.getName());
        menuFrame.setSize(500, 400);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLayout(new GridBagLayout());  // Better control for positioning
        menuFrame.getContentPane().setBackground(Color.GREEN); // Set green background

        Font buttonFont = new Font("Times New Roman", Font.BOLD, 20); // Font for buttons

        // Create buttons with consistent style
        JButton balanceButton = createStyledButton("Check Balance", buttonFont, Color.RED);
        JButton depositButton = createStyledButton("Deposit", buttonFont, Color.RED);
        JButton withdrawButton = createStyledButton("Withdraw", buttonFont, Color.RED);
        JButton pinChangeButton = createStyledButton("Change PIN", buttonFont, Color.RED);
        JButton transferButton = createStyledButton("Money Transfer", buttonFont, Color.RED);
        JButton exitButton = createStyledButton("Exit", buttonFont, Color.GRAY);

        // Add action listeners
        balanceButton.addActionListener(e -> checkBalance());
        depositButton.addActionListener(e -> depositMoney());
        withdrawButton.addActionListener(e -> withdrawMoney());
        pinChangeButton.addActionListener(e -> changePIN());
        transferButton.addActionListener(e -> moneyTransfer());
        exitButton.addActionListener(e -> {
            Atm_application.saveUsersToFile(users, filename);
            JOptionPane.showMessageDialog(menuFrame, "Thank you! Goodbye.");
            System.exit(0);
        });

        // Layout settings
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 10, 0); // Spacing between buttons
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        gbc.gridy = 0; menuFrame.add(balanceButton, gbc);
        gbc.gridy = 1; menuFrame.add(depositButton, gbc);
        gbc.gridy = 2; menuFrame.add(withdrawButton, gbc);
        gbc.gridy = 3; menuFrame.add(pinChangeButton, gbc);
        gbc.gridy = 4; menuFrame.add(transferButton, gbc);
        gbc.gridy = 5; menuFrame.add(exitButton, gbc);

        menuFrame.setLocationRelativeTo(null); // Center window
        menuFrame.setVisible(true);            // Show the main menu
    }
    private JButton createStyledButton(String text, Font font, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(font);                   // Set font
        button.setBackground(backgroundColor);  // Background color
        button.setForeground(Color.WHITE);      // Text color
        button.setFocusPainted(true);          // Remove focus border
        button.setPreferredSize(new Dimension(400, 70)); // Button size
        return button;
    }


    private void checkBalance() {
        JOptionPane.showMessageDialog(this, "Your balance: Rs. " + currentUser.getBalance());
    }

    private void depositMoney() {
        String input = JOptionPane.showInputDialog(this, "Enter deposit amount (Max: Rs. 50,000):");
        if (input != null && !input.isEmpty()) {
            try {
                double amount = Double.parseDouble(input);
                if (amount > 0 && amount <= 50000) {
                    currentUser.setBalance(currentUser.getBalance() + amount);
                    Atm_application.saveUsersToFile(users, filename);
                    JOptionPane.showMessageDialog(this, "Deposit successful! New balance: Rs. " + currentUser.getBalance());
                } else {
                    JOptionPane.showMessageDialog(this, "You can't Deposit Ammount greater than Rs.50,000 ", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void withdrawMoney() {
        String input = JOptionPane.showInputDialog(this, "Enter withdrawal amount (Max: Rs. 25,000):");
        if (input != null && !input.isEmpty()) {
            try {
                double amount = Double.parseDouble(input);
                if (amount > 0 && amount <= 25000 && amount <= currentUser.getBalance()) {
                    currentUser.setBalance(currentUser.getBalance() - amount);
                    Atm_application.saveUsersToFile(users, filename);
                    JOptionPane.showMessageDialog(this, "Withdrawal successful! New balance: Rs. " + currentUser.getBalance());
                } else if (amount > 25000) {
                    JOptionPane.showMessageDialog(this, "Withdrawal limit is Rs. 25,000.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient balance.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void changePIN() {
        String oldPin = JOptionPane.showInputDialog(this, "Enter current PIN:");
        if (oldPin != null && currentUser.checkPassword(oldPin)) {
            String newPin = JOptionPane.showInputDialog(this, "Enter new PIN:");
            String confirmPin = JOptionPane.showInputDialog(this, "Confirm new PIN:");

            if (newPin != null && newPin.equals(confirmPin)) {
                currentUser.setPassword(newPin);
                Atm_application.saveUsersToFile(users, filename);
                JOptionPane.showMessageDialog(this, "PIN changed successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "PINs do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect current PIN!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void moneyTransfer() {
        String recipientName = JOptionPane.showInputDialog(this, "Enter recipient username:");
        if (recipientName == null || recipientName.trim().isEmpty()) return;

        Atm_application recipient = null;
        for (Atm_application user : users) {
            if (user.getName().equals(recipientName) && !user.getName().equals(currentUser.getName())) {
                recipient = user;
                break;
            }
        }

        if (recipient == null) {
            JOptionPane.showMessageDialog(this, "Recipient not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String pin = JOptionPane.showInputDialog(this, "Enter your PIN to confirm:");
        if (pin != null && currentUser.checkPassword(pin)) {
            String amountStr = JOptionPane.showInputDialog(this, "Enter amount to transfer:");
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount > 0 && amount <= currentUser.getBalance()) {
                    currentUser.setBalance(currentUser.getBalance() - amount);
                    recipient.setBalance(recipient.getBalance() + amount);
                    Atm_application.saveUsersToFile(users, filename);
                    JOptionPane.showMessageDialog(this, "Transfer successful! New balance: Rs. " + currentUser.getBalance());
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient balance or invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect PIN!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ATM_GUI::new);
    }
}
