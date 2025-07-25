import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BoxCounting {
    public static double[][] boxCounting2D(ArrayList<Point2D> points, int limit) {
        Point2D[] normalPoints = Helper.normalize(new double[]{0.0, 1.0 - Helper.epsilon}, points);

        double[][] logLog = new double[limit + 1][2];

        double[] fractionalShiftX = {0.0, 0.5, 0.0, 0.5};
        double[] fractionalShiftY = {0.0, 0.0, 0.5, 0.5};
        int numShifts = fractionalShiftX.length;

        for (int i = 0; i <= limit; i++) {
            int boxesPerAxis = (int) Math.pow(2, i);
            double boxSize = 1.0 / boxesPerAxis;

            double totalBoxCountForEpsilon = 0.0;

            for (int s = 0; s < numShifts; s++) {
                Set<String> seenBoxes = new HashSet<>();

                double currentOffsetX = fractionalShiftX[s] * boxSize;
                double currentOffsetY = fractionalShiftY[s] * boxSize;

                for (Point2D point : normalPoints) {
                    double shiftedX = point.getX() + currentOffsetX;
                    double shiftedY = point.getY() + currentOffsetY;

                    int boxX = (int) Math.floor(shiftedX / boxSize);
                    int boxY = (int) Math.floor(shiftedY / boxSize);

                    seenBoxes.add(boxX + "," + boxY);
                }
                totalBoxCountForEpsilon += seenBoxes.size();
            }

            double averageBoxCount = totalBoxCountForEpsilon / numShifts;

            if (averageBoxCount > 0) {
                logLog[i][0] = Math.log(1.0 / boxSize);
                logLog[i][1] = Math.log(averageBoxCount);
            } else {
                logLog[i][0] = Math.log(1.0 / boxSize);
                logLog[i][1] = Double.NaN;
            }
        }

        return logLog;
    }
}
