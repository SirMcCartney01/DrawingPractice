package com.pracrticalab;

import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;

import java.util.List;
import java.util.ArrayList;

public class MyPanel extends JPanel {

    private List<Ellipse2D> ellipse2DArrayList;
    private List<JLabel> labelArrayList;
    private int count;
    private Ellipse2D dragged;
    private Point offset;
    private int radius;

    public MyPanel() {
        super();
        this.ellipse2DArrayList = new ArrayList<>();
        this.labelArrayList = new ArrayList<>();
        this.count = 1;
        this.radius = 50;
        initPanel(this);
    }

    private void initPanel(JPanel panel) {
        panel.setBackground(Color.BLACK);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ellipse2DArrayList.add(new Ellipse2D.Float(e.getX() - (radius / 2), e.getY() - (radius / 2),
                        radius, radius));
                ellipse2DArrayList.add(new Ellipse2D.Float(e.getX() - (radius / 2), e.getY() - (radius / 2),
                        radius, radius));
                JLabel nodeText = new JLabel();
                nodeText.setText(Integer.toString(count));
                labelArrayList.add(nodeText);

                panel.repaint();
                count++;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                for (Ellipse2D node : ellipse2DArrayList) {
                    if (node.contains(e.getPoint())) {
                        dragged = node;
                        offset = new Point(node.getBounds().x - e.getX(), node.getBounds().y - e.getY());
                        panel.repaint();
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (dragged != null)
                    panel.repaint();
                dragged = null;
                offset = null;
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
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
        Point p = null;

        for (Ellipse2D node : ellipse2DArrayList) {
            graphics2D.setColor(Color.WHITE);
            Point to = node.getBounds().getLocation();
            to.x += radius / 2;
            to.y += radius / 2;
            if (p != null)
                graphics2D.draw(new Line2D.Float(p, to));
            p = to;

        }

        for (Ellipse2D node : ellipse2DArrayList) {
            graphics2D.setColor(new Color(117, 35, 37));
            graphics2D.fill(node);
            if (node == dragged) {
                graphics2D.setColor(Color.BLUE);
                graphics2D.draw(node);
            }
            graphics2D.setColor(Color.BLUE);

            FontMetrics fm = g.getFontMetrics();
            for (JLabel nText : labelArrayList) {
                int textWidth = fm.stringWidth(nText.getText());
                int x = node.getBounds().x;
                int y = node.getBounds().y;
                int width = node.getBounds().width;
                int height = node.getBounds().height;
                g.drawString(nText.getText(), x + ((width - textWidth)) / 2,
                        y + ((height - fm.getHeight()) / 2) + fm.getAscent());
            }
        }
        graphics2D.dispose();
    }
}
