import java.awt.geom.Point2D;
import java.util.*;


public class ChaosGameSimulator {
    private ArrayList<Point2D> points;
    private ArrayList<Point2D> anchorPoints;
    private ChaosGameSettings settings;
    private Point2D target;
    private Random rand = new Random();
    private Deque<Integer> lastChoices = new ArrayDeque<>();
    private boolean removed = false;


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
        int n = conditionsCheck();

        if(lastChoices.isEmpty() || lastChoices.getLast() == n)
            lastChoices.add(n);
        else{
            lastChoices.clear();
            lastChoices.add(n);
        }

        target = linearInterpolation(target, anchorPoints.get(n), distanceRatio);

        if (points.size() > anchorPoints.size() + 6 && removed){
            for(int i = 0; i < 6; i++){
                points.removeLast();
            }
            removed = true;
        }
        points.add(target);
    }

    public Point2D linearInterpolation(Point2D p1, Point2D p2, double t){
        return new Point2D.Double(
                p1.getX() * (1 - t) + p2.getX() * t,
                p1.getY() * (1 - t) + p2.getY() * t
        );
    }

    public int conditionsCheck(){
        HashMap<String, Integer>[] conditions = settings.getSkipsConditions();
        int newChoice;
        boolean isValid;

        do {
            isValid = true;
            newChoice = rand.nextInt(anchorPoints.size());

            for (HashMap<String, Integer> condition : conditions) {
                int requiredOccurrence = condition.get("occurrence");
                int proximity = condition.get("proximity");

                // Check if current streak matches required condition
                if (lastChoices.size() >= requiredOccurrence) {
                    int lastChosen = lastChoices.getLast();
                    int blocked = Helper.mod(lastChosen + proximity, anchorPoints.size());

                    if (newChoice == blocked) {
                        isValid = false;
                        break;
                    }
                }
            }
        } while (!isValid);

        return newChoice;
    }
}
