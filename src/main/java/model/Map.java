package model;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;

public class Map {

    private Field[][] fields;
    private Player player;
    private Monster monster;
    private String difficulty;

    private String result;
    private boolean isEnd;

    private Position startPosition;
    private Position endPosition;

    private final int radius = 3;

    public String getResult() {
        return result;
    }

    public Field[][] getFields() {
        return fields;
    }

    public Player getPlayer() {
        return player;
    }

    public Monster getMonster() {
        return monster;
    }

    public Map(String playerName, String difficulty) {
        this.result = "IN PROGRESS";
        this.difficulty = difficulty;

        int matrixSize = switch (difficulty) {
            case "Easy" -> 5;
            case "Normal" -> 7;
            case "Hard" -> 11;
            default -> 0;
        };

        int[][] matrix;

        Random random = new Random();

        int randomNumber = random.nextInt(100);
        if (randomNumber < 30) {
            // 30% chance for BacktrackingMaze
            matrix = new BacktrackingMaze(matrixSize, matrixSize).generateMaze();
        } else {
            // 70% chance for AldousBroderMaze
            matrix = new AldousBroderMaze(matrixSize, matrixSize).generateMaze();
        }

        startPosition = new Position(matrix.length - 1, 0);
        endPosition = new Position(0, matrix.length - 1);

        this.fields = new Field[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            this.fields[i] = new Field[matrix[i].length];
            for (int j = 0; j < matrix[i].length; j++) {
                int type = matrix[i][j];
                switch (type) {
                    case 0:
                        this.fields[i][j] = new Wall(new Position(i, j));
                        break;

                    case 1:
                        this.fields[i][j] = new Empty(new Position(i, j));
                        break;
                }
            }
        }

        player = new Player(playerName, startPosition);
        this.fields[startPosition.getI()][startPosition.getJ()] = player;

        random = new Random();
        int randX = random.nextInt(matrix.length - 1);
        int randY = random.nextInt(matrix.length - 1);

        while (this.fields[randX][randY] instanceof Wall ||
                this.fields[randX][randY] instanceof Player || isPositionNearPlayer(randX, randY)) {
            randX = random.nextInt(matrix.length - 1);
            randY = random.nextInt(matrix.length - 1);
        }

        monster = new Monster(this.fields[randX][randY].getPosition());
        this.fields[randX][randY] = monster;

        changeVisibility();

    }

    //////////////// test constructor for the test cases
    public Map(String playerName) {
        this.result = "IN PROGRESS";
        this.difficulty = "Easy";

        // Predefined test matrix
        int[][] matrix = {
                { 1, 1, 0, 0, 1 },
                { 0, 1, 1, 1, 1 },
                { 0, 0, 1, 0, 0 },
                { 1, 1, 1, 0, 0 },
                { 1, 0, 0, 0, 0 }
        };

        // Initialize start and end positions
        startPosition = new Position(matrix.length - 1, 0); // Bottom-left corner
        endPosition = new Position(0, matrix[0].length - 1); // Top-right corner

        // Initialize the field grid
        this.fields = new Field[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            this.fields[i] = new Field[matrix[i].length];
            for (int j = 0; j < matrix[i].length; j++) {
                int type = matrix[i][j];
                switch (type) {
                    case 0:
                        this.fields[i][j] = new Wall(new Position(i, j)); // Wall
                        break;
                    case 1:
                        this.fields[i][j] = new Empty(new Position(i, j)); // Empty space
                        break;
                }
            }
        }

        // Add the player to the starting position
        player = new Player(playerName, startPosition);
        this.fields[startPosition.getI()][startPosition.getJ()] = player;

        // Place the monster at a fixed position (0, 2)
        Position monsterPosition = new Position(0, 2);
        monster = new Monster(monsterPosition);
        this.fields[monsterPosition.getI()][monsterPosition.getJ()] = monster;

        // Adjust visibility for the initial state
        changeVisibility();
    }

