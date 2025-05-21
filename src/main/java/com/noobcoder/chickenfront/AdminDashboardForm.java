package com.noobcoder.chickenfront;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminDashboardForm extends JFrame {
    private JTable flightTable;
    private DefaultTableModel tableModel; // For dynamic table updates
    private JLabel messageLabel;
    private JButton addFlightButton;
    private JButton modifyFlightButton;
    private JButton deleteFlightButton;
    private JButton backButton;

    public AdminDashboardForm() {
        setTitle("AMS - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(15, 20, 22)); // #0F1416

        // Main panel with GridLayout
        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 20, 20));
        mainPanel.setBackground(new Color(15, 20, 22));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Label
        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel);

        // Flight Management Label
        JLabel flightLabel = new JLabel("Flight Management", SwingConstants.CENTER);
        flightLabel.setForeground(Color.WHITE);
        flightLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        mainPanel.add(flightLabel);

        // Flight Table with sample data
        String[] columns = {"Flight Number", "Departure", "Destination", "Date", "Departure Time", "Arrival Time"};
        Object[][] data = {
                {"FL123", "New York", "London", "2025-05-20", "14:00", "20:00"},
                {"FL456", "Tokyo", "Sydney", "2025-05-21", "09:30", "18:30"}
        }; // Sample data with separate times
        tableModel = new DefaultTableModel(data, columns);
        flightTable = new JTable(tableModel);
        flightTable.setBackground(new Color(255, 255, 255, 13)); // rgba(255, 255, 255, 0.05)
        JScrollPane tableScrollPane = new JScrollPane(flightTable);
        mainPanel.add(tableScrollPane);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(15, 20, 22));

        addFlightButton = new JButton("Add Flight");
        ButtonEffects.applySlideOutEffect(addFlightButton);
        addFlightButton.addActionListener(e -> addFlight());

        modifyFlightButton = new JButton("Modify Flight");
        ButtonEffects.applySlideOutEffect(modifyFlightButton);
        modifyFlightButton.addActionListener(e -> modifyFlight());

        deleteFlightButton = new JButton("Delete Flight");
        ButtonEffects.applySlideOutEffect(deleteFlightButton);
        deleteFlightButton.addActionListener(e -> deleteFlight());

        backButton = new JButton("Back to Home");
        ButtonEffects.applySlideOutEffect(backButton);
        backButton.addActionListener(e -> goToHome());

        buttonPanel.add(addFlightButton);
        buttonPanel.add(modifyFlightButton);
        buttonPanel.add(deleteFlightButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel);

        // Message Label
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        mainPanel.add(messageLabel);

        add(mainPanel);
    }

    private void addFlight() {
        // Create a dialog to input flight details
        JTextField flightNumberField = new JTextField(10);
        JTextField departureField = new JTextField(10);
        JTextField destinationField = new JTextField(10);
        JTextField dateField = new JTextField(10);
        JTextField departureTimeField = new JTextField(10);
        JTextField arrivalTimeField = new JTextField(10);

        JPanel dialogPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        dialogPanel.add(new JLabel("Flight Number:"));
        dialogPanel.add(flightNumberField);
        dialogPanel.add(new JLabel("Departure:"));
        dialogPanel.add(departureField);
        dialogPanel.add(new JLabel("Destination:"));
        dialogPanel.add(destinationField);
        dialogPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dialogPanel.add(dateField);
        dialogPanel.add(new JLabel("Departure Time (HH:MM):"));
        dialogPanel.add(departureTimeField);
        dialogPanel.add(new JLabel("Arrival Time (HH:MM):"));
        dialogPanel.add(arrivalTimeField);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Add Flight", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String flightNumber = flightNumberField.getText();
            String departure = departureField.getText();
            String destination = destinationField.getText();
            String date = dateField.getText();
            String departureTime = departureTimeField.getText();
            String arrivalTime = arrivalTimeField.getText();

            if (!flightNumber.isEmpty() && !departure.isEmpty() && !destination.isEmpty() && !date.isEmpty() && !departureTime.isEmpty() && !arrivalTime.isEmpty()) {
                tableModel.addRow(new Object[]{flightNumber, departure, destination, date, departureTime, arrivalTime});
                messageLabel.setText("Flight added successfully!");
            } else {
                messageLabel.setText("Please fill in all fields.");
            }
        }
    }

    private void modifyFlight() {
        int selectedRow = flightTable.getSelectedRow();
        if (selectedRow == -1) {
            messageLabel.setText("Please select a flight to modify.");
            return;
        }

        // Pre-fill dialog with current flight details
        JTextField flightNumberField = new JTextField((String) tableModel.getValueAt(selectedRow, 0), 10);
        JTextField departureField = new JTextField((String) tableModel.getValueAt(selectedRow, 1), 10);
        JTextField destinationField = new JTextField((String) tableModel.getValueAt(selectedRow, 2), 10);
        JTextField dateField = new JTextField((String) tableModel.getValueAt(selectedRow, 3), 10);
        JTextField departureTimeField = new JTextField((String) tableModel.getValueAt(selectedRow, 4), 10);
        JTextField arrivalTimeField = new JTextField((String) tableModel.getValueAt(selectedRow, 5), 10);

        JPanel dialogPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        dialogPanel.add(new JLabel("Flight Number:"));
        dialogPanel.add(flightNumberField);
        dialogPanel.add(new JLabel("Departure:"));
        dialogPanel.add(departureField);
        dialogPanel.add(new JLabel("Destination:"));
        dialogPanel.add(destinationField);
        dialogPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dialogPanel.add(dateField);
        dialogPanel.add(new JLabel("Departure Time (HH:MM):"));
        dialogPanel.add(departureTimeField);
        dialogPanel.add(new JLabel("Arrival Time (HH:MM):"));
        dialogPanel.add(arrivalTimeField);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Modify Flight", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String flightNumber = flightNumberField.getText();
            String departure = departureField.getText();
            String destination = destinationField.getText();
            String date = dateField.getText();
            String departureTime = departureTimeField.getText();
            String arrivalTime = arrivalTimeField.getText();

            if (!flightNumber.isEmpty() && !departure.isEmpty() && !destination.isEmpty() && !date.isEmpty() && !departureTime.isEmpty() && !arrivalTime.isEmpty()) {
                tableModel.setValueAt(flightNumber, selectedRow, 0);
                tableModel.setValueAt(departure, selectedRow, 1);
                tableModel.setValueAt(destination, selectedRow, 2);
                tableModel.setValueAt(date, selectedRow, 3);
                tableModel.setValueAt(departureTime, selectedRow, 4);
                tableModel.setValueAt(arrivalTime, selectedRow, 5);
                messageLabel.setText("Flight modified successfully!");
            } else {
                messageLabel.setText("Please fill in all fields.");
            }
        }
    }

    private void deleteFlight() {
        int selectedRow = flightTable.getSelectedRow();
        if (selectedRow == -1) {
            messageLabel.setText("Please select a flight to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this flight?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            messageLabel.setText("Flight deleted successfully!");
        }
    }

    private void goToHome() {
        dispose();
        new HomeForm().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboardForm().setVisible(true));
    }
}









//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class AdminDashboardForm {
//
//    public static void main(String[] args) {
//        SpringApplication.run(AdminDashboardForm.class, args);
//    }
//
//}
