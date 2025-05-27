package com.noobcoder.chickenfront.forms;

import javax.swing.*;
import java.awt.*;

public class FlightStatusForm extends JFrame {
    // Theme colors from AdminDashboardForm
    private static final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private static final Color DARK_BLUE = new Color(23, 32, 42);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color WHITE = Color.WHITE;

    private JTextField flightNumberField;
    private JLabel statusLabel;
    private JButton checkButton;
    private JButton backButton;

    public FlightStatusForm() {
        setTitle("AMS - Check Flight Status");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        JLabel titleLabel = new JLabel("Check Flight Status", SwingConstants.CENTER);
        titleLabel.setForeground(DARK_BLUE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Center panel for input and status
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        inputPanel.setBackground(BACKGROUND_COLOR);

        JLabel flightNumberLabel = new JLabel("Flight Number:");
        flightNumberLabel.setForeground(DARK_BLUE);
        flightNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        flightNumberField = new JTextField(8); // Kept column size at 8 for width
        flightNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        flightNumberField.setForeground(DARK_BLUE);
        flightNumberField.setCaretColor(DARK_BLUE);
        flightNumberField.setPreferredSize(new Dimension(120, 25)); // Adjusted to make it more horizontal (wider width, shorter height)

        checkButton = new CustomStyledButton("Check Status", 30, PRIMARY_BLUE, WHITE, 2);
        checkButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        checkButton.addActionListener(e -> checkStatus());

        inputPanel.add(flightNumberLabel);
        inputPanel.add(Box.createHorizontalStrut(10));
        inputPanel.add(flightNumberField);
        inputPanel.add(Box.createHorizontalStrut(20));
        inputPanel.add(checkButton);

        centerPanel.add(inputPanel);
        centerPanel.add(Box.createVerticalStrut(30));

        // Status Label centered
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setForeground(DARK_BLUE);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        statusPanel.setBackground(BACKGROUND_COLOR);
        statusPanel.add(statusLabel);

        centerPanel.add(statusPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Back Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        backButton = new CustomStyledButton("Back to Home", 30, PRIMARY_BLUE, WHITE, 2);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.addActionListener(e -> goToHome());
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void checkStatus() {
        String flightNumber = flightNumberField.getText().trim();
        if (flightNumber.isEmpty()) {
            statusLabel.setText("Please enter a flight number.");
            statusLabel.setForeground(Color.RED);
            return;
        }
        statusLabel.setText("Flight " + flightNumber + " is CONFIRMED.");
        statusLabel.setForeground(DARK_BLUE);
    }

    private void goToHome() {
        dispose();
        new AirlineReservationDashboard().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlightStatusForm().setVisible(true));
    }
}