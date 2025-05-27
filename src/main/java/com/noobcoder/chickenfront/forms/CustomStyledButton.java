package com.noobcoder.chickenfront.forms;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.RoundRectangle2D;

public class CustomStyledButton extends JButton {
    private final int radius;
    private final Color fillColor;
    private final Color borderColor;
    private final int borderThickness;

    public CustomStyledButton(String text, int radius, Color fillColor, Color borderColor, int borderThickness) {
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
