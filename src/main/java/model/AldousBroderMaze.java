package model;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AldousBroderMaze {

    private int[][] maze;
    private int width;
    private int height;
    private Random random = new Random();

    public AldousBroderMaze(int width, int height) {
        // Ensure dimensions are odd
        this.width = (width % 2 == 0) ? width - 1 : width;
        this.height = (height % 2 == 0) ? height - 1 : height;

        // Initialize maze with all walls (0)
        maze = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze[i][j] = 0; // Set all cells as walls
            }
        }
    }

    public int[][] generateMaze() {
        int unvisited = 0;

        // Count unvisited cells (odd indices only)
        for (int i = 1; i < height; i += 2) {
            for (int j = 1; j < width; j += 2) {
                unvisited++;
            }
        }

        // Start at a random odd cell
        int row, col;
        do {
            row = random.nextInt(height);
            col = random.nextInt(width);
        } while (row % 2 == 0 || col % 2 == 0);

        maze[row][col] = 1; // Mark the starting cell as a path
        unvisited--;

        // Perform Aldous-Broder algorithm
        while (unvisited > 0) {
            // Get neighbors
            List<int[]> neighbors = getNeighbors(row, col);

            // Choose a random neighbor
            int[] next = neighbors.get(random.nextInt(neighbors.size()));
            int nextRow = next[0];
            int nextCol = next[1];

            // If the neighbor is unvisited
            if (maze[nextRow][nextCol] == 0) {
                // Remove the wall between the current cell and the neighbor
                maze[(row + nextRow) / 2][(col + nextCol) / 2] = 1;

                // Mark the neighbor as visited
                maze[nextRow][nextCol] = 1;
                unvisited--;
            }

            // Move to the chosen neighbor
            row = nextRow;
            col = nextCol;
        }

        // Create entrance and exit
        maze[0][width - 1] = 1; // Top
        maze[1][width - 1] = 1; // Top
        maze[height - 1][0] = 1; // Bottom
        maze[height - 1][1] = 1; // Bottom

        return maze;
    }

    private List<int[]> getNeighbors(int row, int col) {
        List<int[]> neighbors = new ArrayList<>();
        int[][] directions = {
                {-2, 0}, {2, 0}, {0, -2}, {0, 2} // Up, Down, Left, Right
        };

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            // Check if the neighbor is within bounds
            if (newRow > 0 && newRow < height && newCol > 0 && newCol < width) {
                neighbors.add(new int[]{newRow, newCol});
            }
        }

        return neighbors;
    }

//    public void printMaze() {
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                System.out.print(maze[i][j] == 1 ? " " : "█");
//            }
//            System.out.println();
//        }
//    }
//
//    public static void main(String[] args) {
//        AldousBroderMaze maze = new AldousBroderMaze(15, 15);
//        maze.generateMaze();
//        maze.printMaze();
//    }
}
