package com.pracrticalab;

import com.sun.istack.internal.Nullable;

import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import java.util.List;
import java.util.ArrayList;

public class MyPanel extends JPanel {

    private List<Ellipse2D> ellipses;
    private List<Line2D> lines;
    private Ellipse2D current;
    private Line2D	 line;
    private Point2D	 auxPunto;
    private int clicks = 0, count = 0;

    private static final int DIMENSION = 50;

    public MyPanel() {
        super();
        this.ellipses = new ArrayList<>();
        this.current = null;
        this.lines = new ArrayList<>();
        this.line = null;
        this.auxPunto = null;
        initPanel(this);
    }

    private void initPanel(JPanel panel) {
        panel.setBackground(Color.BLACK);

        panel.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                current = findEllipse(e.getPoint());
                if(current == null){
                    add(e.getPoint());
                    count++;
                }
                panel.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                current = findEllipse(e.getPoint());
                if (current != null && e.getClickCount() >= 2){
                    clicks += e.getClickCount();
                    System.out.println("Numero de clicks: " + clicks);

                    if (clicks == 2){
                        auxPunto = e.getPoint();
                        System.out.println("Posicion actual X: " + e.getX() + " Y: " + e.getY());
                    }
                    else if (clicks == 4){
                        addLinea(auxPunto, e.getPoint());
                        clicks=0;
                    }
                }
                panel.repaint();
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e){
                if(findEllipse(e.getPoint())==null)
                    setCursor(Cursor.getDefaultCursor());
                else
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseDragged(MouseEvent e){
                if(current != null){
                    int x = e.getX();
                    int y = e.getY();
                    current.setFrame(x - DIMENSION / 2, y - DIMENSION / 2, DIMENSION, DIMENSION);
                    //nombre.setX(x);
                    //nombre.setY(y);
                    //linea.setLine(auxPunto.getX(), auxPunto.getY(), x, y);
                }
                panel.repaint();
            }
        });
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(117, 35, 37));
        for (Ellipse2D ellipse : ellipses){
            g2.fill(ellipse);
            //g2.drawString(nombres[i].getNombre(),(float)nombres[i].getX(),(float)nombres[i].getY());
        }

        for(Line2D line : lines){
            g2.setColor(Color.WHITE);
            if (!Window.DIRECTED)
                g2.draw(line);
            else {
                g2.draw(line);
                //drawArrowHead(g2, );
            }
        }
    }

    @Nullable
    private Ellipse2D findEllipse(Point2D p){
        for (Ellipse2D ellipse : ellipses){
            if(ellipse.contains(p)){
                return ellipse;
            }
        }

        return null;
    }

    private void add(Point2D p){
        double x = p.getX();
        double y = p.getY();
        current = new Ellipse2D.Double(x - DIMENSION / 2, y - DIMENSION / 2, DIMENSION, DIMENSION);
        //nombre = new NombresNodo(""+cuenta,p.getX(),p.getY());
        //nombres[cuenta] = nombre;
        ellipses.add(current);
    }

    private void addLinea(Point2D first,Point2D second){
        double x  = first.getX();
        double y  = first.getY();
        double x1  = second.getX();
        double y1  = second.getY();
        line = new Line2D.Double(x, y, x1, y1);
        lines.add(line);
    }

    private void remove(Ellipse2D s){
        if(s == null)
            return;
        if(s == current)
            current = null;

        ellipses.remove(s);
    }

    private void drawArrowHead(Graphics2D g2, Point start, Point end, Color color) {
        double phi = Math.toRadians(30);
        int size = 15;

        g2.setPaint(color);
        double dy = start.y - end.y;
        double dx = start.x - end.x;
        double theta = Math.atan2(dy, dx);
        double x, y, rho = theta + phi;
        for(int j = 0; j < 2; j++) {
            x = start.x - size * Math.cos(rho);
            y = start.y - size * Math.sin(rho);
            g2.draw(new Line2D.Double(start.x, start.y, x, y));
            rho = theta - phi;
        }
    }
}
