package com.noobcoder.chickenfront.forms;

import com.noobcoder.chickenfront.util.HttpClientUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class BookFlightForm extends JFrame {
    private JTextField flightNumberField;
    private JTextField passengerNameField;
    private JComboBox<String> genderCombo;
    private JTextField passportNumberField;
    private JLabel confirmationLabel;
    private JButton bookButton;
    private JButton backButton;

    // Theme colors from AdminDashboardForm
    private final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private final Color DARK_BLUE = new Color(23, 32, 42);
    private final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private final Color WHITE = Color.WHITE;

    public BookFlightForm() {
        setTitle("AMS - Book a Flight");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 350);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR); // Updated to match AdminDashboardForm

        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR); // Updated to match AdminDashboardForm
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Updated padding to match AdminDashboardForm

        JLabel titleLabel = new JLabel("Book a Flight", SwingConstants.CENTER);
        titleLabel.setForeground(DARK_BLUE); // Updated to match AdminDashboardForm
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32)); // Updated to match AdminDashboardForm
        mainPanel.add(titleLabel);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        inputPanel.setBackground(BACKGROUND_COLOR); // Updated to match AdminDashboardForm
        JLabel flightNumberLabel = new JLabel("Flight Number:");
        flightNumberLabel.setForeground(DARK_BLUE); // Updated to match AdminDashboardForm
        flightNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match AdminDashboardForm
        flightNumberField = new JTextField(10);
        flightNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Consistent font for input
        JLabel passengerNameLabel = new JLabel("Passenger Name:");
        passengerNameLabel.setForeground(DARK_BLUE); // Updated to match AdminDashboardForm
        passengerNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match AdminDashboardForm
        passengerNameField = new JTextField(10);
        passengerNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Consistent font for input
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setForeground(DARK_BLUE); // Updated to match AdminDashboardForm
        genderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match AdminDashboardForm
        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Consistent font for combo
        JLabel passportNumberLabel = new JLabel("Passport Number:");
        passportNumberLabel.setForeground(DARK_BLUE); // Updated to match AdminDashboardForm
        passportNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match AdminDashboardForm
        passportNumberField = new JTextField(10);
        passportNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Consistent font for input
        bookButton = new CustomStyledButton("Book Flight", 30, PRIMARY_BLUE, WHITE, 2); // Updated to match AdminDashboardForm
        bookButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Updated to match AdminDashboardForm
        bookButton.addActionListener(e -> bookFlight());
        inputPanel.add(flightNumberLabel);
        inputPanel.add(flightNumberField);
        inputPanel.add(passengerNameLabel);
        inputPanel.add(passengerNameField);
        inputPanel.add(genderLabel);
        inputPanel.add(genderCombo);
        inputPanel.add(passportNumberLabel);
        inputPanel.add(passportNumberField);
        inputPanel.add(bookButton);
        mainPanel.add(inputPanel);

        confirmationLabel = new JLabel("", SwingConstants.CENTER);
        confirmationLabel.setForeground(DARK_BLUE); // Updated to match AdminDashboardForm
        confirmationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match AdminDashboardForm
        mainPanel.add(confirmationLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR); // Updated to match AdminDashboardForm
        backButton = new CustomStyledButton("Back to Home", 30, PRIMARY_BLUE, WHITE, 2); // Updated to match AdminDashboardForm
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Updated to match AdminDashboardForm
        backButton.addActionListener(e -> goToHome());
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel);

        add(mainPanel);
    }

    private void bookFlight() {
        String flightNumber = flightNumberField.getText();
        String passengerName = passengerNameField.getText();
        String gender = (String) genderCombo.getSelectedItem();
        String passportNumber = passportNumberField.getText();

        if (flightNumber.isEmpty() || passengerName.isEmpty() || gender == null || passportNumber.isEmpty()) {
            confirmationLabel.setText("Please fill in all fields.");
            return;
        }

        JComboBox<String> paymentMethodCombo = new JComboBox<>(new String[]{"Credit Card", "Debit Card", "PayPal", "Cash"});
        paymentMethodCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Consistent font for combo
        JTextField cardNumberField = new JTextField(10);
        cardNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Consistent font for input
        JTextField expiryField = new JTextField(5);
        expiryField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Consistent font for input
        JTextField cvvField = new JTextField(3);
        cvvField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Consistent font for input
        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberLabel.setForeground(DARK_BLUE); // Updated to match AdminDashboardForm
        cardNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match AdminDashboardForm
        JLabel expiryLabel = new JLabel("Expiry (MM/YY):");
        expiryLabel.setForeground(DARK_BLUE); // Updated to match AdminDashboardForm
        expiryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match AdminDashboardForm
        JLabel cvvLabel = new JLabel("CVV:");
        cvvLabel.setForeground(DARK_BLUE); // Updated to match AdminDashboardForm
        cvvLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match AdminDashboardForm
        JLabel cashMessageLabel = new JLabel("Please pay at the counter.", SwingConstants.CENTER);
        cashMessageLabel.setForeground(DARK_BLUE); // Updated to match AdminDashboardForm
        cashMessageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Updated to match AdminDashboardForm

        JPanel paymentPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        paymentPanel.setBackground(BACKGROUND_COLOR); // Updated to match AdminDashboardForm
        paymentPanel.add(new JLabel("Payment Method:"));
        paymentPanel.add(paymentMethodCombo);
        paymentPanel.add(cardNumberLabel);
        paymentPanel.add(cardNumberField);
        paymentPanel.add(expiryLabel);
        paymentPanel.add(expiryField);
        paymentPanel.add(cvvLabel);
        paymentPanel.add(cvvField);
        paymentPanel.add(new JLabel());
        paymentPanel.add(cashMessageLabel);

        cashMessageLabel.setVisible(false);
        paymentMethodCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedMethod = (String) paymentMethodCombo.getSelectedItem();
                    boolean isCash = "Cash".equals(selectedMethod);
                    cardNumberLabel.setVisible(!isCash);
                    cardNumberField.setVisible(!isCash);
                    expiryLabel.setVisible(!isCash);
                    expiryField.setVisible(!isCash);
                    cvvLabel.setVisible(!isCash);
                    cvvField.setVisible(!isCash);
                    cashMessageLabel.setVisible(isCash);
                    paymentPanel.revalidate();
                    paymentPanel.repaint();
                }
            }
        });

        int paymentResult = JOptionPane.showConfirmDialog(this, paymentPanel, "Payment Details", JOptionPane.OK_CANCEL_OPTION);
        if (paymentResult != JOptionPane.OK_OPTION) {
            confirmationLabel.setText("Payment cancelled.");
            return;
        }

        String paymentMethod = (String) paymentMethodCombo.getSelectedItem();
        if (!"Cash".equals(paymentMethod)) {
            String cardNumber = cardNumberField.getText();
            String expiry = expiryField.getText();
            String cvv = cvvField.getText();
            if (cardNumber.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
                confirmationLabel.setText("Please fill in all payment details.");
                return;
            }
        }

        try {
            // For simplicity, assume 1 ticket per booking
            String query = String.format("?flightNumber=%s&numberOfTickets=1", flightNumber);
            HttpResponse<String> response = HttpClientUtil.sendPostRequest("/bookings/book" + query, "");
            if (response.statusCode() == 200) {
                JSONObject booking = new JSONObject(response.body());
                confirmationLabel.setText("Booking successful for: " + passengerName);
                String bill = String.format(
                        "===== Flight Booking Bill =====\n" +
                                "Booking ID: %d\n" +
                                "Flight Number: %s\n" +
                                "Passenger Name: %s\n" +
                                "Gender: %s\n" +
                                "Passport Number: %s\n" +
                                "Payment Method: %s\n" +
                                "Status: %s\n" +
                                "Date: %s\n" +
                                "==============================",
                        booking.getLong("bookingId"),
                        flightNumber, passengerName, gender, passportNumber,
                        paymentMethod, booking.getString("status"),
                        booking.getString("bookingDate").substring(0, 10)
                );
                JOptionPane.showMessageDialog(this, bill, "Booking Confirmation", JOptionPane.INFORMATION_MESSAGE);
            } else {
                confirmationLabel.setText("Booking failed: " + response.body());
            }
        } catch (Exception e) {
            confirmationLabel.setText("Error: " + e.getMessage());
        }
    }

    private void goToHome() {
        dispose();
        new AirlineReservationDashboard().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookFlightForm().setVisible(true));
    }
}