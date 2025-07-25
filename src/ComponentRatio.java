import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class ComponentRatio {
    public static int bfs_fill(int[][] grid, int start_x, int start_y, boolean[][] visited) {
        int size = grid.length;
        int count = 1;

        Deque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{start_x, start_y});

        visited[start_y][start_x] = true;

        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        while(!queue.isEmpty()) {
            int[] current = queue.removeFirst();

            int x = current[0];
            int y = current[1];

            for (int i = 0; i < dx.length; i++) {
                int new_x = x + dx[i];
                int new_y = y + dy[i];
                if (new_x >= 0 && new_x < size && new_y >= 0 && new_y < size) {
                    if (!visited[new_y][new_x] && grid[new_y][new_x] == 1) {
                        queue.add(new int[]{new_x, new_y});
                        visited[new_y][new_x] = true;
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public static double largestComponentRatio(ArrayList<Point2D> points){
        int[][] grid = Helper.toMatrix(points, 256);
        int totalPoints = 0;
        int maxComponentSize = 0;
        int length = grid.length;
        boolean[][] visited = new boolean[length][length];
        for(int i = 0; i < length; i++){
            for(int j = 0; j < length; j++){
                if(grid[i][j] == 1 ){
                    totalPoints++;
                    if(!visited[i][j])
                        maxComponentSize = Math.max(maxComponentSize, bfs_fill(grid, i, j, visited));
                }
            }
        }
        return (double) maxComponentSize/totalPoints;
    }
}
