package com.noobcoder.chickenfront.forms;

import javax.swing.*;
import java.awt.*;

public class FlightStatusForm extends JFrame {
    // Theme colors
    private static final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private static final Color DARK_BG = new Color(15, 20, 22);
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
        getContentPane().setBackground(DARK_BG);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        JLabel titleLabel = new JLabel("Check Flight Status", SwingConstants.CENTER);
        titleLabel.setForeground(WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Center panel for input and status
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(DARK_BG);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        inputPanel.setBackground(DARK_BG);

        JLabel flightNumberLabel = new JLabel("Flight Number:");
        flightNumberLabel.setForeground(WHITE);
        flightNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        flightNumberField = new JTextField(12);
        flightNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        flightNumberField.setBackground(new Color(30, 35, 38));
        flightNumberField.setForeground(WHITE);
        flightNumberField.setCaretColor(WHITE);
        flightNumberField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_BLUE, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        checkButton = new JButton("Check Status");
        styleButton(checkButton);
        checkButton.addActionListener(e -> checkStatus());

        inputPanel.add(flightNumberLabel);
        inputPanel.add(Box.createHorizontalStrut(10));
        inputPanel.add(flightNumberField);
        inputPanel.add(Box.createHorizontalStrut(20)); // Spacing between input and button
        inputPanel.add(checkButton);

        centerPanel.add(inputPanel);
        centerPanel.add(Box.createVerticalStrut(30)); // More space before status

        // Status Label centered
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setForeground(WHITE);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        statusPanel.setBackground(DARK_BG);
        statusPanel.add(statusLabel);

        centerPanel.add(statusPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Back Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(DARK_BG);
        backButton = new JButton("Back to Home");
        styleButton(backButton);
        backButton.addActionListener(e -> goToHome());
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void styleButton(JButton button) {
        button.setBackground(PRIMARY_BLUE);
        button.setForeground(WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 22, 8, 22));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(PRIMARY_BLUE, 1, true));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_BLUE.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_BLUE);
            }
        });
    }

    private void checkStatus() {
        String flightNumber = flightNumberField.getText().trim();
        if (flightNumber.isEmpty()) {
            statusLabel.setText("Please enter a flight number.");
            statusLabel.setForeground(Color.RED);
            return;
        }
        statusLabel.setText("Flight " + flightNumber + " is CONFIRMED.");
        statusLabel.setForeground(new Color(46, 204, 113)); // Green
    }

    private void goToHome() {
        dispose();
        // Uncomment and implement this if you have a dashboard to go back to
        // new AirlineReservationDashboard().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlightStatusForm().setVisible(true));
    }
}