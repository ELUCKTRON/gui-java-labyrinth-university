package controller;

import view.GameRunner;
import view.GameView;
import view.MainMenu;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;

public class StartGameController implements ActionListener {
    private CardLayout cardLayout;
    private JPanel parentPanel;
    private MainMenu mainMenu;
    private String difficulty;

    public StartGameController(CardLayout cardLayout, JPanel parentPanel, MainMenu mainMenu, String difficulty) {
        this.cardLayout = cardLayout;
        this.parentPanel = parentPanel;
        this.mainMenu = mainMenu;
        this.difficulty = difficulty;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        String playerName = mainMenu.getPlayerName();
        String selectedDifficulty = (difficulty != null) ? difficulty : mainMenu.getSelectedDifficulty();

        if (selectedDifficulty == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "Please select a difficulty level " + playerName,
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Navigate to GameView
        GameView gameView = new GameView(cardLayout, parentPanel, playerName, selectedDifficulty);
        parentPanel.add(gameView, "gameView");
        cardLayout.show(parentPanel, "gameView");
        gameView.setFocusable(true);
        gameView.requestFocusInWindow();

        // Enable the menu bar
        Window window = SwingUtilities.getWindowAncestor(parentPanel);
        if (window instanceof GameRunner) {
            GameRunner gameRunner = (GameRunner) window;
            gameRunner.getMyMenu().getMenu(0).setEnabled(true); // Enable the first menu (File menu)
        } else {
            System.err.println("Ancestor is not an instance of GameRunner.");
        }
    }
}
