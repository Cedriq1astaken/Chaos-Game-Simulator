package Custom;

import javax.swing.*;
import java.awt.*;

public class CollapseBtn extends JButton {
    private Polygon triangle;

    public CollapseBtn(int x, int y) {
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setFocusable(false);
        this.setBorder(null);
        this.setBackground(null);
        this.setBounds(x, y ,30, 20);
        this.setOpaque(false);

        this.triangle = new Polygon(
                new int[]{0, 30, 30}, // X coordinates
                new int[]{10, 0, 20}, // Y coordinates
                3
        );

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.WHITE);
        g2.fillPolygon(triangle);
        g2.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        return triangle.contains(x, y);
    }
}
