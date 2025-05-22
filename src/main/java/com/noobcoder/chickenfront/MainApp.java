package com.noobcoder.chickenfront;

import com.noobcoder.chickenfront.forms.LoginForm;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}