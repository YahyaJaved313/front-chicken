package com.noobcoder.chickenfront.forms;

import javax.swing.*;
import java.awt.*;

public class UserProfileForm extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JLabel messageLabel;
    private JButton saveButton;
    private JButton backButton;

    // Theme colors from AdminDashboardForm
    private final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private final Color DARK_BLUE = new Color(23, 32, 42);
    private final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private final Color WHITE = Color.WHITE;

    public UserProfileForm() {
        setTitle("AMS - User Profile");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Main panel with GridLayout
        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title Label
        JLabel titleLabel = new JLabel("User Profile", SwingConstants.CENTER);
        titleLabel.setForeground(DARK_BLUE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        mainPanel.add(titleLabel);

        // Name Panel
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        namePanel.setBackground(BACKGROUND_COLOR);
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(DARK_BLUE);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField = new JTextField(8); // Reduced from 15 to 8
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField.setForeground(DARK_BLUE); // Explicitly set for consistency
        nameField.setPreferredSize(new Dimension(120, 25)); // Make more horizontal
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        mainPanel.add(namePanel);

        // Email Panel
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        emailPanel.setBackground(BACKGROUND_COLOR);
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(DARK_BLUE);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField = new JTextField(8); // Reduced from 15 to 8
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setForeground(DARK_BLUE); // Explicitly set for consistency
        emailField.setPreferredSize(new Dimension(120, 25)); // Make more horizontal
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);
        mainPanel.add(emailPanel);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        saveButton = new CustomStyledButton("Save Changes", 30, PRIMARY_BLUE, WHITE, 2);
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.addActionListener(e -> saveProfile());
        backButton = new CustomStyledButton("Back to Home", 30, PRIMARY_BLUE, WHITE, 2);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.addActionListener(e -> goToHome());
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel);

        // Message Label
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(DARK_BLUE);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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