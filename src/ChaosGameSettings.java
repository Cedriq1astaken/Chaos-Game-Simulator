import java.awt.geom.Point2D;

public class ChaosGameSettings {
    private int nVertexAnchor;
    private int nEdgeAnchor;
    private double distanceRatio;
    private double centroidToVertex;
    private Point2D.Double initialPoint;

    public ChaosGameSettings(int nVertexAnchor, int nEdgeAnchor, double distanceRatio,
                             double centroidToVertex, double width, double height) {
        this.nVertexAnchor = nVertexAnchor;
        this.nEdgeAnchor = nEdgeAnchor;
        this.distanceRatio = distanceRatio;
        this.centroidToVertex = centroidToVertex;
        this.initialPoint = new Point2D.Double(width/2, height/2);
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
}