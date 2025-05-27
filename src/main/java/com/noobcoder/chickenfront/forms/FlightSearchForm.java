package com.noobcoder.chickenfront.forms;

import com.noobcoder.chickenfront.util.HttpClientUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FlightSearchForm extends JFrame {
    private JTextField departureField;
    private JTextField destinationField;
    private JSpinner datePicker;
    private JTable flightTable;
    private DefaultTableModel tableModel;
    private JButton searchButton;
    private JButton showAllButton;
    private JButton backButton;

    // Theme colors from AdminDashboardForm
    private final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private final Color DARK_BLUE = new Color(23, 32, 42);
    private final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private final Color WHITE = Color.WHITE;

    public FlightSearchForm() {
        // Set authentication credentials (using provided admin credentials)
        try {
            String username = "admin@example.com"; // Provided username
            String password = "admin123"; // Provided password
            HttpClientUtil.setAuthCredentials(username, password);
            System.err.println("Authentication set successfully in FlightSearchForm constructor with user: " + username);
        } catch (Exception e) {
            System.err.println("Failed to set authentication credentials: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Authentication setup error: " + e.getMessage());
        }

        setTitle("AMS - Search Flights");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR); // Updated to match AdminDashboardForm

        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR); // Updated to match AdminDashboardForm
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Updated padding to match AdminDashboardForm

        JLabel titleLabel = new JLabel("Search Flights", SwingConstants.CENTER);
        titleLabel.setForeground(DARK_BLUE); // Updated to match AdminDashboardForm
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32)); // Updated to match AdminDashboardForm
        mainPanel.add(titleLabel);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        inputPanel.setBackground(BACKGROUND_COLOR); // Updated to match AdminDashboardForm
        JLabel departureLabel = new JLabel("Departure:");
        departureLabel.setForeground(DARK_BLUE); // Updated to match AdminDashboardForm
        departureLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match AdminDashboardForm
        departureField = new JTextField(10);
        departureField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Consistent font for input
        JLabel destinationLabel = new JLabel("Destination:");
        destinationLabel.setForeground(DARK_BLUE); // Updated to match AdminDashboardForm
        destinationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match AdminDashboardForm
        destinationField = new JTextField(10);
        destinationField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Consistent font for input
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setForeground(DARK_BLUE); // Updated to match AdminDashboardForm
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match AdminDashboardForm
        // Use SpinnerDateModel with a Date object
        Date currentDate = Date.from(LocalDate.of(2025, 5, 26).atStartOfDay(ZoneId.systemDefault()).toInstant());
        datePicker = new JSpinner(new SpinnerDateModel(currentDate, null, null, java.util.Calendar.DAY_OF_MONTH));
        datePicker.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Consistent font for spinner
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(datePicker, "yyyy-MM-dd");
        datePicker.setEditor(dateEditor);
        // Log initial date picker value
        System.err.println("Initial date picker value: " + ((JSpinner.DateEditor) datePicker.getEditor()).getFormat().format(datePicker.getValue()));
        searchButton = new CustomStyledButton("Search", 30, PRIMARY_BLUE, WHITE, 2); // Updated to match AdminDashboardForm
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Updated to match AdminDashboardForm
        searchButton.addActionListener(e -> {
            System.err.println("Search button clicked");
            try {
                searchFlights();
            } catch (Exception ex) {
                System.err.println("Error in Search Flights ActionListener: " + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Unexpected error in Search Flights: " + ex.getMessage());
            }
        });
        showAllButton = new CustomStyledButton("Show All Flights", 30, PRIMARY_BLUE, WHITE, 2); // Updated to match AdminDashboardForm
        showAllButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Updated to match AdminDashboardForm
        showAllButton.addActionListener(e -> {
            System.err.println("Show All button clicked");
            try {
                showAllFlights();
            } catch (Exception ex) {
                System.err.println("Error in Show All Flights ActionListener: " + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Unexpected error in Show All Flights: " + ex.getMessage());
            }
        });
        inputPanel.add(departureLabel);
        inputPanel.add(departureField);
        inputPanel.add(destinationLabel);
        inputPanel.add(destinationField);
        inputPanel.add(dateLabel);
        inputPanel.add(datePicker);
        inputPanel.add(searchButton);
        inputPanel.add(showAllButton);
        mainPanel.add(inputPanel);

        String[] columns = {"Flight Number", "Departure", "Destination", "Date", "Dep Time", "Arr Time"};
        tableModel = new DefaultTableModel(columns, 0);
        flightTable = new JTable(tableModel);
        flightTable.setBackground(BACKGROUND_COLOR); // Updated to match AdminDashboardForm
        flightTable.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Consistent font for table
        JScrollPane tableScrollPane = new JScrollPane(flightTable);
        mainPanel.add(tableScrollPane);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR); // Updated to match AdminDashboardForm
        backButton = new CustomStyledButton("Back to Home", 30, PRIMARY_BLUE, WHITE, 2); // Updated to match AdminDashboardForm
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Updated to match AdminDashboardForm
        backButton.addActionListener(e -> goToHome());
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel);

        add(mainPanel);
        loadFlights();
    }

    private void loadFlights() {
        try {
            System.err.println("Loading flights initially...");
            showAllFlights();
        } catch (Exception e) {
            System.err.println("Error in loadFlights: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading flights: " + e.getMessage());
        }
    }

    private void searchFlights() {
        String departure = departureField.getText().trim();
        String destination = destinationField.getText().trim();
        // Get the raw date from the JSpinner
        Date dateValue = (Date) datePicker.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = formatter.format(dateValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        System.err.println("Raw date picker value: " + dateValue);
        System.err.println("Formatted date for search: " + date);

        tableModel.setRowCount(0); // Clear existing rows
        System.err.println("Searching flights - Departure: " + departure + ", Destination: " + destination + ", Date: " + date);

        try {
            // Use the date as-is in yyyy-MM-dd format
            String formattedDate = date; // Already in yyyy-MM-dd format
            System.err.println("Formatted date for backend: " + formattedDate);

            // Validate and construct URL
            String baseUrl = "http://localhost:8080"; // Adjust to your application server port
            String queryParams = "?origin=" + (departure.isEmpty() ? "" : URLEncoder.encode(departure, StandardCharsets.UTF_8)) +
                    "&destination=" + (destination.isEmpty() ? "" : URLEncoder.encode(destination, StandardCharsets.UTF_8)) +
                    "&date=" + (formattedDate.isEmpty() ? "" : URLEncoder.encode(formattedDate, StandardCharsets.UTF_8));
            String fullUrl = "/flights/search" + queryParams;
            URI uri = new URI(baseUrl + fullUrl);
            System.err.println("Constructed URI: " + uri.toString());

            HttpResponse<String> response = HttpClientUtil.sendGetRequest(fullUrl); // Pass relative endpoint
            System.err.println("Response Status: " + response.statusCode() + ", Headers: " + response.headers() + ", Body: " + response.body());

            if (response.statusCode() == 200) {
                System.err.println("Response Body: " + response.body());
                JSONArray flights = new JSONArray(response.body());
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                for (int i = 0; i < flights.length(); i++) {
                    JSONObject flight = flights.getJSONObject(i);
                    String depTimeStr = flight.getString("departureTime");
                    String arrTimeStr = flight.getString("arrivalTime");
                    System.err.println("Parsing departureTime: " + depTimeStr + ", arrivalTime: " + arrTimeStr);
                    LocalDateTime depTime = parseTimeSafely(depTimeStr);
                    LocalDateTime arrTime = parseTimeSafely(arrTimeStr);
                    tableModel.addRow(new Object[]{
                            flight.getString("flightNumber"),
                            flight.getString("origin"),
                            flight.getString("destination"),
                            depTime.toLocalDate(),
                            depTime.format(timeFormatter),
                            arrTime.format(timeFormatter)
                    });
                }
                JOptionPane.showMessageDialog(this, "Found " + tableModel.getRowCount() + " flight(s).");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to search flights: Status " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException e) {
            System.err.println("IO Exception in searchFlights: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Network error: " + e.getMessage());
        } catch (JSONException e) {
            System.err.println("JSON Exception in searchFlights: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid response format: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.err.println("DateTime Parse Exception in searchFlights: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid date/time format: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Illegal Argument Exception in searchFlights: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Illegal value error: " + e.getMessage());
        } catch (URISyntaxException e) {
            System.err.println("URI Syntax Exception in searchFlights: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid URL: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error in searchFlights: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void showAllFlights() {
        tableModel.setRowCount(0); // Clear existing rows
        System.err.println("Loading all flights...");

        try {
            String endpoint = "/admin/flights"; // Working endpoint
            URI fullUri = new URI("http://localhost:8080" + endpoint);
            System.err.println("Requesting URI: " + fullUri.toString());

            HttpResponse<String> response = HttpClientUtil.sendGetRequest(endpoint);
            System.err.println("Response Status: " + response.statusCode() + ", Headers: " + response.headers() + ", Body: " + response.body());

            if (response.statusCode() == 200) {
                System.err.println("Response Body: " + response.body());
                JSONArray flights = new JSONArray(response.body());
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                for (int i = 0; i < flights.length(); i++) {
                    JSONObject flight = flights.getJSONObject(i);
                    String depTimeStr = flight.getString("departureTime");
                    String arrTimeStr = flight.getString("arrivalTime");
                    System.err.println("Parsing departureTime: " + depTimeStr + ", arrivalTime: " + arrTimeStr);
                    LocalDateTime depTime = parseTimeSafely(depTimeStr);
                    LocalDateTime arrTime = parseTimeSafely(arrTimeStr);
                    tableModel.addRow(new Object[]{
                            flight.getString("flightNumber"),
                            flight.getString("origin"),
                            flight.getString("destination"),
                            depTime.toLocalDate(),
                            depTime.format(timeFormatter),
                            arrTime.format(timeFormatter)
                    });
                }
                JOptionPane.showMessageDialog(this, "Displaying all available flights (" + tableModel.getRowCount() + ").");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to load flights: Status " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException e) {
            System.err.println("IO Exception in showAllFlights: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Network error: " + e.getMessage());
        } catch (JSONException e) {
            System.err.println("JSON Exception in showAllFlights: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid response format: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.err.println("DateTime Parse Exception in showAllFlights: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid date/time format: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Illegal Argument Exception in showAllFlights: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Illegal value error: " + e.getMessage());
        } catch (URISyntaxException e) {
            System.err.println("URI Syntax Exception in showAllFlights: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid URL: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error in showAllFlights: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private LocalDateTime parseTimeSafely(String timeStr) {
        try {
            return LocalDateTime.parse(timeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            System.err.println("Failed to parse time: " + timeStr + " - Using fallback: " + LocalDateTime.now());
            return LocalDateTime.now(); // Fallback to current time if parsing fails
        }
    }

    private void goToHome() {
        dispose();
        HttpClientUtil.clearAuthCredentials();
        new AirlineReservationDashboard().setVisible(true);
    }

    public static void main(String[] args) {
        // Set an uncaught exception handler for Swing EDT
        SwingUtilities.invokeLater(() -> {
            Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
                System.err.println("Uncaught exception in EDT: " + throwable.getMessage());
                throwable.printStackTrace();
                JOptionPane.showMessageDialog(null, "Critical error: " + throwable.getMessage());
            });
            new FlightSearchForm().setVisible(true);
        });
    }
}