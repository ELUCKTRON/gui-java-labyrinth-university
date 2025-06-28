package view;

import controller.HighScoresController;
import controller.StartGameController;
import javax.swing.*;
import java.awt.*;

public class MyMenu extends JMenuBar {
    public MyMenu(CardLayout cardLayout, JPanel parentPanel, MainMenu mainMenu) {

        JMenu file = new JMenu("File");
        JMenu newGame = new JMenu("New Game");
        JMenuItem highScores = new JMenuItem("highScores");
        JMenuItem exit = new JMenuItem("Exit");
        JMenuItem help = new JMenuItem("Help");

        highScores.addActionListener(e -> new HighScoresController(cardLayout, parentPanel).actionPerformed(e));

        exit.addActionListener(e -> System.exit(0));

        help.addActionListener(e -> {
            String gameRules = """
                    Welcome to the Labyrinth Game!
                    Your goal is to escape the labyrinth by moving from the bottom-left corner to the top-right corner.
                    Avoid the dragon, which moves randomly and kills you if it reaches a neighboring tile.
                    You can only move up, down, left, or right.
                    The labyrinth is dark, so you can only see fields within 3 units around you.
                    Solve as many labyrinths as you can and track your score.
                    View the high scores or restart the game from the menu.
                    Good luck escaping the Monster!
                    """;

            JOptionPane.showMessageDialog(null, gameRules, "Information", JOptionPane.INFORMATION_MESSAGE);
        });

        JMenuItem easy = new JMenuItem("Easy");
        JMenuItem normal = new JMenuItem("Normal");
        JMenuItem hard = new JMenuItem("Hard");

        easy.addActionListener(new StartGameController(cardLayout, parentPanel, mainMenu, "Easy"));
        normal.addActionListener(new StartGameController(cardLayout, parentPanel, mainMenu, "Normal"));
        hard.addActionListener(new StartGameController(cardLayout, parentPanel, mainMenu, "Hard"));

        newGame.add(easy);
        newGame.add(normal);
        newGame.add(hard);
        file.add(newGame);
        file.add(highScores);
        file.add(help);
        file.add(exit);

        this.add(file);
    }
}
