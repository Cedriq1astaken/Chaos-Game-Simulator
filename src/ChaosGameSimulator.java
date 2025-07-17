import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

public class ChaosGameSimulator {
    private ArrayList<Point2D> points;
    private ArrayList<Point2D> anchorPoints;
    private ChaosGameSettings settings;
    private Point2D target;
    private Random rand = new Random();


    public ChaosGameSimulator(ChaosGameSettings settings){
        this.settings = settings;
        this.points = new ArrayList<>();
        this.anchorPoints = new ArrayList<>();
        this.target = settings.getInitialPoint();
        this.init();
    }

    public ArrayList<Point2D> getPoints() {
        return points;
    }

    public void init(){
        double x0 = settings.getInitialPoint().x;
        double y0 = settings.getInitialPoint().y;
        double angle = 2 * Math.PI/settings.getnVertexAnchor();

        for(int i = 0; i < settings.getnVertexAnchor(); i++){
            Point2D p = new Point2D.Double(
                x0 + settings.getCentroidToVertex() * Math.cos(angle * i),
                y0 + settings.getCentroidToVertex() * Math.sin(angle * i)
            );
            points.add(p);
            anchorPoints.add(p);
        }

        //Add Edge anchor points
        int n = settings.getnEdgeAnchor();
        int v = settings.getnVertexAnchor();
        for(int i = 0; i < v; i++){
            double distance = 1.0/(n + 1);
            for(int j = 1; j <= n; j++){
                Point2D p1 = points.get(i);
                Point2D p2 = points.get((i + 1)%v);


                Point2D p = linearInterpolation(p1, p2, distance * j);
                points.add(p);
                anchorPoints.add(p);
            }
        }
    }

    public void nextPoint(){
        double distanceRatio = settings.getDistanceRatio();
        int choice = rand.nextInt(anchorPoints.size());

        target = linearInterpolation(target, anchorPoints.get(choice), distanceRatio);
        points.add(target);
    }

    public Point2D linearInterpolation(Point2D p1, Point2D p2, double t){
        Point2D p = new Point2D.Double(
                p1.getX() * (1 - t) + p2.getX() * t,
                p1.getY() * (1 - t) + p2.getY() * t
        );
        return p;
    }


}
