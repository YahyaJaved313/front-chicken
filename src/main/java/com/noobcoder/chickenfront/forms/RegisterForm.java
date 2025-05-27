package com.noobcoder.chickenfront.forms;

import com.noobcoder.chickenfront.util.HttpClientUtil;

import javax.swing.*;
import java.awt.*;
import org.json.JSONObject;
import java.net.http.HttpResponse;

public class RegisterForm extends JFrame {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private JButton registerButton;
    private JButton backButton;
    private LoginForm loginForm;

    // Theme colors from LoginForm (matching AdminDashboardForm)
    private final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private final Color DARK_BLUE = new Color(23, 32, 42);
    private final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private final Color WHITE = Color.WHITE;

    public RegisterForm(LoginForm loginForm) {
        this.loginForm = loginForm;
        setTitle("AMS - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR); // Updated to match LoginForm

        JPanel mainPanel = new JPanel(new GridLayout(6, 1, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR); // Updated to match LoginForm
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Updated padding to match LoginForm

        JLabel titleLabel = new JLabel("Register", SwingConstants.CENTER);
        titleLabel.setForeground(DARK_BLUE); // Updated to match LoginForm
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32)); // Updated to match LoginForm
        mainPanel.add(titleLabel);

        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        usernamePanel.setBackground(BACKGROUND_COLOR); // Updated to match LoginForm
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(DARK_BLUE); // Updated to match LoginForm
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match LoginForm
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Consistent font for input
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        mainPanel.add(usernamePanel);

        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        emailPanel.setBackground(BACKGROUND_COLOR); // Updated to match LoginForm
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(DARK_BLUE); // Updated to match LoginForm
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match LoginForm
        emailField = new JTextField(15);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Consistent font for input
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);
        mainPanel.add(emailPanel);

        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        passwordPanel.setBackground(BACKGROUND_COLOR); // Updated to match LoginForm
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(DARK_BLUE); // Updated to match LoginForm
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match LoginForm
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Consistent font for input
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        mainPanel.add(passwordPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR); // Updated to match LoginForm
        registerButton = new CustomStyledButton("Register", 30, PRIMARY_BLUE, WHITE, 2); // Updated to match LoginForm
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Updated to match LoginForm
        registerButton.addActionListener(e -> register());
        backButton = new CustomStyledButton("Back to Login", 30, PRIMARY_BLUE, WHITE, 2); // Updated to match LoginForm
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Updated to match LoginForm
        backButton.addActionListener(e -> goToLogin());
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(DARK_BLUE); // Updated to match LoginForm
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match LoginForm
        mainPanel.add(messageLabel);

        add(mainPanel);
    }

    private void register() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill all fields.");
            return;
        }

        try {
            JSONObject user = new JSONObject();
            user.put("name", username);
            user.put("email", email);
            user.put("password", password);
            user.put("role", "customer"); // Assuming default role

            HttpResponse<String> response = HttpClientUtil.sendPostRequest("/users/register", user.toString());
            if (response.statusCode() == 200) {
                messageLabel.setText("Registration successful for: " + email);
                dispose();
                loginForm.setVisible(true);
            } else {
                messageLabel.setText("Registration failed: " + response.body());
            }
        } catch (Exception e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    private void goToLogin() {
        dispose();
        loginForm.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterForm(new LoginForm()).setVisible(true));
    }
}