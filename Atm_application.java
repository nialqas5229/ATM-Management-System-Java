package aTMProjec;

import java.io.*;
import java.util.*;

public class Atm_application {
    private String name;
    private String password;
    private double balance;

    // Constructor
    public Atm_application(String name, String password, double balance) {
        this.name = name;
        this.password = password;
        this.balance = balance;
    }

    // Authentication method
    public boolean authenticate(String inputName, String inputPassword) {
        return this.name.equals(inputName) && this.password.equals(inputPassword);
    }

    // Check if input password matches current password
    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    // Change password
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    // Get current balance
    public double getBalance() {
        return this.balance;
    }

    // Deposit amount
    public boolean deposit(double amount) {
        if (amount > 0 && amount <= 50000) {
            this.balance += amount;
            return true;
        }
        return false;
    }

    // Withdraw amount
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= 25000 && amount <= this.balance) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    // Transfer amount to another user
    public boolean transfer(Atm_application recipient, double amount) {
        if (this.withdraw(amount)) {
            recipient.balance += amount;
            return true;
        }
        return false;
    }

    // Load users from file
    public static Atm_application[] loadUsersFromFile(String filename) {
        List<Atm_application> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    String password = parts[1];
                    double balance = Double.parseDouble(parts[2]);
                    users.add(new Atm_application(name, password, balance));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading user data: " + e.getMessage());
        }
        return users.toArray(new Atm_application[0]);
    }

    // Save users to file
    public static void saveUsersToFile(Atm_application[] users, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Atm_application user : users) {
                writer.write(user.name + "," + user.password + "," + user.balance);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for balance (if needed for GUI)
    public void setBalance(double balance) {
        this.balance = balance;
   
    }
    
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new ATM_GUI()); // Launches the GUI
    }

}

