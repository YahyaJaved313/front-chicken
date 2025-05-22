package com.noobcoder.chickenfront.forms;


import javax.swing.*;
import java.awt.*;

public class FlightStatusForm extends JFrame {
    private JTextField flightNumberField;
    private JLabel statusLabel;
    private JButton checkButton;
    private JButton backButton;

    public FlightStatusForm() {
        setTitle("AMS - Check Flight Status");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(15, 20, 22)); // #0F1416

        // Main panel with GridLayout
        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 20, 20));
        mainPanel.setBackground(new Color(15, 20, 22));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Label
        JLabel titleLabel = new JLabel("Check Flight Status", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel);

        // Input Panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        inputPanel.setBackground(new Color(15, 20, 22));
        JLabel flightNumberLabel = new JLabel("Flight Number:");
        flightNumberLabel.setForeground(Color.WHITE);
        flightNumberLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        flightNumberField = new JTextField(15);
        checkButton = new JButton("Check Status");
        ButtonEffects.applySlideOutEffect(checkButton);
        checkButton.addActionListener(e -> checkStatus());
        inputPanel.add(flightNumberLabel);
        inputPanel.add(flightNumberField);
        inputPanel.add(checkButton);
        mainPanel.add(inputPanel);

        // Status Label
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        mainPanel.add(statusLabel);

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

    private void checkStatus() {
        String flightNumber = flightNumberField.getText();
        statusLabel.setText("Checking status for: " + flightNumber);
    }

    private void goToHome() {
        dispose();
        new HomeForm().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlightStatusForm().setVisible(true));
    }
}

