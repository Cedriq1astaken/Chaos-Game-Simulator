import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

public class Panel extends JPanel implements ActionListener, MouseWheelListener, MouseMotionListener {
    private final int WIDTH = 900;
    private final int HEIGHT = 900;
    private final SidePanel sidePanel = new SidePanel(WIDTH/3, HEIGHT);

    private Timer timer;
    private ChaosGameSettings SETTINGS;
    private ChaosGameSimulator SIM;
    private double zoomFactor;
    private Point2D zoomCenter;

    public Panel(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setLayout(null);
        this.setFocusable(true);
        this.addMouseWheelListener(this);
        this.addMouseMotionListener(this);
//        this.add(sidePanel);

        this.SETTINGS = new ChaosGameSettings(4, 0, 0.5, 350, WIDTH, HEIGHT,
                                                new HashMap[]{
                                                        Helper.hashMapBuilder(1, 2),
                                                        Helper.hashMapBuilder(-1, 2)
                                                });
        this.SIM = new ChaosGameSimulator(SETTINGS);

        this.timer = new Timer(16, this);
        this.zoomFactor = 1.0;
        this.zoomCenter = new Point2D.Double((double) WIDTH/2, (double) HEIGHT/2);

        this.timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        g2d.setColor(Color.CYAN);
        ArrayList<Point2D> points = SIM.getPoints();

        for (Point2D p : points) {
            double dx = p.getX() - zoomCenter.getX();
            double dy = p.getY() - zoomCenter.getY();

            double x = zoomCenter.getX() + dx * zoomFactor;
            double y = zoomCenter.getY() + dy * zoomFactor;
            double radius = 1;

            Ellipse2D.Double point = new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius);
            g2d.fill(point);
        }
    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.isControlDown()){
            zoomCenter = new Point2D.Double(
                    e.getX(),
                    e.getY()
            );
            if (e.getWheelRotation() < 0 ) {
                zoomFactor *= 1.1; // zoom in
                zoomFactor = Math.min(zoomFactor, 4.0);
            } else if(e.getWheelRotation() > 0){
                zoomFactor /= 1.1; // zoom out
                zoomFactor = Math.max(zoomFactor, 0.5);
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.isControlDown()){
            zoomCenter = new Point2D.Double(
                    e.getX(),
                    e.getY()
            );
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SIM.nextPoint();
        repaint();
    }

}