    public boolean validateMove(Position oldPosition, Position newPosition) {
        // Get the matrix dimensions
        int rows = fields.length;
        int cols = fields[0].length;

        // Check if the new position is within the bounds of the matrix
        if (newPosition.getI() < 0 || newPosition.getI() >= rows ||
                newPosition.getJ() < 0 || newPosition.getJ() >= cols) {
            return false; // Out of bounds
        }

        // Calculate the absolute difference between old and new positions
        int rowDiff = Math.abs(newPosition.getI() - oldPosition.getI());
        int colDiff = Math.abs(newPosition.getJ() - oldPosition.getJ());

        // Ensure the move is only horizontal or vertical
        if ((rowDiff + colDiff) != 1) {
            return false;
        }

        // Check if the new position is not a wall
        Field fieldAtNewPosition = fields[newPosition.getI()][newPosition.getJ()];
        if (fieldAtNewPosition instanceof Wall) {
            return false; // Invalid move (blocked by wall)
        }

        return true;
    }

    public void changeVisibility() {
        int radiusSquared = radius * radius;

        // Iterate over all fields
        for (int i = 0; i < this.getFields().length; i++) {
            for (int j = 0; j < this.getFields()[i].length; j++) {
                Field field = this.getFields()[i][j];

                int distanceI = i - this.getPlayer().getPosition().getI();
                int distanceJ = j - this.getPlayer().getPosition().getJ();
                int distanceSquared = distanceI * distanceI + distanceJ * distanceJ;

                // If within radius
                if (distanceSquared <= radiusSquared) {
                    if (field instanceof Wall && isClosestWall(this.getPlayer().getPosition(), field.getPosition())) {
                        field.setVisibility(true);
                    } else if (isPathClear(this.getPlayer().getPosition(), field.getPosition())) {
                        field.setVisibility(true);
                    } else {
                        field.setVisibility(false);
                    }
                } else {
                    field.setVisibility(false);
                }
            }
        }
    }

    public boolean isClosestWall(Position playerPos, Position wallPos) {
        int x0 = playerPos.getI();
        int y0 = playerPos.getJ();
        int x1 = wallPos.getI();
        int y1 = wallPos.getJ();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;

        int err = dx - dy;

        while (true) {
            // Stop if we reach the wall position
            if (x0 == x1 && y0 == y1) {
                return true; // This is the closest wall
            }

            // If we encounter a wall before reaching this one, it's not the closest
            if (fields[x0][y0] instanceof Wall) {
                return false;
            }

            int e2 = 2 * err;

            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }

            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }

    public boolean isPathClear(Position start, Position end) {
        int x0 = start.getI();
        int y0 = start.getJ();
        int x1 = end.getI();
        int y1 = end.getJ();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;

        int err = dx - dy;

        while (true) {
            // If the current position is a wall, the path is blocked
            if (fields[x0][y0] instanceof Wall) {
                return false;
            }

            // If we reached the end position, the path is clear
            if (x0 == x1 && y0 == y1) {
                break;
            }

            int e2 = 2 * err;

            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }

            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }

        return true;
    }

    public boolean playRound(Position newPosition) {
        player.action(newPosition, this);
        monster.action(this);

        if (isWon()) {
            System.out.println("Congratulations! You escaped the labyrinth!");
            result = "WIN";
            isEnd = true;

            try {
                HighScores highScores = new HighScores(10);
                highScores.putHighScore(player.getName(), player.getMoves(), difficulty);
                highScores.close();
            } catch (SQLException e) {
                System.out.println("cant start data base ");
                System.out.println(Arrays.toString(e.getStackTrace()));
            }

        } else if (isLost()) {
            System.out.println("Game Over! The monster got you!");
            result = "LOSE";
            isEnd = true;
        }

        return isEnd;
    }

    public boolean isWon() {
        return player.position.equals(endPosition);
    }

    public boolean isLost() {
        Position playerPosition = player.getPosition();
        Position monsterPosition = monster.getPosition();

        int distanceI = Math.abs(playerPosition.getI() - monsterPosition.getI());
        int distanceJ = Math.abs(playerPosition.getJ() - monsterPosition.getJ());

        return (distanceI == 0 && distanceJ == 0) || (distanceI == 1 && distanceJ == 0)
                || (distanceI == 0 && distanceJ == 1);
    }

    private boolean isPositionNearPlayer(int x, int y) {
        Position playerPosition = player.getPosition();

        int distanceI = Math.abs(playerPosition.getI() - x);
        int distanceJ = Math.abs(playerPosition.getJ() - y);

        return (distanceI <= 1 && distanceJ <= 1);
    }

    // set true for all field visibility
    public void letThereBeLight() {
        for (int i = 0; i < this.getFields().length; i++) {
            for (int j = 0; j < this.getFields()[i].length; j++) {
                Field field = this.getFields()[i][j];
                field.setVisibility(true);
            }
        }
    }

}
