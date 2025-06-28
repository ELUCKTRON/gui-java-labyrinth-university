package controller;

import model.HighScore;
import model.HighScores;
import view.GameRunner;
import view.HighScoreView;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class HighScoresController implements ActionListener {

    private final CardLayout cardLayout;
    private final JPanel parentPanel;

    public HighScoresController(CardLayout cardLayout, JPanel parentPanel) {
        this.cardLayout = cardLayout;
        this.parentPanel = parentPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            HighScores highScoresModel = new HighScores(10);
            ArrayList<HighScore> highScoreList = highScoresModel.getAllHighScores();
            highScoresModel.close();

            HighScoreView highScoreView = new HighScoreView(highScoreList);
            parentPanel.add(highScoreView, "highScoreView");
            cardLayout.show(parentPanel, "highScoreView");

            // Enable the menu bar
            Window window = SwingUtilities.getWindowAncestor(parentPanel);
            if (window instanceof GameRunner) {
                GameRunner gameRunner = (GameRunner) window;
                gameRunner.getMyMenu().getMenu(0).setEnabled(true); // Enable the first menu (File menu)
            } else {
                System.err.println("Ancestor is not an instance of GameRunner.");
            }

        } catch (SQLException ex) {
            System.err.println("Failed to load high scores from database.");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to load high scores. Please try again later.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
