package com.noobcoder.chickenfront.forms;

import com.noobcoder.chickenfront.util.HttpClientUtil;

import javax.swing.*;
import java.awt.*;
import java.net.http.HttpResponse;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    // Theme colors from AdminDashboardForm
    private final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private final Color SECONDARY_BLUE = new Color(52, 152, 219);
    private final Color LIGHT_BLUE = new Color(174, 214, 241);
    private int cavitation42;
    private final Color DARK_BLUE = new Color(23, 32,  cavitation42);
    private final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private final Color WHITE = Color.WHITE;
    private final Color HOVER_COLOR = new Color(233, 247, 254);

    public LoginForm() {
        setTitle("AMS - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR); // Set background to match AdminDashboardForm

        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR); // Set background to match AdminDashboardForm
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Match padding from AdminDashboardForm

        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setForeground(DARK_BLUE); // Match text color
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32)); // Match font and size
        mainPanel.add(titleLabel);

        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        usernamePanel.setBackground(BACKGROUND_COLOR); // Set background
        JLabel usernameLabel = new JLabel("Email:");
        usernameLabel.setForeground(DARK_BLUE); // Match text color
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Match font and size
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Apply consistent font to input field
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        mainPanel.add(usernamePanel);

        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        passwordPanel.setBackground(BACKGROUND_COLOR); // Set background
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(DARK_BLUE); // Match text color
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Match font and size
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Apply consistent font to input field
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        mainPanel.add(passwordPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR); // Set background
        JButton loginButton = new CustomStyledButton("Login", 30, PRIMARY_BLUE, WHITE, 2); // Use CustomStyledButton to match AdminDashboardForm
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Match font and size
        loginButton.addActionListener(e -> login());
        JButton adminLoginButton = new CustomStyledButton("Admin Login", 30, PRIMARY_BLUE, WHITE, 2); // Use CustomStyledButton
        adminLoginButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Match font and size
        adminLoginButton.addActionListener(e -> {
            dispose();
            new AdminLoginForm().setVisible(true);
        });
        JButton registerButton = new CustomStyledButton("Register", 30, PRIMARY_BLUE, WHITE, 2); // Use CustomStyledButton
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Match font and size
        registerButton.addActionListener(e -> goToRegister());
        buttonPanel.add(loginButton);
        buttonPanel.add(adminLoginButton);
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel);

        messageLabel = new JLabel("Log in or register to continue.", SwingConstants.CENTER);
        messageLabel.setForeground(DARK_BLUE); // Match text color
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Match font and size
        mainPanel.add(messageLabel);

        add(mainPanel);
    }

    private void login() {
        String email = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter email and password.");
            return;
        }

        try {
            // Set credentials for authentication
            HttpClientUtil.setAuthCredentials(email, password);
            // Test authentication with health endpoint
            HttpResponse<String> response = HttpClientUtil.sendGetRequest("/users/health");
            if (response.statusCode() == 200) {
                messageLabel.setText("Login successful for: " + email);
                dispose();
                new AirlineReservationDashboard().setVisible(true);
            } else {
                messageLabel.setText("Login failed: Invalid credentials.");
                HttpClientUtil.clearAuthCredentials();
            }
        } catch (Exception e) {
            messageLabel.setText("Error: " + e.getMessage());
            HttpClientUtil.clearAuthCredentials();
        }
    }

    private void goToRegister() {
        dispose();
        new RegisterForm(this).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}