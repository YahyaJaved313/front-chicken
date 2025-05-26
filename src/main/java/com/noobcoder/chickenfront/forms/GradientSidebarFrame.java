package com.noobcoder.chickenfront.forms;

import javax.swing.*;
import java.awt.*;

public class GradientSidebarFrame extends JFrame {

    public GradientSidebarFrame() {
        setTitle("Gradient Sidebar Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Sidebar Panel with Gradient
        JPanel sidebarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();

                Color color1 = new Color(0, 0, 102); // Darker Blue
                Color color2 = new Color(102, 153, 255); // Lighter Blue

                GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        sidebarPanel.setLayout(new FlowLayout()); // Example layout for sidebar content

        // Main Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE); // Example background for content area
        contentPanel.setLayout(new BorderLayout()); // Example layout for content area

        // Add panels to the frame
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GradientSidebarFrame().setVisible(true);
        });
    }
}
