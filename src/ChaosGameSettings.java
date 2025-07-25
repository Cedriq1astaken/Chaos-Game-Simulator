import java.awt.geom.Point2D;
import java.util.HashMap;

public class ChaosGameSettings {
    private int nVertexAnchor;
    private int nEdgeAnchor;
    private double distanceRatio;
    private double centroidToVertex;
    private Point2D.Double initialPoint;
    private double r;

    //Conditions;
    private HashMap<String, Integer>[] skipsConditions;

    public ChaosGameSettings(int nVertexAnchor, int nEdgeAnchor, double distanceRatio,
                             double centroidToVertex, double width, double height,
                             HashMap<String, Integer>[] skipsConditions) {
        this.r = distanceRatio;
        this.nVertexAnchor = nVertexAnchor;
        this.nEdgeAnchor = nEdgeAnchor;
        this.distanceRatio = distanceRatio;
        this.centroidToVertex = centroidToVertex;
        this.initialPoint = new Point2D.Double(width/2, height/2);
        this.skipsConditions = skipsConditions;
    }

    public int getnVertexAnchor() {
        return nVertexAnchor;
    }

    public double getDistanceRatio() {
        return distanceRatio;
    }

    public double getCentroidToVertex() {
        return centroidToVertex;
    }

    public Point2D.Double getInitialPoint() {
        return initialPoint;
    }

    public int getnEdgeAnchor() {
        return nEdgeAnchor;
    }


    public HashMap<String, Integer>[] getSkipsConditions() {
        return skipsConditions;
    }

    public double getR() {
        return r;
    }
}