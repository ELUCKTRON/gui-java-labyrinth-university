package view;

import model.HighScore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class HighScoreView extends JPanel {

    public HighScoreView(ArrayList<HighScore> highScores) {
        // Set the layout to BorderLayout for dynamic resizing
        setLayout(new BorderLayout());

        // Create column names for the table
        String[] columnNames = {"Name", "Score", "Difficulty"};

        // Populate table data from the highScores ArrayList
        Object[][] data = new Object[highScores.size()][3];
        for (int i = 0; i < highScores.size(); i++) {
            HighScore highScore = highScores.get(i);
            data[i][0] = highScore.getName();
            data[i][1] = highScore.getScore();
            data[i][2] = highScore.getDifficulty();
        }

        // Create a table model and JTable
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(tableModel);

        // Add the JTable to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the scroll pane to the center of the BorderLayout
        add(scrollPane, BorderLayout.CENTER);
    }

}