package com.noobcoder.chickenfront;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import javax.swing.SpinnerDateModel;

public class FlightSearchForm extends JFrame {
    private JTextField departureField;
    private JTextField destinationField;
    private JSpinner datePicker;
    private JTable flightTable;
    private JButton searchButton;
    private JButton backButton;

    public FlightSearchForm() {
        setTitle("AMS - Search Flights");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(15, 20, 22)); // #0F1416

        // Main panel with GridLayout
        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 20, 20));
        mainPanel.setBackground(new Color(15, 20, 22));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Label
        JLabel titleLabel = new JLabel("Search Flights", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel);

        // Input Panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        inputPanel.setBackground(new Color(15, 20, 22));
        JLabel departureLabel = new JLabel("Departure:");
        departureLabel.setForeground(Color.WHITE);
        departureLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        departureField = new JTextField(10);
        JLabel destinationLabel = new JLabel("Destination:");
        destinationLabel.setForeground(Color.WHITE);
        destinationLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        destinationField = new JTextField(10);
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        datePicker = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(datePicker, "yyyy-MM-dd");
        datePicker.setEditor(dateEditor);
        searchButton = new JButton("Search");
        ButtonEffects.applySlideOutEffect(searchButton);
        searchButton.addActionListener(e -> searchFlights());
        inputPanel.add(departureLabel);
        inputPanel.add(departureField);
        inputPanel.add(destinationLabel);
        inputPanel.add(destinationField);
        inputPanel.add(dateLabel);
        inputPanel.add(datePicker);
        inputPanel.add(searchButton);
        mainPanel.add(inputPanel);

        // Flight Table
        String[] columns = {"Flight Number", "Departure", "Destination", "Date", "Time"};
        Object[][] data = {}; // Placeholder data
        flightTable = new JTable(data, columns);
        flightTable.setBackground(new Color(255, 255, 255, 13)); // rgba(255, 255, 255, 0.05)
        JScrollPane tableScrollPane = new JScrollPane(flightTable);
        mainPanel.add(tableScrollPane);

        // Back Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(15, 20, 22));
        backButton = new JButton("Back to Home");
        ButtonEffects.applySlideOutEffect(backButton);
        backButton.addActionListener(e -> goToHome());
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel);

        add(mainPanel);
    }

    private void searchFlights() {
        String departure = departureField.getText();
        String destination = destinationField.getText();
        Date date = (Date) datePicker.getValue();
        JOptionPane.showMessageDialog(this, "Searching flights from " + departure + " to " + destination);
    }

    private void goToHome() {
        dispose();
        new HomeForm().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlightSearchForm().setVisible(true));
    }
}

