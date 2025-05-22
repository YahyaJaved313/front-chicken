package com.noobcoder.chickenfront.forms;

import com.noobcoder.chickenfront.forms.AdminDashboardForm;
import com.noobcoder.chickenfront.forms.LoginForm;
import com.noobcoder.chickenfront.util.HttpClientUtil;

import javax.swing.*;
import java.awt.*;
import java.net.http.HttpResponse;

public class AdminLoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private JButton loginButton;
    private JButton backButton;

    public AdminLoginForm() {
        setTitle("AMS - Admin Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(15, 20, 22));

        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 20, 20));
        mainPanel.setBackground(new Color(15, 20, 22));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Admin Login", SwingConstants.CENTER);
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
        loginButton = new JButton("Login");
        ButtonEffects.applySlideOutEffect(loginButton);
        loginButton.addActionListener(e -> login());
        backButton = new JButton("Back to Login");
        ButtonEffects.applySlideOutEffect(backButton);
        backButton.addActionListener(e -> goToLogin());
        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel);

        messageLabel = new JLabel("", SwingConstants.CENTER);
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
            HttpClientUtil.setAuthCredentials(email, password);
            HttpResponse<String> response = HttpClientUtil.sendGetRequest("/admin/flights");
            if (response.statusCode() == 200) {
                messageLabel.setText("Admin login successful!");
                dispose();
                new AdminDashboardForm().setVisible(true);
            } else {
                messageLabel.setText("Admin login failed: Invalid credentials or insufficient permissions.");
                HttpClientUtil.clearAuthCredentials();
            }
        } catch (Exception e) {
            messageLabel.setText("Error: " + e.getMessage());
            HttpClientUtil.clearAuthCredentials();
        }
    }

    private void goToLogin() {
        dispose();
        new LoginForm().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminLoginForm().setVisible(true));
    }
}