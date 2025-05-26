package com.noobcoder.chickenfront.forms;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import com.noobcoder.chickenfront.util.HttpClientUtil;

public class AirlineReservationDashboard extends JFrame {
    private JPanel sidebarPanel;
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JPanel headerPanel;
    private boolean sidebarVisible = true;
    private javax.swing.Timer slideTimer;
    private int sidebarTargetWidth = 250;
    private int sidebarCollapsedWidth = 50;
    private int sidebarCurrentWidth = 250;
    private JTable flightTable;
    private JLabel hmLabel;

    // Custom Colors
    private final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private final Color SECONDARY_BLUE = new Color(52, 152, 219);
    private final Color LIGHT_BLUE = new Color(174, 214, 241);
    private final Color DARK_BLUE = new Color(23, 32, 42);
    private final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private final Color WHITE = Color.WHITE;
    private final Color HOVER_COLOR = new Color(233, 247, 254);

    public AirlineReservationDashboard() {
        initializeFrame();
        createComponents();
        setupLayout();
        populateFlightData();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Airline Ticket Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000, 600));
        getContentPane().setBackground(BACKGROUND_COLOR);
    }

    private void createComponents() {
        setLayout(new BorderLayout());
        createSidebar();
        createMainContent();
    }

    private void createSidebar() {
        sidebarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, PRIMARY_BLUE,
                        0, getHeight(), new Color(52, 73, 94)
                );
                g2d.setPaint(gradient);

                RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), 20, 20
                );
                g2d.fill(roundedRect);
                g2d.dispose();
            }
        };
        sidebarPanel.setPreferredSize(new Dimension(sidebarTargetWidth, 0));
        sidebarPanel.setLayout(new BorderLayout());
        sidebarPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        hmLabel = new JLabel("H&M");
        hmLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        hmLabel.setForeground(WHITE);
        hmLabel.setHorizontalAlignment(SwingConstants.LEFT);
        hmLabel.setPreferredSize(new Dimension(60, 40));

        JButton hamburgerBtn = createHamburgerButton();
        hamburgerBtn.setPreferredSize(new Dimension(40, 40));
        hamburgerBtn.setMaximumSize(new Dimension(40, 40));

        headerPanel.add(hmLabel, BorderLayout.WEST);
        headerPanel.add(hamburgerBtn, BorderLayout.EAST);

        JPanel navPanel = createNavigationPanel();
        JPanel bottomPanel = createBottomPanel();

        sidebarPanel.add(headerPanel, BorderLayout.NORTH);
        sidebarPanel.add(navPanel, BorderLayout.CENTER);
        sidebarPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void updateHamburgerMargin() {
        int leftPad = sidebarVisible ? 10 : 14;
        headerPanel.setBorder(new EmptyBorder(10, leftPad, 10, 10));
        headerPanel.revalidate();
        headerPanel.repaint();
    }

    private JButton createHamburgerButton() {
        RoundedButton btn = new RoundedButton("", 30, PRIMARY_BLUE, WHITE, 2);
        btn.setPreferredSize(new Dimension(40, 40));
        btn.setMaximumSize(new Dimension(40, 40));
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setForeground(WHITE);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/icons/hamburger.png"));
            btn.setIcon(new ImageIcon(icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            btn.setText("☰");
        }

        btn.addActionListener(e -> toggleSidebar());
        return btn;
    }

    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(30, 0, 0, 0));

        String[] buttonTexts = {
                "Book Flight",
                "Flight Status",
                "Search Flights",
                "Contact Us",
                "Admin Login"
        };

        for (String text : buttonTexts) {
            JButton btn = createNavButton(text);
            panel.add(btn);
            panel.add(Box.createVerticalStrut(25));
        }
        panel.remove(panel.getComponentCount() - 1);
        return panel;
    }

    private JButton createNavButton(String text) {
        RoundedButton btn = new RoundedButton(text, 30, PRIMARY_BLUE, WHITE, 2);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 45));

        btn.addActionListener(e -> {
            dispose();
            switch (text) {
                case "Book Flight":
                    new BookFlightForm().setVisible(true);
                    break;
                case "Flight Status":
                    new FlightStatusForm().setVisible(true);
                    break;
                case "Search Flights":
                    new FlightSearchForm().setVisible(true);
                    break;
                case "Contact Us":
                    new ContactUsForm().setVisible(true);
                    break;
                case "Admin Login":
                    new AdminLoginForm().setVisible(true);
                    break;
            }
        });

        return btn;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);

        RoundedButton logoutBtn = new RoundedButton("Logout", 30, PRIMARY_BLUE, WHITE, 2);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginForm().setVisible(true);
        });

        panel.add(logoutBtn);
        return panel;
    }

    private void createMainContent() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel headerLabel = new JLabel("Airline Ticket Reservation System");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headerLabel.setForeground(DARK_BLUE);
        headerLabel.setBorder(new EmptyBorder(0, 0, 30, 0));

        JPanel tablePanel = createTablePanel();

        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel tableTitle = new JLabel("Flight Schedule");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tableTitle.setForeground(DARK_BLUE);
        tableTitle.setBorder(new EmptyBorder(0, 0, 20, 0));

        String[] columns = {"Flight Number", "Departure Time", "Arrival Time", "Origin", "Destination"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        flightTable = new JTable(model);
        styleTable(flightTable);

        JScrollPane scrollPane = new JScrollPane(flightTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(WHITE);

        panel.add(tableTitle, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(45);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(233, 247, 254));
        table.setSelectionForeground(DARK_BLUE);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_BLUE);
        header.setForeground(WHITE);
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(BorderFactory.createEmptyBorder());

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(WHITE);
                    } else {
                        c.setBackground(new Color(245, 245, 245));
                    }
                }

                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private void populateFlightData() {
        if (flightTable != null) {
            DefaultTableModel model = (DefaultTableModel) flightTable.getModel();
            model.setRowCount(0); // Clear existing rows

            try {
                HttpResponse<String> response = HttpClientUtil.sendGetRequest("/flights");
                System.out.println("API Response: " + response.body()); // Debug log
                if (response.statusCode() == 200) {
                    JSONArray flights = new JSONArray(response.body());
                    for (int i = 0; i < flights.length(); i++) {
                        JSONObject flight = flights.getJSONObject(i);
                        String flightNumber = flight.optString("flight_number", flight.optString("flightNumber", "N/A"));
                        String departureTime = flight.optString("departure_time", flight.optString("departureTime", "N/A"));
                        String arrivalTime = flight.optString("arrival_time", flight.optString("arrivalTime", "N/A"));
                        String origin = flight.optString("origin", "N/A");
                        String destination = flight.optString("destination", "N/A");

                        model.addRow(new Object[]{flightNumber, departureTime, arrivalTime, origin, destination});
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to fetch flight data: HTTP " + response.statusCode(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error fetching flight data: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void toggleSidebar() {
        if (slideTimer != null && slideTimer.isRunning()) {
            slideTimer.stop();
        }

        int startWidth = sidebarCurrentWidth;
        int endWidth = sidebarVisible ? sidebarCollapsedWidth : sidebarTargetWidth;
        sidebarVisible = !sidebarVisible;

        updateHamburgerMargin();

        slideTimer = new javax.swing.Timer(10, new ActionListener() {
            int step = 0;
            final int totalSteps = 20;

            @Override
            public void actionPerformed(ActionEvent e) {
                step++;
                double progress = (double) step / totalSteps;
                progress = 1 - Math.pow(1 - progress, 3);

                sidebarCurrentWidth = (int) (startWidth + (endWidth - startWidth) * progress);
                sidebarPanel.setPreferredSize(new Dimension(sidebarCurrentWidth, 0));

                for (Component comp : sidebarPanel.getComponents()) {
                    if (comp != null && comp != sidebarPanel.getComponent(0)) {
                        comp.setVisible(sidebarCurrentWidth > sidebarCollapsedWidth + 10);
                    }
                }
                if (hmLabel != null) {
                    hmLabel.setVisible(sidebarCurrentWidth > sidebarCollapsedWidth + 10);
                }

                revalidate();
                repaint();

                if (step >= totalSteps) {
                    slideTimer.stop();
                }
            }
        });

        slideTimer.start();
    }

    private void setupLayout() {
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel
        }

        SwingUtilities.invokeLater(() -> {
            new AirlineReservationDashboard();
        });
    }
}

class RoundedButton extends JButton {
    private final int radius;
    private final Color fillColor;
    private final Color borderColor;
    private final int borderThickness;

    public RoundedButton(String text, int radius, Color fillColor, Color borderColor, int borderThickness) {
        super(text);
        this.radius = radius;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
        this.borderThickness = borderThickness;
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(fillColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(borderThickness));
        g2.drawRoundRect(borderThickness / 2, borderThickness / 2,
                getWidth() - borderThickness, getHeight() - borderThickness, radius, radius);
        g2.dispose();
    }
}