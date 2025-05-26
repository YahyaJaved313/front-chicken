package com.noobcoder.chickenfront.forms;

import com.noobcoder.chickenfront.util.HttpClientUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminDashboardForm extends JFrame {
    private JTable flightTable;
    private DefaultTableModel tableModel;
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
        getContentPane().setBackground(new Color(15, 20, 22));

        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 20, 20));
        mainPanel.setBackground(new Color(15, 20, 22));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel);

        JLabel flightLabel = new JLabel("Flight Management", SwingConstants.CENTER);
        flightLabel.setForeground(Color.WHITE);
        flightLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        mainPanel.add(flightLabel);

        String[] columns = {"Flight Number", "Departure", "Destination", "Date", "Dep Time", "Arr Time"};
        tableModel = new DefaultTableModel(columns, 0);
        flightTable = new JTable(tableModel);
        flightTable.setBackground(new Color(255, 255, 255, 13));
        JScrollPane tableScrollPane = new JScrollPane(flightTable);
        mainPanel.add(tableScrollPane);

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

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        mainPanel.add(messageLabel);

        add(mainPanel);
        loadFlights();
    }

    private void loadFlights() {
        try {
            HttpResponse<String> response = HttpClientUtil.sendGetRequest("/admin/flights");
            if (response.statusCode() == 200) {
                JSONArray flights = new JSONArray(response.body());
                tableModel.setRowCount(0); // Clear existing rows
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                for (int i = 0; i < flights.length(); i++) {
                    JSONObject flight = flights.getJSONObject(i);
                    tableModel.addRow(new Object[]{
                            flight.getString("flightNumber"),
                            flight.getString("origin"),
                            flight.getString("destination"),
                            LocalDateTime.parse(flight.getString("departureTime")).toLocalDate(),
                            LocalDateTime.parse(flight.getString("departureTime")).format(timeFormatter),
                            LocalDateTime.parse(flight.getString("arrivalTime")).format(timeFormatter)
                    });
                }
            } else {
                messageLabel.setText("Failed to load flights: " + response.body());
            }
        } catch (Exception e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    private void addFlight() {
        JTextField flightNumberField = new JTextField(10);
        JTextField departureField = new JTextField(10);
        JTextField destinationField = new JTextField(10);
        JTextField dateField = new JTextField(10);
        JTextField departureTimeField = new JTextField(10);
        JTextField arrivalTimeField = new JTextField(10);
        JTextField totalSeatsField = new JTextField(10);
        JTextField availableSeatsField = new JTextField(10);

        JPanel dialogPanel = new JPanel(new GridLayout(8, 2, 10, 10));
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
        dialogPanel.add(new JLabel("Total Seats:"));
        dialogPanel.add(totalSeatsField);
        dialogPanel.add(new JLabel("Available Seats:"));
        dialogPanel.add(availableSeatsField);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Add Flight", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String flightNumber = flightNumberField.getText();
            String departure = departureField.getText();
            String destination = destinationField.getText();
            String date = dateField.getText();
            String departureTime = departureTimeField.getText();
            String arrivalTime = arrivalTimeField.getText();
            String totalSeats = totalSeatsField.getText();
            String availableSeats = availableSeatsField.getText();

            if (flightNumber.isEmpty() || departure.isEmpty() || destination.isEmpty() || date.isEmpty() ||
                    departureTime.isEmpty() || arrivalTime.isEmpty() || totalSeats.isEmpty() || availableSeats.isEmpty()) {
                messageLabel.setText("Please fill in all fields.");
                return;
            }

            try {
                JSONObject flight = new JSONObject();
                flight.put("flightNumber", flightNumber);
                flight.put("origin", departure);
                flight.put("destination", destination);
                flight.put("departureTime", date + "T" + departureTime + ":00");
                flight.put("arrivalTime", date + "T" + arrivalTime + ":00");
                flight.put("totalSeats", Integer.parseInt(totalSeats));
                flight.put("availableSeats", Integer.parseInt(availableSeats));

                HttpResponse<String> response = HttpClientUtil.sendPostRequest("/admin/flights", flight.toString());
                if (response.statusCode() == 200) {
                    messageLabel.setText("Flight added successfully!");
                    loadFlights();
                } else {
                    messageLabel.setText("Failed to add flight: " + response.body());
                }
            } catch (Exception e) {
                messageLabel.setText("Error: " + e.getMessage());
            }
        }
    }

    private void modifyFlight() {
        int selectedRow = flightTable.getSelectedRow();
        if (selectedRow == -1) {
            messageLabel.setText("Please select a flight to modify.");
            return;
        }

        JTextField flightNumberField = new JTextField((String) tableModel.getValueAt(selectedRow, 0), 10);
        JTextField departureField = new JTextField((String) tableModel.getValueAt(selectedRow, 1), 10);
        JTextField destinationField = new JTextField((String) tableModel.getValueAt(selectedRow, 2), 10);
        JTextField dateField = new JTextField((String) tableModel.getValueAt(selectedRow, 3), 10);
        JTextField departureTimeField = new JTextField((String) tableModel.getValueAt(selectedRow, 4), 10);
        JTextField arrivalTimeField = new JTextField((String) tableModel.getValueAt(selectedRow, 5), 10);
        JTextField totalSeatsField = new JTextField(10);
        JTextField availableSeatsField = new JTextField(10);

        JPanel dialogPanel = new JPanel(new GridLayout(8, 2, 10, 10));
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
        dialogPanel.add(new JLabel("Total Seats:"));
        dialogPanel.add(totalSeatsField);
        dialogPanel.add(new JLabel("Available Seats:"));
        dialogPanel.add(availableSeatsField);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Modify Flight", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String flightNumber = flightNumberField.getText();
            String departure = departureField.getText();
            String destination = destinationField.getText();
            String date = dateField.getText();
            String departureTime = departureTimeField.getText();
            String arrivalTime = arrivalTimeField.getText();
            String totalSeats = totalSeatsField.getText();
            String availableSeats = availableSeatsField.getText();

            if (flightNumber.isEmpty() || departure.isEmpty() || destination.isEmpty() || date.isEmpty() ||
                    departureTime.isEmpty() || arrivalTime.isEmpty() || totalSeats.isEmpty() || availableSeats.isEmpty()) {
                messageLabel.setText("Please fill in all fields.");
                return;
            }

            try {
                JSONObject flight = new JSONObject();
                flight.put("flightNumber", flightNumber);
                flight.put("origin", departure);
                flight.put("destination", destination);
                flight.put("departureTime", date + "T" + departureTime + ":00");
                flight.put("arrivalTime", date + "T" + arrivalTime + ":00");
                flight.put("totalSeats", Integer.parseInt(totalSeats));
                flight.put("availableSeats", Integer.parseInt(availableSeats));

                HttpResponse<String> response = HttpClientUtil.sendPutRequest("/admin/flights/" + flightNumber, flight.toString());
                if (response.statusCode() == 200) {
                    messageLabel.setText("Flight modified successfully!");
                    loadFlights();
                } else {
                    messageLabel.setText("Failed to modify flight: " + response.body());
                }
            } catch (Exception e) {
                messageLabel.setText("Error: " + e.getMessage());
            }
        }
    }

    private void deleteFlight() {
        int selectedRow = flightTable.getSelectedRow();
        if (selectedRow == -1) {
            messageLabel.setText("Please select a flight to delete.");
            return;
        }

        String flightNumber = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete flight " + flightNumber + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                HttpResponse<String> response = HttpClientUtil.sendDeleteRequest("/admin/flights/" + flightNumber);
                if (response.statusCode() == 200) {
                    messageLabel.setText("Flight deleted successfully!");
                    loadFlights();
                } else {
                    messageLabel.setText("Failed to delete flight: " + response.body());
                }
            } catch (Exception e) {
                messageLabel.setText("Error: " + e.getMessage());
            }
        }
    }

    private void goToHome() {
        dispose();
        HttpClientUtil.clearAuthCredentials();
        new AirlineReservationDashboard().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboardForm().setVisible(true));
    }
}