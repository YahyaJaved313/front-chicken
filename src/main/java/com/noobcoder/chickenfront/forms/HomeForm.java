package com.noobcoder.chickenfront.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class HomeForm extends JFrame {
    private JPanel sidebar;
    private JLabel headingLabel;
    private JLabel welcomeLabel;
    private JLabel descriptionLabel;
    private JButton[] sidebarButtons;
    private boolean isSidebarVisible = false;
    private int sidebarWidth = 200;
    private int animationStep = 10;
    private Timer animationTimer;
    private Timer[] buttonTimers; // For cascading button animations

    public HomeForm() {
        setTitle("AMS - Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(15, 20, 22)); // #0F1416

        setLayout(new BorderLayout());

        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(15, 20, 22));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Hamburger Toggle Button with Generated Icon
        BufferedImage hamburgerImage = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = hamburgerImage.createGraphics();
        g2d.setColor(new Color(0, 0, 0, 0)); // Transparent background
        g2d.fillRect(0, 0, 30, 30);
        g2d.setColor(new Color(139, 0, 0)); // #8B0000 for lines
        int barHeight = 2;
        int barSpacing = 6;
        g2d.fillRect(5, 4, 20, barHeight); // Top line
        g2d.fillRect(5, 10, 20, barHeight); // Middle line
        g2d.fillRect(5, 16, 20, barHeight); // Bottom line
        g2d.dispose();
        ImageIcon hamburgerIcon = new ImageIcon(hamburgerImage);
        JButton menuButton = new JButton(hamburgerIcon);
        menuButton.setOpaque(false);
        menuButton.setContentAreaFilled(false);
        menuButton.setBorderPainted(false);
        menuButton.setFocusable(false);
        menuButton.addActionListener(e -> {
            System.out.println("Hamburger button clicked! Toggling sidebar...");
            toggleSidebar();
        });
        topPanel.add(menuButton, BorderLayout.WEST);

        // Heading Label
        headingLabel = new JLabel("Airline Reservation System", SwingConstants.CENTER);
        headingLabel.setForeground(Color.WHITE);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 28));
        topPanel.add(headingLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Sidebar
        sidebar = new JPanel();
        sidebar.setBackground(new Color(26, 37, 38)); // #1A2526
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebar.setPreferredSize(new Dimension(0, 0)); // Start with width 0
        String[] buttons = {
                "Search Flights", "Book Flight", "Flight Status", "User Profile",
                "Login", "Register", "Contact Us"
        }; // Removed "Admin Login" and "Admin Dashboard"
        sidebarButtons = new JButton[buttons.length];
        buttonTimers = new Timer[buttons.length];
        for (int i = 0; i < buttons.length; i++) {
            JButton button = new JButton(buttons[i]);
            ButtonEffects.applySlideOutEffect(button);
            button.setMaximumSize(new Dimension(180, 40));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            int finalI = i;
            button.addActionListener(e -> navigate(buttons[finalI]));
            sidebar.add(button);
            if (i < buttons.length - 1) sidebar.add(Box.createVerticalStrut(15)); // Match spacing="15"
            sidebarButtons[i] = button;
        }
        add(sidebar, BorderLayout.WEST);

        // Center Panel
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        centerPanel.setBackground(new Color(15, 20, 22));
        welcomeLabel = new JLabel("Welcome to the Airline Ticketing System!", SwingConstants.CENTER);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        descriptionLabel = new JLabel("Book your flights with ease and explore our services!", SwingConstants.CENTER);
        descriptionLabel.setForeground(new Color(224, 224, 224)); // #E0E0E0
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        centerPanel.add(welcomeLabel);
        centerPanel.add(descriptionLabel);
        add(centerPanel, BorderLayout.CENTER);

        // Animation Timer for sidebar (0.5s duration)
        animationTimer = new Timer(20, null); // 20ms interval
    }

    private void toggleSidebar() {
        if (animationTimer.isRunning()) {
            animationTimer.stop();
        }

        final int totalSteps = 25; // 20ms * 25 = 500ms
        final int stepSize = sidebarWidth / totalSteps;
        if (isSidebarVisible) {
            // Slide out
            animationTimer = new Timer(20, new ActionListener() {
                int currentWidth = sidebarWidth;
                int step = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    step++;
                    currentWidth -= stepSize;
                    if (step >= totalSteps || currentWidth <= 0) {
                        currentWidth = 0;
                        animationTimer.stop();
                        isSidebarVisible = false;
                    }
                    sidebar.setPreferredSize(new Dimension(currentWidth, sidebar.getHeight()));
                    sidebar.revalidate();
                    sidebar.repaint();
                }
            });
        } else {
            // Slide in
            animationTimer = new Timer(20, new ActionListener() {
                int currentWidth = 0;
                int step = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    step++;
                    currentWidth += stepSize;
                    if (step >= totalSteps || currentWidth >= sidebarWidth) {
                        currentWidth = sidebarWidth;
                        animationTimer.stop();
                        isSidebarVisible = true;
                    }
                    sidebar.setPreferredSize(new Dimension(currentWidth, sidebar.getHeight()));
                    sidebar.revalidate();
                    sidebar.repaint();
                }
            });
        }
        animationTimer.start();
    }

    private void navigate(String buttonText) {
        dispose();
        switch (buttonText) {
            case "Search Flights":
                new FlightSearchForm().setVisible(true);
                break;
            case "Book Flight":
                new BookFlightForm().setVisible(true);
                break;
            case "Flight Status":
                new FlightStatusForm().setVisible(true);
                break;
            case "User Profile":
                new UserProfileForm().setVisible(true);
                break;
            case "Login":
                new LoginForm().setVisible(true);
                break;
            case "Register":
                new RegisterForm(new LoginForm()).setVisible(true);
                break;
            case "Contact Us":
                new ContactUsForm().setVisible(true);
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomeForm().setVisible(true));
    }
}