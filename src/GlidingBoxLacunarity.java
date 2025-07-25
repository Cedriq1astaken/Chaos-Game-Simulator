import java.util.*;

/**
 * Efficient implementation of the Gliding Box Lacunarity Algorithm by Partially by Claude (LLM)
 * Based on the paper: "An efficient implementation of the gliding box lacunarity algorithm"
 * by Charles R. Tolle, Timothy R. McJunkin, David J. Gorsich (2008)
 */
public class GlidingBoxLacunarity {
    private int dimensions;
    private int L; // power of 2 for grid subdivision (s = 2^L)
    private int s; // number of boxes per dimension (s = 2^L)
    private int[] baseBox; // base-level grid array
    private int totalGridBoxes; // total number of grid boxes (s^N)

    // Data bounds for normalization
    private double[] xMin, xMax, xRange;

    /**
     * Constructor
     * @param dimensions Number of dimensions (N)
     * @param L Grid subdivision parameter (s = 2^L boxes per dimension)
     */
    public GlidingBoxLacunarity(int dimensions, int L) {
        this.dimensions = dimensions;
        this.L = L;
        this.s = (int) Math.pow(2, L);
        this.totalGridBoxes = (int) Math.pow(s, dimensions);
        this.baseBox = new int[totalGridBoxes];
        this.xMin = new double[dimensions];
        this.xMax = new double[dimensions];
        this.xRange = new double[dimensions];
    }

    /**
     * Main method to calculate lacunarity for a dataset
     * @param dataSet N-dimensional dataset, each point is double[dimensions]
     * @param boxSizes Array of box sizes (in terms of base grid units)
     * @return Array of lacunarity values corresponding to each box size
     */
    public double[] calculateLacunarity(double[][] dataSet, int[] boxSizes) {
        // Step 1: Bin the data into base-level grid
        binDataToGrid(dataSet);

        double[] lacunarityResults = new double[boxSizes.length];

        // Step 2: Calculate lacunarity for each box size
        for (int i = 0; i < boxSizes.length; i++) {
            lacunarityResults[i] = calculateLacunarityForBoxSize(boxSizes[i]);
        }

        return lacunarityResults;
    }

    /**
     * Bins the dataset into the base-level grid using equations (18) and (19)
     */
    private void binDataToGrid(double[][] dataSet) {
        Arrays.fill(baseBox, 0);

        // Find data bounds (equations 15, 16)
        for (int dim = 0; dim < dimensions; dim++) {
            xMin[dim] = Double.MAX_VALUE;
            xMax[dim] = Double.MIN_VALUE;

            for (double[] point : dataSet) {
                xMin[dim] = Math.min(xMin[dim], point[dim]);
                xMax[dim] = Math.max(xMax[dim], point[dim]);
            }
            // Handle cases where xRange might be zero (e.g., all points have the same coordinate)
            xRange[dim] = xMax[dim] - xMin[dim];
            if (xRange[dim] == 0) {
                // If range is zero, give it a small epsilon to avoid division by zero
                // This might not be the ideal solution for all datasets, but prevents crashes.
                // A more robust solution might involve scaling based on data distribution or
                // treating single-valued dimensions differently.
                xRange[dim] = 1e-9;
            }
        }

        // Bin each data point
        for (double[] point : dataSet) {
            int gridIndex = calculateGridIndex(point);
            if (gridIndex >= 0 && gridIndex < totalGridBoxes) {
                baseBox[gridIndex]++;
            }
        }
    }

    /**
     * Calculates grid index for a data point using equations (18) and (19)
     */
    private int calculateGridIndex(double[] point) {
        int index = 0;
        int multiplier = 1;

        for (int dim = 0; dim < dimensions; dim++) {
            // Equation (18): k_i = s * ceiling((x_i - x_min) / x_range)
            double normalized = (point[dim] - xMin[dim]) / xRange[dim];
            int k = (int) Math.ceil(s * normalized);

            // Clamp to valid range [1, s]
            k = Math.max(1, Math.min(s, k));

            // Equation (19): Convert to array index (0-based)
            index += (k - 1) * multiplier;
            multiplier *= s;
        }

        return index;
    }

