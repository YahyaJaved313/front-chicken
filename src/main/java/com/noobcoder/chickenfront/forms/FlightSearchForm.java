package com.noobcoder.chickenfront.forms;

import com.noobcoder.chickenfront.util.HttpClientUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FlightSearchForm extends JFrame {
    private JTextField departureField;
    private JTextField destinationField;
    private JSpinner datePicker;
    private JTable flightTable;
    private DefaultTableModel tableModel;
    private JButton searchButton;
    private JButton backButton;

    public FlightSearchForm() {
        setTitle("AMS - Search Flights");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(15, 20, 22));

        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 20, 20));
        mainPanel.setBackground(new Color(15, 20, 22));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Search Flights", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel);

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

        String[] columns = {"Flight Number", "Departure", "Destination", "Date", "Dep Time", "Arr Time"};
        tableModel = new DefaultTableModel(columns, 0);
        flightTable = new JTable(tableModel);
        flightTable.setBackground(new Color(255, 255, 255, 13));
        JScrollPane tableScrollPane = new JScrollPane(flightTable);
        mainPanel.add(tableScrollPane);

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            HttpResponse<String> response = HttpClientUtil.sendGetRequest("/flights");
            if (response.statusCode() == 200) {
                JSONArray flights = new JSONArray(response.body());
                tableModel.setRowCount(0);
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                for (int i = 0; i < flights.length(); i++) {
                    JSONObject flight = flights.getJSONObject(i);
                    String flightDeparture = flight.getString("origin");
                    String flightDestination = flight.getString("destination");
                    String flightDate = LocalDateTime.parse(flight.getString("departureTime")).toLocalDate().toString();
                    if ((departure.isEmpty() || flightDeparture.equalsIgnoreCase(departure)) &&
                            (destination.isEmpty() || flightDestination.equalsIgnoreCase(destination)) &&
                            (date == null || flightDate.equals(formatter.format(date.toInstant())))) {
                        tableModel.addRow(new Object[]{
                                flight.getString("flightNumber"),
                                flightDeparture,
                                flightDestination,
                                flightDate,
                                LocalDateTime.parse(flight.getString("departureTime")).format(timeFormatter),
                                LocalDateTime.parse(flight.getString("arrivalTime")).format(timeFormatter)
                        });
                    }
                }
                if (tableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "No flights found for the given criteria.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to fetch flights: " + response.body());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void goToHome() {
        dispose();
        new AirlineReservationDashboard().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlightSearchForm().setVisible(true));
    }
}