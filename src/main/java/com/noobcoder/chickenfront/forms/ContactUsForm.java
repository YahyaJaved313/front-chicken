package com.noobcoder.chickenfront.forms;

import javax.swing.*;
import java.awt.*;

public class ContactUsForm extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JTextArea messageField;
    private JLabel confirmationLabel;
    private JButton submitButton;
    private JButton backButton;

    public ContactUsForm() {
        setTitle("AMS - Contact Us");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(15, 20, 22)); // #0F1416

        // Main panel with GridLayout
        JPanel mainPanel = new JPanel(new GridLayout(6, 1, 20, 20));
        mainPanel.setBackground(new Color(15, 20, 22));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Label
        JLabel titleLabel = new JLabel("Contact Us", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel);

        // Name Panel
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        namePanel.setBackground(new Color(15, 20, 22));
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nameField = new JTextField(15);
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        mainPanel.add(namePanel);

        // Email Panel
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        emailPanel.setBackground(new Color(15, 20, 22));
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailField = new JTextField(15);
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);
        mainPanel.add(emailPanel);

        // Message Panel
        JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        messagePanel.setBackground(new Color(15, 20, 22));
        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        messageField = new JTextArea(5, 15);
        messageField.setLineWrap(true);
        messageField.setWrapStyleWord(true);
        JScrollPane messageScrollPane = new JScrollPane(messageField);
        messagePanel.add(messageLabel);
        messagePanel.add(messageScrollPane);
        mainPanel.add(messagePanel);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(15, 20, 22));
        submitButton = new JButton("Submit");
        ButtonEffects.applySlideOutEffect(submitButton);
        submitButton.addActionListener(e -> submit());
        backButton = new JButton("Back to Home");
        ButtonEffects.applySlideOutEffect(backButton);
        backButton.addActionListener(e -> goToHome());
        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel);

        // Confirmation Label
        confirmationLabel = new JLabel("", SwingConstants.CENTER);
        confirmationLabel.setForeground(Color.WHITE);
        confirmationLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        mainPanel.add(confirmationLabel);

        add(mainPanel);
    }

    private void submit() {
        String name = nameField.getText();
        String email = emailField.getText();
        String message = messageField.getText();
        confirmationLabel.setText("Message submitted by: " + name);
    }

    private void goToHome() {
        dispose();
        new AirlineReservationDashboard().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ContactUsForm().setVisible(true));
    }
}