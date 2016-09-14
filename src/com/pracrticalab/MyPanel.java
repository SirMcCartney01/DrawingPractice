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

                JLabel nodeText = new JLabel();
                nodeText.setText(Integer.toString(count++));
                labelArrayList.add(nodeText);

                Ellipse2D current = findEllipse(e.getPoint());
                int clicks = 0;
                Point2D auxPoint;

                if (current != null && e.getClickCount() >= 2) {
                    clicks += 2;
                    if (clicks == 2)
                        auxPoint = e.getPoint();
                    else if (clicks == 4) {
                        // TODO: addLinea

                        if (Window.DIRECTED) {
                            // TODO: If is a directed graph
                        } else {
                            // TODO: If is not a directed graph
                        }
                    }
                }

                panel.repaint();
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
            public void mouseMoved(MouseEvent e) {
                if(findEllipse(e.getPoint()) == null)
                    setCursor(Cursor.getDefaultCursor());
                else
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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

        for (Ellipse2D node : ellipse2DArrayList) {
            graphics2D.setColor(new Color(117, 35, 37));
            graphics2D.fill(node);
            if (node == dragged) {
                graphics2D.setColor(Color.BLUE);
                graphics2D.draw(node);
            }
            graphics2D.setColor(Color.BLUE);
        }

        graphics2D.setColor(Color.WHITE);
        for (int i = 0; i < labelArrayList.size(); i++)
            graphics2D.drawString(labelArrayList.get(i).getText(), (int) ellipse2DArrayList.get(i).getX(),
                    (int) ellipse2DArrayList.get(i).getY());

        graphics2D.dispose();
    }

    @Nullable
    private Ellipse2D findEllipse(Point2D p) {
        for (Ellipse2D node : ellipse2DArrayList) {
            if (node.contains(p))
                return node;
        }
        return null;
    }
}
