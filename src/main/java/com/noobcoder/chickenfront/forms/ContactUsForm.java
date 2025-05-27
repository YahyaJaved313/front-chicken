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

    // Theme colors from AdminDashboardForm
    private final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private final Color DARK_BLUE = new Color(23, 32, 42);
    private final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private final Color WHITE = Color.WHITE;

    public ContactUsForm() {
        setTitle("AMS - Contact Us");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR); // Updated to match AdminDashboardForm

        // Main panel with GridLayout
        JPanel mainPanel = new JPanel(new GridLayout(6, 1, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR); // Updated to match AdminDashboardForm
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Updated padding to match AdminDashboardForm

        // Title Label
        JLabel titleLabel = new JLabel("Contact Us", SwingConstants.CENTER);
        titleLabel.setForeground(DARK_BLUE); // Updated to match AdminDashboardForm
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32)); // Updated to match AdminDashboardForm
        mainPanel.add(titleLabel);

        // Name Panel
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        namePanel.setBackground(BACKGROUND_COLOR); // Updated to match AdminDashboardForm
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(DARK_BLUE); // Updated to match AdminDashboardForm
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match AdminDashboardForm
        nameField = new JTextField(15);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Consistent font for input
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        mainPanel.add(namePanel);

        // Email Panel
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        emailPanel.setBackground(BACKGROUND_COLOR); // Updated to match AdminDashboardForm
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(DARK_BLUE); // Updated to match AdminDashboardForm
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match AdminDashboardForm
        emailField = new JTextField(15);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Consistent font for input
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);
        mainPanel.add(emailPanel);

        // Message Panel
        JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        messagePanel.setBackground(BACKGROUND_COLOR); // Updated to match AdminDashboardForm
        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setForeground(DARK_BLUE); // Updated to match AdminDashboardForm
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match AdminDashboardForm
        messageField = new JTextArea(2, 15);
        messageField.setLineWrap(true);
        messageField.setWrapStyleWord(true);
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Consistent font for text area
        JScrollPane messageScrollPane = new JScrollPane(messageField);
        messagePanel.add(messageLabel);
        messagePanel.add(messageScrollPane);
        mainPanel.add(messagePanel);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR); // Updated to match AdminDashboardForm
        submitButton = new CustomStyledButton("Submit", 30, PRIMARY_BLUE, WHITE, 2); // Updated to match AdminDashboardForm
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Updated to match AdminDashboardForm
        submitButton.addActionListener(e -> submit());
        backButton = new CustomStyledButton("Back to Home", 30, PRIMARY_BLUE, WHITE, 2); // Updated to match AdminDashboardForm
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Updated to match AdminDashboardForm
        backButton.addActionListener(e -> goToHome());
        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel);

        // Confirmation Label
        confirmationLabel = new JLabel("", SwingConstants.CENTER);
        confirmationLabel.setForeground(DARK_BLUE); // Updated to match AdminDashboardForm
        confirmationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match AdminDashboardForm
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