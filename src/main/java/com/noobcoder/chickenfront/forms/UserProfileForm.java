package com.noobcoder.chickenfront.forms;

import javax.swing.*;
import java.awt.*;

public class UserProfileForm extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JLabel messageLabel;
    private JButton saveButton;
    private JButton backButton;

    public UserProfileForm() {
        setTitle("AMS - User Profile");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(15, 20, 22)); // #0F1416

        // Main panel with GridLayout
        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 20, 20));
        mainPanel.setBackground(new Color(15, 20, 22));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Label
        JLabel titleLabel = new JLabel("User Profile", SwingConstants.CENTER);
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

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(15, 20, 22));
        saveButton = new JButton("Save Changes");
        ButtonEffects.applySlideOutEffect(saveButton);
        saveButton.addActionListener(e -> saveProfile());
        backButton = new JButton("Back to Home");
        ButtonEffects.applySlideOutEffect(backButton);
        backButton.addActionListener(e -> goToHome());
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel);

        // Message Label
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        mainPanel.add(messageLabel);

        add(mainPanel);
    }

    private void saveProfile() {
        String name = nameField.getText();
        String email = emailField.getText();
        messageLabel.setText("Profile update attempted for: " + email);
    }

    private void goToHome() {
        dispose();
        new AirlineReservationDashboard().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserProfileForm().setVisible(true));
    }
}