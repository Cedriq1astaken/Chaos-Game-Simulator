import java.awt.geom.Point2D;
import java.util.*;

public class Helper {
    public static double epsilon = 10e-9;


    public static int mod(int a, int b){
        return (a % b + b) % b;
    }
    public static HashMap<String, Integer> hashMapBuilder(int proximity, int occurrence){
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("proximity", proximity);
        hash.put("occurrence", occurrence);
        return hash;
    }
    public static double[] domain(ArrayList<Point2D> points){
        double smallest = points.getFirst().getX();
        double biggest = points.getFirst().getX();

        for(int i = 1; i < points.size(); i++){
            Point2D point = points.get(i);
            smallest = Math.min(smallest, point.getX());
            biggest = Math.max(biggest, point.getX());
        }
        return new double[]{smallest, biggest};
    }

    public static double[] range(ArrayList<Point2D> points){
        double smallest = points.getFirst().getY();
        double biggest = points.getFirst().getY();

        for(int i = 1; i < points.size(); i++){
            Point2D point = points.get(i);
            smallest = Math.min(smallest, point.getY());
            biggest = Math.max(biggest, point.getY());
        }
        return new double[]{smallest, biggest};
    }

    public static Point2D[] normalize(double[] scale, ArrayList<Point2D> points){
        int size = points.size();
        Point2D[] newPoints = new Point2D[points.size()];
        double[] domain = domain(points);
        double[] range = range(points);

        double xSpan = domain[1] - domain[0];
        double ySpan = range[1] - range[0];
        double span = Math.max(xSpan, ySpan);

        for(int i = 0; i< size; i++){
            double x = (points.get(i).getX() - domain[0]) / span * (scale[1] - scale[0]) + scale[0];
            double y = (points.get(i).getY() - range[0]) / span * (scale[1] - scale[0]) + scale[0];
            newPoints[i] = new Point2D.Double(x, y);
        }
        return newPoints;
    }

    public static double[][] point2dToDouble(Point2D[] data){
        ArrayList<double[]> points = new ArrayList<>();

        for(Point2D datum: data){
            points.add(new double[]{
                    datum.getX(),
                    datum.getY()
            });
        }
        return points.toArray(new double[0][0]);
    }

    public static int[][] toMatrix(ArrayList<Point2D> data, int gridSize){
        Point2D[] points = normalize(new double[]{0.0, gridSize - epsilon}, data);
        int[][] matrix = new int[gridSize][gridSize];

        for(Point2D point: points){
            int col = (int) point.getX();
            int row = (int) point.getY();
            matrix[row][col] = 1;
        }
        return matrix;
    }

    public static HashMap<String, Integer>[] skipsConditionsWriter(String rules){
        HashMap<String, Integer>[] map = new HashMap[rules.length()/2];
        for(int i = 0; i < rules.length(); i += 2){
            map[i/2] = hashMapBuilder(Integer.parseInt(String.valueOf(rules.charAt(i))), Integer.parseInt(String.valueOf(rules.charAt(i + 1))));
        }
        return map;
    }
}
