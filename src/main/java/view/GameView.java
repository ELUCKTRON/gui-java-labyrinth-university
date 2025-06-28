package view;

import controller.GameController;
import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GameView extends JPanel {

    private Map map;

    private Image wallImage;
    private Image emptyNotVisibleImage;
    private Image emptyVisibleImage;
    private Image playerImage;
    private Image monsterImage;

    private String difficulty;

    private String playerName;

    private int stepCount = 0;
    public boolean gameWon = false;
    public boolean gameLost = false;

    public String getPlayerName() {
        return playerName;
    }

    public GameView(CardLayout cardLayout, JPanel parentPanel, String playerName, String difficulty) {

        this.difficulty = difficulty;
        this.playerName = playerName;

        this.setFocusable(true);
        this.requestFocusInWindow();

        map = new Map(playerName, difficulty);

        imageLoader();

        new GameController(map, this);
    }

    @Override
    public Dimension getPreferredSize() {
        return Toolkit.getDefaultToolkit().getScreenSize(); // Full screen size
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        // Dimensions of the matrix
        int rows = map.getFields().length;
        int cols = map.getFields()[0].length;

        // Calculate cell width and height to fill the screen dynamically
        int cellWidth = width / cols;
        int cellHeight = (height - 50) / rows; // Subtract status bar height

        // Start painting the board below the status bar
        paintBoard(cellWidth, cellHeight, g2d, 0, 50);
        paintItems(cellWidth, cellHeight, g2d, 0, 50);
        paintStatusBar(g2d, width);
    }

    private void paintBoard(int cellWidth, int cellHeight, Graphics2D g2d, int xOffset, int yOffset) {
        for (int i = 0; i < this.map.getFields().length; i++) {
            for (int j = 0; j < this.map.getFields()[i].length; j++) {
                Field field = this.map.getFields()[i][j];

                int x = j * cellWidth + xOffset;
                int y = i * cellHeight + yOffset;

                if (field instanceof Wall) {
                    if (field.getVisibility()) {
                        g2d.drawImage(wallImage, x, y, cellWidth, cellHeight, this);
                    } else {
                        g2d.drawImage(emptyNotVisibleImage, x, y, cellWidth, cellHeight, this);
                    }
                } else {
                    if (field.getVisibility()) {
                        g2d.drawImage(emptyVisibleImage, x, y, cellWidth, cellHeight, this);
                    } else {
                        g2d.drawImage(emptyNotVisibleImage, x, y, cellWidth, cellHeight, this);
                    }
                }
            }
        }
    }

    private void paintItems(int cellWidth, int cellHeight, Graphics2D g2d, int xOffset, int yOffset) {
        for (int i = 0; i < this.map.getFields().length; i++) {
            for (int j = 0; j < this.map.getFields()[i].length; j++) {
                Field field = this.map.getFields()[i][j];

                int x = j * cellWidth + xOffset;
                int y = i * cellHeight + yOffset;

                if (field instanceof Player) {
                    g2d.drawImage(playerImage, x, y, cellWidth, cellHeight, this);
                } else if (field instanceof Monster) {
                    if (field.getVisibility()) {
                        g2d.drawImage(monsterImage, x, y, cellWidth, cellHeight, this);
                    } else {
                        g2d.drawImage(emptyNotVisibleImage, x, y, cellWidth, cellHeight, this);
                    }
                }
            }
        }
    }

    private void paintStatusBar(Graphics2D g2d, int width) {
        int barHeight = 50;

        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, width, barHeight);

        g2d.setFont(new Font("Arial", Font.BOLD, 18));

        int padding = 20;
        int elementSpacing = width / 5;

        g2d.setColor(Color.WHITE);
        g2d.drawString("Player: " + playerName, padding, 30);
        g2d.drawString("Difficulty: " + difficulty, padding + elementSpacing, 30);
        g2d.drawString("Steps: " + stepCount, padding + 2 * elementSpacing, 30);

        String statusText;
        if (gameWon) {
            g2d.setColor(Color.GREEN);
            statusText = "Status: WIN";
        } else if (gameLost) {
            g2d.setColor(Color.RED);
            statusText = "Status: LOSE";
        } else {
            g2d.setColor(Color.CYAN);
            statusText = "Status: IN PROGRESS";
        }

        int stepsTextWidth = g2d.getFontMetrics().stringWidth("Steps: " + stepCount);
        int statusStartX = padding + 2 * elementSpacing + stepsTextWidth + 20;
        g2d.drawString(statusText, statusStartX, 30);

        g2d.setColor(new Color(255, 215, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String exitText = "EXIT ↓";
        int exitTextWidth = g2d.getFontMetrics().stringWidth(exitText);
        g2d.drawString(exitText, width - exitTextWidth - padding, 30);
    }

    public void updateSteps(int steps) {
        this.stepCount = steps;
        repaint();
    }

    private void imageLoader() {
        try {
            wallImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/wall.jpeg"));
            emptyNotVisibleImage = ImageIO
                    .read(getClass().getClassLoader().getResourceAsStream("images/notVisible.jpeg"));
            emptyVisibleImage = ImageIO
                    .read(getClass().getClassLoader().getResourceAsStream("images/visible.jpeg"));
            playerImage = ImageIO
                    .read(getClass().getClassLoader().getResourceAsStream("images/player.png"));
            monsterImage = ImageIO
                    .read(getClass().getClassLoader().getResourceAsStream("images/warewolf.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
