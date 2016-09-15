package com.pracrticalab;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Container;
import java.awt.FlowLayout;

public class Window extends JFrame {

    public static boolean DIRECTED = true;

    public Window() {
        super("Grafos");
        initComponents(this);
    }

    private void initComponents(JFrame frame) {
        frame.setSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        Container container = frame.getContentPane();
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Archivo");
        JMenu graphTypeMenu = new JMenu("Tipo de grafo");
        ButtonGroup menuGroup = new ButtonGroup();
        JRadioButtonMenuItem directedMenu = new JRadioButtonMenuItem("Dirigido", true);
        JRadioButtonMenuItem noDirectedMenu = new JRadioButtonMenuItem("No dirigido");
        JMenuItem exitMenu = new JMenuItem("Salir");

        MyPanel myPanel = new MyPanel();
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new FlowLayout());

        menuBar.add(fileMenu);
        menuGroup.add(directedMenu);
        menuGroup.add(noDirectedMenu);

        fileMenu.add(graphTypeMenu);
        graphTypeMenu.add(directedMenu);
        graphTypeMenu.add(noDirectedMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitMenu);

        frame.setJMenuBar(menuBar);

        directedMenu.addActionListener(e -> DIRECTED = true);
        noDirectedMenu.addActionListener(e -> DIRECTED = false);
        exitMenu.addActionListener(e -> System.exit(0));

        container.add(myPanel);
    }
}
