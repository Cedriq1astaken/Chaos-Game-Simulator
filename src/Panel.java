import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Panel extends JPanel implements ActionListener, MouseWheelListener, MouseMotionListener {
    private final int WIDTH = 900;
    private final int HEIGHT = 900;

    private  SidePanel sidePanel = new SidePanel();
    private Timer timer;
    private ChaosGameSettings SETTINGS;
    private ChaosGameSimulator SIM;
    private double zoomFactor;
    private Point2D zoomCenter;
    private boolean isPaused = false;
    private double dimension = 0.0;

    //Labels
    private JLabel countLabel;
    private JLabel vertexCount;
    private JLabel dimensionLabel;

    //Buttons
    private JButton pause;
    private JButton dimensionBtn;
    private JButton add;

    public Panel(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setLayout(null);
        this.setFocusable(true);
        this.addMouseWheelListener(this);
        this.addMouseMotionListener(this);

        this.SETTINGS = new ChaosGameSettings(3, 0, 0.6, 350, WIDTH, HEIGHT,
                                                new HashMap[]{});
        this.SIM = new ChaosGameSimulator(SETTINGS);

        this.timer = new Timer(0, this);
        this.zoomFactor = 1.0;
        this.zoomCenter = new Point2D.Double((double) WIDTH/2, (double) HEIGHT/2);


//        //SidePanel
//        sidePanel = new SidePanel();
//        sidePanel.setBounds(0, 0, WIDTH/3, HEIGHT);
//        this.add(sidePanel);
//
//        JButton toggle = sidePanel.getToggleButton();
//        toggle.setBounds(WIDTH/3, 10, 50, 30);
//        this.add(toggle);

        this.setLabels();
        this.setButtons();
        this.timer.start();
    }

    public void setLabels(){
        countLabel = new JLabel("Point count: 0");
        countLabel.setBounds(10, 10, 200, 20);
        countLabel.setForeground(Color.WHITE);

        vertexCount = new JLabel("Vertex: " + SETTINGS.getnVertexAnchor());
        vertexCount.setBounds(10, 30, 100, 20);
        vertexCount.setForeground(Color.WHITE);

        dimensionLabel = new JLabel("Dimension: xx");
        dimensionLabel.setBounds(10, 50, 100, 20);
        dimensionLabel.setForeground(Color.WHITE);


        this.add(countLabel);
        this.add(vertexCount);
        this.add(dimensionLabel);
    }

    public void setButtons(){
        pause = new JButton();
        pause.setBounds(WIDTH - 80, 10, 70, 30);
        pause.setBackground(new Color(0, 0 ,0 ,0));
        pause.setText("pause");
        pause.setFocusable(false);
        pause.addActionListener(e ->{
            isPaused = !isPaused;
        });

        dimensionBtn = new JButton();
        dimensionBtn.setBounds(WIDTH - 80, 40, 70, 30);
        dimensionBtn.setBackground(new Color(0, 0 ,0 ,0));
        dimensionBtn.setText("dimension");
        dimensionBtn.setFocusable(false);
        dimensionBtn.addActionListener(e ->{
            System.out.println(Arrays.deepToString(Helper.boxCounting2D(SIM.getPoints(), 9)));
        });

        add = new JButton();
        add.setBounds(WIDTH - 80, 70, 70, 30);
        add.setBackground(new Color(0, 0 ,0 ,0));
        add.setText("add");
        add.setFocusable(false);
        add.addActionListener(e ->{
            isPaused = true;
            for(int i = 0; i < 1_000; i++){
                SIM.nextPoint();

            }
        });

        this.add(pause);
        this.add(dimensionBtn);
        this.add(add);
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
        if(!isPaused) {
            SIM.nextPoint();
        }
        else{
        }
        repaint();
        countLabel.setText("Point count: " + SIM.getPoints().size());
        pause.setText(isPaused ? "Resume" : "Pause");
        dimensionLabel.setText("Dimension: " + dimension);

    }

}