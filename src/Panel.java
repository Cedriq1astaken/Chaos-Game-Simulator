import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Panel extends JPanel implements ActionListener, MouseWheelListener, MouseMotionListener {
    private final int WIDTH = 900;
    private final int HEIGHT = 900;
    private double radius;

    private Timer timer;
    private ChaosGameSettings SETTINGS;
    private ChaosGameSimulator SIM;
    private double zoomFactor;
    private Point2D zoomCenter;
    private boolean isPaused = false;
    private double dimension = 0.0;
    GlidingBoxLacunarity calculator = new GlidingBoxLacunarity(2, 4);

    //Labels
    private JLabel rLabel;
    private JLabel countLabel;
    private JLabel vertexCount;
    private JLabel dimensionLabel;
    private JLabel edgeAnchor;
    private JLabel largestComponentLabel ;

    //Buttons
    private JButton pause;
    private JButton dimensionBtn;
    private JButton add;
    private JButton largestComponent;
    private JButton newFractal;

    public Panel(double r, int vertex, double radius, int edge, String rule){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setLayout(null);
        this.setFocusable(true);
        this.addMouseWheelListener(this);
        this.addMouseMotionListener(this);
        this.radius = radius;

        this.SETTINGS = new ChaosGameSettings(vertex, edge, r, radius, WIDTH, HEIGHT, Helper.skipsConditionsWriter(rule));
        this.SIM = new ChaosGameSimulator(SETTINGS);

        this.timer = new Timer(0, this);
        this.zoomFactor = 1.0;
        this.zoomCenter = new Point2D.Double((double) WIDTH/2, (double) HEIGHT/2);

        this.setLabels();
        this.setButtons();
        this.timer.start();

    }

    public void setLabels(){
        rLabel = new JLabel("R value: " + SETTINGS.getR());
        rLabel.setBounds(10, 10, 200, 20);
        rLabel.setForeground(Color.WHITE);

        countLabel = new JLabel("Point count: 0");
        countLabel.setBounds(10, 30, 200, 20);
        countLabel.setForeground(Color.WHITE);

        vertexCount = new JLabel("Vertex: " + SETTINGS.getnVertexAnchor());
        vertexCount.setBounds(10, 50, 200, 20);
        vertexCount.setForeground(Color.WHITE);

        edgeAnchor = new JLabel("Edge: " + SETTINGS.getnEdgeAnchor());
        edgeAnchor.setBounds(10, 70, 200, 20);
        edgeAnchor.setForeground(Color.WHITE);

        dimensionLabel = new JLabel("Dimension: xx");
        dimensionLabel.setBounds(10, 90, 200, 20);
        dimensionLabel.setForeground(Color.WHITE);

        largestComponentLabel = new JLabel("Largest Component: xx");
        largestComponentLabel.setBounds(10, 110, 200, 20);
        largestComponentLabel.setForeground(Color.WHITE);

        this.add(rLabel);
        this.add(countLabel);
        this.add(vertexCount);
        this.add(dimensionLabel);
        this.add(edgeAnchor);
        this.add(largestComponentLabel);
    }

    public void setButtons(){
        pause = new JButton();
        pause.setBounds(WIDTH - 160, 10, 150, 30);
        pause.setBackground(new Color(0, 0 ,0 ,0));
        pause.setText("pause");
        pause.setFocusable(false);
        pause.addActionListener(e ->{
            isPaused = !isPaused;
        });

        dimensionBtn = new JButton();
        dimensionBtn.setBounds(WIDTH - 160, 40, 150, 30);
        dimensionBtn.setBackground(new Color(0, 0 ,0 ,0));
        dimensionBtn.setText("Lacunarity slope");
        dimensionBtn.setFocusable(false);
        dimensionBtn.addActionListener(e ->{
            Point2D[] data = SIM.getPoints().toArray(new Point2D[0]);
            int[] boxSizes = {1, 2, 3, 4, 5, 6};
            double[] lacunarityValues = calculator.calculateLacunarity(Helper.point2dToDouble(data), boxSizes);
            dimension = calculator.lacunaritySlope(lacunarityValues);
        });

        add = new JButton();
        add.setBounds(WIDTH - 160, 70, 150, 30);
        add.setBackground(new Color(0, 0 ,0 ,0));
        add.setText("add");
        add.setFocusable(false);
        add.addActionListener(e ->{
            isPaused = true;
            for(int i = 0; i < 10_000; i++){
                SIM.nextPoint();
            }
        });

        largestComponent = new JButton();
        largestComponent.setBounds(WIDTH - 160, 100, 150, 30);
        largestComponent.setBackground(new Color(0, 0 ,0 ,0));
        largestComponent.setText("largestComponent");
        largestComponent.setFocusable(false);
        largestComponent.addActionListener(e ->{
            isPaused = true;
            largestComponentLabel.setText("Largest Component: " + ComponentRatio.largestComponentRatio(SIM.getPoints()));
            System.out.println(radius);
            System.out.println(CoverageRatio.coverage(SIM.getPoints(), SETTINGS.getnVertexAnchor(), radius));
        });

        newFractal = new JButton();
        newFractal.setBounds(WIDTH - 160, 130, 150, 30);
        newFractal.setBackground(new Color(0, 0 ,0 ,0));
        newFractal.setText("New Fractal");
        newFractal.setFocusable(false);
        newFractal.addActionListener(e ->{
            Main.launch();
        });

        this.add(pause);
        this.add(dimensionBtn);
        this.add(add);
        this.add(largestComponent);
        this.add(newFractal);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        ArrayList<Point2D> points = SIM.getPoints();

        for (Point2D p : points) {
            g2d.setColor(Color.CYAN);
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
        dimensionLabel.setText("Lacunarity slope: " + dimension);
    }

}