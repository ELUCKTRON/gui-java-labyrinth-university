package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BacktrackingMaze {

    private int[][] maze;
    private int width;
    private int height;
    private Random random = new Random();

    public BacktrackingMaze(int width, int height) {
        // Ensure dimensions are odd
        this.width = (width % 2 == 0) ? width - 1 : width;
        this.height = (height % 2 == 0) ? height - 1 : height;

        // Initialize maze with walls (0)
        maze = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze[i][j] = 0;
            }
        }
    }

    public int[][] generateMaze() {
        // Create random starting point
        int startRow, startCol;
        do {
            startRow = random.nextInt(height);
        } while (startRow % 2 == 0);

        do {
            startCol = random.nextInt(width);
        } while (startCol % 2 == 0);

        maze[startRow][startCol] = 1; // Starting cell as path

        // List to hold open cells
        List<int[]> openCells = new ArrayList<>();
        openCells.add(new int[]{startRow, startCol});

        while (!openCells.isEmpty()) {
            // Current cell and its neighbors
            int[] cell = openCells.get(openCells.size() - 1);
            List<int[]> neighbors = getNeighbors(cell[0], cell[1]);

            if (!neighbors.isEmpty()) {
                // Choose a random neighbor
                int[] neighbor = neighbors.get(random.nextInt(neighbors.size()));

                // Carve path between current cell and chosen neighbor
                int midRow = (cell[0] + neighbor[0]) / 2;
                int midCol = (cell[1] + neighbor[1]) / 2;

                maze[midRow][midCol] = 1; // Carve wall between
                maze[neighbor[0]][neighbor[1]] = 1; // Carve neighbor cell

                // Add neighbor to open cells
                openCells.add(neighbor);
            } else {
                // Backtrack if no neighbors
                openCells.remove(openCells.size() - 1);
            }
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

            // Check bounds and ensure neighbor is a wall
            if (newRow > 0 && newRow < height && newCol > 0 && newCol < width && maze[newRow][newCol] == 0) {
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
//        BacktrackingMaze maze = new BacktrackingMaze(15, 15);
//        maze.generateMaze();
//        maze.printMaze();
//    }
}
