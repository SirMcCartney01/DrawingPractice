package com.pracrticalab;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import java.awt.Dimension;
import java.awt.Container;

public class Window extends JFrame {

    public Window() {
        super("Practica 4");
        initComponents(this);
    }

    private void initComponents(JFrame frame) {
        frame.setSize(new Dimension(400, 300));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        Container container = frame.getContentPane();

        MyPanel myPanel = new MyPanel();

        container.add(myPanel);
    }
}
