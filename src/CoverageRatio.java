import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

public class CoverageRatio {
    public static double coverage(ArrayList<Point2D> points, int vertex, double radius){
        double totalArea = (0.5) * vertex * radius * radius * Math.sin(2 * Math.PI/vertex);
        double[] domain = Helper.domain(points);
        double[] range = Helper.range(points);
        double xGridLength = (domain[1] - domain[0]) / 256;
        double yGridLength = (range[1] - range[0]) / 256;
        int[][] grid = Helper.toMatrix(points, 256);
        int occupied = 0;

        for (int[] row : grid) {
            for (int cell : row) {
                occupied += cell;
            }
        }

        System.out.println();

        return (double) (occupied*xGridLength* yGridLength)/(totalArea);
    }
}
