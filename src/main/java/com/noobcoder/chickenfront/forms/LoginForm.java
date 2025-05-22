package com.noobcoder.chickenfront.forms;

import com.noobcoder.chickenfront.util.HttpClientUtil;

import javax.swing.*;
import java.awt.*;
import java.net.http.HttpResponse;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public LoginForm() {
        setTitle("AMS - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(15, 20, 22));

        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 20, 20));
        mainPanel.setBackground(new Color(15, 20, 22));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel);

        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        usernamePanel.setBackground(new Color(15, 20, 22));
        JLabel usernameLabel = new JLabel("Email:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField = new JTextField(15);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        mainPanel.add(usernamePanel);

        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        passwordPanel.setBackground(new Color(15, 20, 22));
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField = new JPasswordField(15);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        mainPanel.add(passwordPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(15, 20, 22));
        JButton loginButton = new JButton("Login");
        ButtonEffects.applySlideOutEffect(loginButton);
        loginButton.addActionListener(e -> login());
        JButton adminLoginButton = new JButton("Admin Login");
        ButtonEffects.applySlideOutEffect(adminLoginButton);
        adminLoginButton.addActionListener(e -> {
            dispose();
            new AdminLoginForm().setVisible(true);
        });
        JButton registerButton = new JButton("Register");
        ButtonEffects.applySlideOutEffect(registerButton);
        registerButton.addActionListener(e -> goToRegister());
        buttonPanel.add(loginButton);
        buttonPanel.add(adminLoginButton);
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel);

        messageLabel = new JLabel("Log in or register to continue.", SwingConstants.CENTER);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
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
                new HomeForm().setVisible(true);
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