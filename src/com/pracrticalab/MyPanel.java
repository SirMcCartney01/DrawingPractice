package com.pracrticalab;

import com.sun.istack.internal.Nullable;

import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import java.util.List;
import java.util.ArrayList;

public class MyPanel extends JPanel {

    private List<Ellipse2D> ellipses;
    private List<JLabel> labels;
    private int count;
    private Ellipse2D dragged;
    private Point offset, pointStart, pointEnd;
    private int radius;
    private static boolean isDragged = true;

    public MyPanel() {
        super();
        this.ellipses = new ArrayList<>();
        this.labels = new ArrayList<>();
        this.count = 1;
        this.radius = 50;
        initPanel(this);
    }

    private void initPanel(JPanel panel) {
        panel.setBackground(Color.BLACK);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ellipses.add(new Ellipse2D.Float(e.getX() - (radius / 2), e.getY() - (radius / 2),
                        radius, radius));

                JLabel nodeText = new JLabel();
                nodeText.setText(Integer.toString(count++));
                labels.add(nodeText);

                Ellipse2D current = findEllipse(e.getPoint());
                int clicks = 0;
                Point2D auxPoint;

                if (current != null && e.getClickCount() >= 2) {
                    clicks += e.getClickCount();
                    auxPoint = e.getPoint();
                    if (clicks == 2) {
                        System.out.println("Got two clicks at X:" + auxPoint.getX() + " Y:" + auxPoint.getY()); // Debug
                    }
                    else if (clicks == 4) {
                        // TODO: add linea
                        System.out.println("Got four clicks at X:" + auxPoint.getX() + " Y:" + auxPoint.getY()); // Debug
                    }
                }

                panel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                for (Ellipse2D node : ellipses) {
                    if (node.contains(e.getPoint())) {
                        dragged = node;
                        offset = new Point(node.getBounds().x - e.getX(), node.getBounds().y - e.getY());
                        panel.repaint();
                        break;
                    }
                }

                pointStart = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (dragged != null)
                    panel.repaint();

                dragged = null;
                offset = null;

                pointStart = null;
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if(findEllipse(e.getPoint()) == null)
                    setCursor(Cursor.getDefaultCursor());
                else
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                pointEnd = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragged != null && offset != null) {
                    Point to = e.getPoint();
                    to.x += offset.x;
                    to.y += offset.y;

                    final Rectangle bounds = dragged.getBounds();
                    bounds.setLocation(to);
                    dragged.setFrame(bounds);

                    panel.repaint();
                }

                pointEnd = e.getPoint();
                panel.repaint();
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g.create();

        graphics2D.setColor(new Color(117, 35, 37));
        for (Ellipse2D node : ellipses) {
            graphics2D.fill(node);
            if (node == dragged)
                graphics2D.draw(node);
        }

        graphics2D.setColor(Color.WHITE);
        for (int i = 0; i < ellipses.size(); i++)
            graphics2D.drawString(labels.get(i).getText(), ellipses.get(i).getBounds().x,
                    ellipses.get(i).getBounds().y);

        graphics2D.setColor(Color.WHITE);
        if (pointStart != null) {
            if(Window.DIRECTED)
                graphics2D.draw(new Line2D.Float(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y));
            else {
                Line2D.Float line = new Line2D.Float(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
                graphics2D.draw(line);
                drawArrowHead(graphics2D, pointEnd, pointStart, Color.WHITE);
            }
        }

        graphics2D.dispose();
    }

    @Nullable
    private Ellipse2D findEllipse(Point2D p) {
        for (Ellipse2D node : ellipses) {
            if (node.contains(p))
                return node;
        }
        return null;
    }

    private void drawArrowHead(Graphics2D g2, Point tip, Point tail, Color color)
    {
        double phi = Math.toRadians(30);
        int size = 15;

        g2.setPaint(color);
        double dy = tip.y - tail.y;
        double dx = tip.x - tail.x;
        double theta = Math.atan2(dy, dx);
        double x, y, rho = theta + phi;
        for(int j = 0; j < 2; j++)
        {
            x = tip.x - size * Math.cos(rho);
            y = tip.y - size * Math.sin(rho);
            g2.draw(new Line2D.Double(tip.x, tip.y, x, y));
            rho = theta - phi;
        }
    }
}
