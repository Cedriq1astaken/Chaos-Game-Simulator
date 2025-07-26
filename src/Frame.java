import javax.swing.*;

public class Frame extends JFrame {
    public Frame(double r, int vertex, double radius, int edge, String rule){
        System.setProperty("sun.java2d.opengl", "true");
        this.setTitle("Chaos Game Simulator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new Panel(r, vertex, radius, edge, rule));

        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    public Frame(double radius, int edge, String rule){
        System.setProperty("sun.java2d.opengl", "true");
        this.setTitle("Chaos Game Simulator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new MultiPanel(radius, edge, rule));

        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

}