    /**
     * Calculates lacunarity for a specific box size
     */
    private double calculateLacunarityForBoxSize(int boxRadius) {
        BoxSlideResult result = new BoxSlideResult();

        // Initialize sliding box parameters
        int[] pos = new int[dimensions];
        int[] llec = new int[dimensions]; // lower left edge coordinates
        int[] rlec = new int[dimensions]; // right limit edge coordinates

        // Set up sliding limits (box can slide from 0 to s-boxRadius)
        for (int dim = 0; dim < dimensions; dim++) {
            llec[dim] = 0;
            rlec[dim] = s - boxRadius;
        }

        // Start the recursive sliding box algorithm
        boxSlide(pos, llec, rlec, dimensions - 1, boxRadius, result);

        // Calculate lacunarity using equation (14)
        // Ensure denominator is not zero
        if (result.totalBoxes > 0 && result.sumP > 0) {
            double numerator = result.totalBoxes * result.sumP2;
            double denominator = result.sumP * result.sumP;
            return numerator / denominator;
        }

        return 0.0;
    }

    /**
     * Recursive box sliding procedure (Table 1 implementation)
     * This implements the variable N-dimensional nested loops
     */
    private void boxSlide(int[] pos, int[] llec, int[] rlec, int currentLevel,
                          int boxRadius, BoxSlideResult result) {

        // Have we reached the inner most loop?
        if (currentLevel > 0) {
            // Spawn a for loop and call the next inner most loop
            for (pos[currentLevel] = llec[currentLevel];
                 pos[currentLevel] <= rlec[currentLevel];
                 pos[currentLevel]++) {
                boxSlide(pos, llec, rlec, currentLevel - 1, boxRadius, result);
            }
        } else {
            // Inner loop - slide the box across this dimension
            for (pos[currentLevel] = llec[currentLevel];
                 pos[currentLevel] <= rlec[currentLevel];
                 pos[currentLevel]++) {

                // Calculate box mass
                double boxSum = calculateFullBoxSum(pos, boxRadius);

                // Accumulate p(i,r) and p(i,r)²
                result.sumP += boxSum;
                result.sumP2 += boxSum * boxSum;
                result.totalBoxes++;
            }
        }
    }

    /**
     * Efficient hyperplane addition/subtraction procedure
     * This is a simplified version that calculates the full box sum
     * (A full optimization would track leading/trailing edges)
     */
    private double boxPlaneAddSub(int[] pos, int boxRadius) {
        // For this implementation, we calculate the full box sum
        // The paper's full optimization tracks only edge changes
        return calculateFullBoxSum(pos, boxRadius);
    }

    /**
     * Calculates the sum of all points in a box at the given position
     */
    private double calculateFullBoxSum(int[] pos, int boxRadius) {
        int[] offset = new int[dimensions];
        // The recursive helper should return the sum
        return calculateBoxSumRecursive(pos, boxRadius, offset, dimensions - 1);
    }

    /**
     * Recursive helper to sum all grid cells within the sliding box
     */
    private double calculateBoxSumRecursive(int[] pos, int boxRadius, int[] offset,
                                            int currentDim) {
        double currentSum = 0.0; // Initialize sum for this level of recursion

        if (currentDim >= 0) {
            for (offset[currentDim] = 0; offset[currentDim] < boxRadius; offset[currentDim]++) {
                currentSum += calculateBoxSumRecursive(pos, boxRadius, offset, currentDim - 1);
            }
        } else {
            // Base case: All dimensions have been offset. Calculate grid index and add to sum.
            int gridIndex = 0;
            int multiplier = 1;
            boolean validPosition = true;

            for (int dim = 0; dim < dimensions; dim++) {
                int gridPos = pos[dim] + offset[dim];
                if (gridPos < 0 || gridPos >= s) {
                    validPosition = false;
                    break;
                }
                gridIndex += gridPos * multiplier;
                multiplier *= s;
            }

            if (validPosition && gridIndex < totalGridBoxes) {
                currentSum += baseBox[gridIndex];
            }
        }
        return currentSum; // Return the accumulated sum
    }

    /**
     * Helper class to accumulate results during box sliding
     */
    private static class BoxSlideResult {
        double sumP = 0.0;      // Sum of p(i,r)
        double sumP2 = 0.0;     // Sum of p(i,r)²
        int totalBoxes = 0;     // B(r) - total number of boxes
    }

    public double lacunaritySlope(double[] values){
        double[] log_Vr = new double[values.length];
        double[] log_r = new double[values.length];

        for(int i = 1; i <= values.length; i++){
            log_r[i - 1] = Math.log(i);
            log_Vr[i - 1] = Math.log(values[i - 1]);
        }

        return Statistics.LinearRegression(log_r, log_Vr)[0];
    }
}