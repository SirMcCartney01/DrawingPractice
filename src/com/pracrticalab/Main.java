package com.pracrticalab;

import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println("Failed to load Look and Feel");
            e.printStackTrace();
        }

	    Window window = new Window();
        window.setVisible(true);
    }
}
