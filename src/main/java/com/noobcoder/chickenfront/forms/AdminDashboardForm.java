package com.noobcoder.chickenfront.forms;

import com.noobcoder.chickenfront.forms.HomeForm;
import com.noobcoder.chickenfront.forms.ButtonEffects;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;
import java.net.http.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class AdminDashboardForm extends JFrame {
    private JTable flightTable;
    private DefaultTableModel tableModel;
    private JLabel messageLabel;

    public AdminDashboardForm() {
        HttpClientUtil.setAuthCredentials("admin@example.com", "admin123");

        setTitle("AMS - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(15, 20, 22));

        String[] columns = {"Flight Number", "Origin", "Destination", "Date", "Departure", "Arrival"};
        tableModel = new DefaultTableModel(columns, 0);
        flightTable = new JTable(tableModel);
        JScrollPane tablePane = new JScrollPane(flightTable);
        mainPanel.add(tablePane, BorderLayout.CENTER);

        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setForeground(Color.WHITE);
        mainPanel.add(messageLabel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(15, 20, 22));
        String[] actions = {"Add Flight", "Modify Flight", "Delete Flight", "Back to Home"};
        for (String action : actions) {
            JButton btn = new JButton(action);
            ButtonEffects.applySlideOutEffect(btn);
            buttonPanel.add(btn);

            switch (action) {
                case "Add Flight" -> btn.addActionListener(e -> addOrModifyFlight(false));
                case "Modify Flight" -> btn.addActionListener(e -> addOrModifyFlight(true));
                case "Delete Flight" -> btn.addActionListener(e -> deleteFlight());
                case "Back to Home" -> btn.addActionListener(e -> {
                    dispose();
                    HttpClientUtil.clearAuthCredentials();
                    new HomeForm().setVisible(true);
                });
            }
        }

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        add(mainPanel);
        loadFlights();
    }

    private void loadFlights() {
        try {
            HttpResponse<String> res = HttpClientUtil.sendGetRequest("/api/admin/flights");
            if (res.statusCode() == 200) {
                tableModel.setRowCount(0);
                JSONArray flights = new JSONArray(res.body());
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
                for (int i = 0; i < flights.length(); i++) {
                    JSONObject f = flights.getJSONObject(i);
                    LocalDateTime dep = LocalDateTime.parse(f.getString("departureTime"));
                    LocalDateTime arr = LocalDateTime.parse(f.getString("arrivalTime"));
                    tableModel.addRow(new Object[]{
                            f.getString("flightNumber"),
                            f.getString("origin"),
                            f.getString("destination"),
                            dep.toLocalDate().toString(),
                            dep.format(fmt),
                            arr.format(fmt)
                    });
                }
                messageLabel.setText("Flights loaded: " + flights.length());
            } else {
                messageLabel.setText("Failed to load flights: " + res.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading flights.");
        }
    }

    private void addOrModifyFlight(boolean isModify) {
        int row = isModify ? flightTable.getSelectedRow() : -1;
        if (isModify && row == -1) {
            messageLabel.setText("Select a flight to modify.");
            return;
        }

        JTextField flightNumberField = new JTextField(isModify ? tableModel.getValueAt(row, 0).toString() : "", 10);
        JTextField originField = new JTextField(isModify ? tableModel.getValueAt(row, 1).toString() : "", 10);
        JTextField destinationField = new JTextField(isModify ? tableModel.getValueAt(row, 2).toString() : "", 10);
        JTextField dateField = new JTextField(isModify ? tableModel.getValueAt(row, 3).toString() : "", 10);
        JTextField depTimeField = new JTextField(isModify ? tableModel.getValueAt(row, 4).toString() : "", 10);
        JTextField arrTimeField = new JTextField(isModify ? tableModel.getValueAt(row, 5).toString() : "", 10);

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Flight #:")); panel.add(flightNumberField);
        panel.add(new JLabel("Origin:")); panel.add(originField);
        panel.add(new JLabel("Destination:")); panel.add(destinationField);
        panel.add(new JLabel("Date (YYYY-MM-DD):")); panel.add(dateField);
        panel.add(new JLabel("Departure Time (HH:MM):")); panel.add(depTimeField);
        panel.add(new JLabel("Arrival Time (HH:MM):")); panel.add(arrTimeField);

        int result = JOptionPane.showConfirmDialog(this, panel, (isModify ? "Modify" : "Add") + " Flight", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String dtDep = dateField.getText() + "T" + depTimeField.getText() + ":00";
                String dtArr = dateField.getText() + "T" + arrTimeField.getText() + ":00";
                JSONObject json = new JSONObject()
                        .put("flightNumber", flightNumberField.getText())
                        .put("origin", originField.getText())
                        .put("destination", destinationField.getText())
                        .put("departureTime", dtDep)
                        .put("arrivalTime", dtArr);

                if (isModify) {
                    HttpResponse<String> getResp = HttpClientUtil.sendGetRequest("/api/admin/flights/" + flightNumberField.getText());
                    JSONObject existing = new JSONObject(getResp.body());
                    json.put("totalSeats", existing.getInt("totalSeats"));
                    json.put("availableSeats", existing.getInt("availableSeats"));

                    HttpResponse<String> putResp = HttpClientUtil.sendPutRequest("/api/admin/flights/" + flightNumberField.getText(), json.toString());
                    messageLabel.setText(putResp.statusCode() == 200 ? "Modified successfully." : "Modify failed.");
                } else {
                    json.put("totalSeats", 100);
                    json.put("availableSeats", 100);
                    HttpResponse<String> postResp = HttpClientUtil.sendPostRequest("/api/admin/flights", json.toString());
                    messageLabel.setText(postResp.statusCode() == 200 ? "Added successfully." : "Add failed.");
                }
                loadFlights();
            } catch (Exception e) {
                e.printStackTrace();
                messageLabel.setText("Error processing flight.");
            }
        }
    }

    private void deleteFlight() {
        int row = flightTable.getSelectedRow();
        if (row == -1) {
            messageLabel.setText("Select a flight to delete.");
            return;
        }
        String fn = tableModel.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Delete flight " + fn + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                HttpResponse<String> res = HttpClientUtil.sendDeleteRequest("/api/admin/flights/" + fn);
                messageLabel.setText(res.statusCode() == 200 ? "Deleted successfully." : "Delete failed.");
                loadFlights();
            } catch (Exception e) {
                e.printStackTrace();
                messageLabel.setText("Delete error.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboardForm().setVisible(true));
    }

    // ✅ Embedded HttpClientUtil class
    private static class HttpClientUtil {
        private static String baseUrl = "http://localhost:8080";
        private static String authHeader = "";

        public static void setAuthCredentials(String email, String password) {
            String encoded = Base64.getEncoder().encodeToString((email + ":" + password).getBytes());
            authHeader = "Basic " + encoded;
        }

        public static void clearAuthCredentials() {
            authHeader = "";
        }

        public static HttpResponse<String> sendGetRequest(String path) throws Exception {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + path))
                    .GET()
                    .header("Authorization", authHeader)
                    .build();
            return HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
        }

        public static HttpResponse<String> sendPostRequest(String path, String body) throws Exception {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + path))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .header("Content-Type", "application/json")
                    .header("Authorization", authHeader)
                    .build();
            return HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
        }

        public static HttpResponse<String> sendPutRequest(String path, String body) throws Exception {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + path))
                    .PUT(HttpRequest.BodyPublishers.ofString(body))
                    .header("Content-Type", "application/json")
                    .header("Authorization", authHeader)
                    .build();
            return HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
        }

        public static HttpResponse<String> sendDeleteRequest(String path) throws Exception {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + path))
                    .DELETE()
                    .header("Authorization", authHeader)
                    .build();
            return HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
        }
    }
}
