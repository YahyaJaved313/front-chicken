package com.noobcoder.chickenfront;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Random;

public class BookFlightForm extends JFrame {
    private JTextField flightNumberField;
    private JTextField passengerNameField;
    private JComboBox<String> genderCombo;
    private JTextField passportNumberField;
    private JLabel confirmationLabel;
    private JButton bookButton;
    private JButton backButton;

    public BookFlightForm() {
        setTitle("AMS - Book a Flight");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 350);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(15, 20, 22)); // #0F1416

        // Main panel with GridLayout
        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 20, 20));
        mainPanel.setBackground(new Color(15, 20, 22));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Label
        JLabel titleLabel = new JLabel("Book a Flight", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel);

        // Input Panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        inputPanel.setBackground(new Color(15, 20, 22));
        JLabel flightNumberLabel = new JLabel("Flight Number:");
        flightNumberLabel.setForeground(Color.WHITE);
        flightNumberLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        flightNumberField = new JTextField(10);
        JLabel passengerNameLabel = new JLabel("Passenger Name:");
        passengerNameLabel.setForeground(Color.WHITE);
        passengerNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passengerNameField = new JTextField(10);
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setForeground(Color.WHITE);
        genderLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JLabel passportNumberLabel = new JLabel("Passport Number:");
        passportNumberLabel.setForeground(Color.WHITE);
        passportNumberLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passportNumberField = new JTextField(10);
        bookButton = new JButton("Book Flight");
        ButtonEffects.applySlideOutEffect(bookButton);
        bookButton.addActionListener(e -> bookFlight());
        inputPanel.add(flightNumberLabel);
        inputPanel.add(flightNumberField);
        inputPanel.add(passengerNameLabel);
        inputPanel.add(passengerNameField);
        inputPanel.add(genderLabel);
        inputPanel.add(genderCombo);
        inputPanel.add(passportNumberLabel);
        inputPanel.add(passportNumberField);
        inputPanel.add(bookButton);
        mainPanel.add(inputPanel);

        // Confirmation Label
        confirmationLabel = new JLabel("", SwingConstants.CENTER);
        confirmationLabel.setForeground(Color.WHITE);
        confirmationLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        mainPanel.add(confirmationLabel);

        // Back Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(15, 20, 22));
        backButton = new JButton("Back to Home");
        ButtonEffects.applySlideOutEffect(backButton);
        backButton.addActionListener(e -> goToHome());
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel);

        add(mainPanel);
    }

    private void bookFlight() {
        String flightNumber = flightNumberField.getText();
        String passengerName = passengerNameField.getText();
        String gender = (String) genderCombo.getSelectedItem();
        String passportNumber = passportNumberField.getText();

        // Validate input
        if (flightNumber.isEmpty() || passengerName.isEmpty() || gender == null || passportNumber.isEmpty()) {
            confirmationLabel.setText("Please fill in all fields.");
            return;
        }

        // Payment Method Selection Dialog
        JComboBox<String> paymentMethodCombo = new JComboBox<>(new String[]{"Credit Card", "Debit Card", "PayPal", "Cash"});
        JTextField cardNumberField = new JTextField(10);
        JTextField expiryField = new JTextField(5);
        JTextField cvvField = new JTextField(3);
        JLabel cardNumberLabel = new JLabel("Card Number:");
        JLabel expiryLabel = new JLabel("Expiry (MM/YY):");
        JLabel cvvLabel = new JLabel("CVV:");
        JLabel cashMessageLabel = new JLabel("Please pay at the counter.", SwingConstants.CENTER);

        // Payment panel with dynamic fields
        JPanel paymentPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        paymentPanel.add(new JLabel("Payment Method:"));
        paymentPanel.add(paymentMethodCombo);
        paymentPanel.add(cardNumberLabel);
        paymentPanel.add(cardNumberField);
        paymentPanel.add(expiryLabel);
        paymentPanel.add(expiryField);
        paymentPanel.add(cvvLabel);
        paymentPanel.add(cvvField);
        paymentPanel.add(new JLabel()); // Placeholder
        paymentPanel.add(cashMessageLabel);

        // Initially hide cash message
        cashMessageLabel.setVisible(false);
        // Show/hide fields based on payment method
        paymentMethodCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedMethod = (String) paymentMethodCombo.getSelectedItem();
                    boolean isCash = "Cash".equals(selectedMethod);
                    cardNumberLabel.setVisible(!isCash);
                    cardNumberField.setVisible(!isCash);
                    expiryLabel.setVisible(!isCash);
                    expiryField.setVisible(!isCash);
                    cvvLabel.setVisible(!isCash);
                    cvvField.setVisible(!isCash);
                    cashMessageLabel.setVisible(isCash);
                    paymentPanel.revalidate();
                    paymentPanel.repaint();
                }
            }
        });

        int paymentResult = JOptionPane.showConfirmDialog(this, paymentPanel, "Payment Details", JOptionPane.OK_CANCEL_OPTION);
        if (paymentResult != JOptionPane.OK_OPTION) {
            confirmationLabel.setText("Payment cancelled.");
            return;
        }

        // Validate payment details (simplified for demo)
        String paymentMethod = (String) paymentMethodCombo.getSelectedItem();
        if (!"Cash".equals(paymentMethod)) {
            String cardNumber = cardNumberField.getText();
            String expiry = expiryField.getText();
            String cvv = cvvField.getText();
            if (cardNumber.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
                confirmationLabel.setText("Please fill in all payment details.");
                return;
            }
        }

        // Simulate successful payment
        confirmationLabel.setText("Payment successful for: " + passengerName);

        // Generate bill
        Random random = new Random();
        double billAmount = 100 + (random.nextDouble() * 900); // Random amount between $100 and $1000
        String bill = String.format(
                "===== Flight Booking Bill =====\n" +
                        "Flight Number: %s\n" +
                        "Passenger Name: %s\n" +
                        "Gender: %s\n" +
                        "Passport Number: %s\n" +
                        "Payment Method: %s\n" +
                        "Bill Amount: $%.2f\n" +
                        "Date: Monday, May 19, 2025\n" +
                        "Time: 05:39 PM PKT\n" +
                        "==============================",
                flightNumber, passengerName, gender, passportNumber, paymentMethod, billAmount
        );

        // Display bill
        JOptionPane.showMessageDialog(this, bill, "Booking Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }

    private void goToHome() {
        dispose();
        new HomeForm().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookFlightForm().setVisible(true));
    }
}
