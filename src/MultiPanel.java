import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class MultiPanel extends JPanel implements ActionListener {
    private final int WIDTH = 1200;
    private final int HEIGHT = 900;
    private final int LIMIT = 100;
    private final double STEP = 0.05;

    private Timer timer;
    private int I = 0;
    private int threshold = -1;

    private ArrayList<Rectangle2D> rectangles = new ArrayList<>();

    private MultiSimulator multiSim;


    public MultiPanel(double radius, int edge, String rule) {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setLayout(null);
        this.setFocusable(true);

        multiSim = new MultiSimulator(radius, edge, rule, WIDTH, HEIGHT, LIMIT, STEP);


        this.timer = new Timer(0, this);
        this.timer.start();
    }

    public void squares(){
        ArrayList<ChaosGameSimulator> sims = multiSim.nextSims();

        double w = (double) WIDTH /(LIMIT);
        double h = (double)  HEIGHT/(sims.size());

        for(int j = sims.size() - 1; j >= 0 ; j--){
            Rectangle2D rect = new Rectangle2D.Double(
                    w * I, h * j, w, h
            );
            rectangles.add(rect);
            double ratio = ComponentRatio.largestComponentRatio(sims.get(j).getPoints());
            if(ratio > 0.8){
                threshold++;
            }
        }
        I++;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        for(int j = 0; j < rectangles.size(); j++){
            if(j < threshold){
                g2d.setColor(Color.decode("#111111"));
            }
            else if(j == threshold){
                System.out.println("d");
                g2d.setColor(Color.decode("#9933FF"));
            }
            else{
                g2d.setColor(Color.decode("#FFDD00"));
            }
            Rectangle2D rect = rectangles.get(j);
            g2d.fill(rect);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (I == 0 ){
            squares();
        }
    }
}
