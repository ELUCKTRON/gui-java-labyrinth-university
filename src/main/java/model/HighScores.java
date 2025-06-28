package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class HighScores {

    private final int maxScores;
    private final Connection connection;

    public Connection getConnectionForTest() {
        return connection;
    }

    public HighScores(int maxScores) throws SQLException {
        this.maxScores = maxScores;
        String dbURL = "jdbc:sqlite:labyrinth.db";
        connection = DriverManager.getConnection(dbURL);
        ensureTableExists();
    }

    public ArrayList<HighScore> getAllHighScores() throws SQLException {
        String query = "SELECT * FROM HIGHSCORES ORDER BY SCORE ASC";
        ArrayList<HighScore> highScores = new ArrayList<>();
        try (PreparedStatement selectStatement = connection.prepareStatement(query)) {
            try (ResultSet results = selectStatement.executeQuery()) {
                while (results.next()) {

                    String name = results.getString("NAME");
                    int score = results.getInt("SCORE");
                    String difficulty = results.getString("DIFFICULTY");

                    highScores.add(new HighScore(name, score, difficulty));
                }
            }
        }
        return highScores;
    }

    public ArrayList<HighScore> getHighScores(String difficulty) throws SQLException {
        String query = "SELECT * FROM HIGHSCORES WHERE DIFFICULTY=? ORDER BY SCORE ASC";
        ArrayList<HighScore> highScores = new ArrayList<>();

        try (PreparedStatement selectStatement = connection.prepareStatement(query)) {
            selectStatement.setString(1, difficulty);

            try (ResultSet results = selectStatement.executeQuery()) {
                while (results.next()) {
                    String name = results.getString("NAME");
                    int score = results.getInt("SCORE");
                    highScores.add(new HighScore(name, score, difficulty));
                }
            }
        }
        return highScores;
    }

    public void putHighScore(String name, int score, String difficulty) throws SQLException {
        ArrayList<HighScore> highScores = getHighScores(difficulty);

        if (highScores.size() < maxScores) {
            insertScore(name, score, difficulty);
        } else {
            int highestScore = highScores.get(highScores.size() - 1).getScore();

            if (score < highestScore) {
                deleteHighestScore(highestScore, difficulty);
                insertScore(name, score, difficulty);
            }
        }
    }

    private void insertScore(String name, int score, String difficulty) throws SQLException {
        String insertQuery = "INSERT INTO HIGHSCORES (TIMESTAMP, NAME, SCORE, DIFFICULTY) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            insertStatement.setString(1, ts.toString());
            insertStatement.setString(2, name);
            insertStatement.setInt(3, score);
            insertStatement.setString(4, difficulty);
            insertStatement.executeUpdate();
        }
    }

    private void deleteHighestScore(int score, String difficulty) throws SQLException {
        String deleteQuery = "DELETE FROM HIGHSCORES WHERE SCORE=? AND DIFFICULTY=?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setInt(1, score);
            deleteStatement.setString(2, difficulty);
            deleteStatement.executeUpdate();
        }
    }

    private void ensureTableExists() throws SQLException {
        String createTableQuery = """
                    CREATE TABLE IF NOT EXISTS HIGHSCORES (
                        TIMESTAMP TEXT DEFAULT (datetime('now')),
                        NAME TEXT NOT NULL,
                        SCORE INTEGER NOT NULL,
                        DIFFICULTY TEXT NOT NULL
                    )
                """;

        try (PreparedStatement createTableStatement = connection.prepareStatement(createTableQuery)) {
            createTableStatement.execute();
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
