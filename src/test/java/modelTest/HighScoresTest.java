package modelTest;

import model.HighScores;
import model.HighScore;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class HighScoresTest {

    private final String tableName = "HIGHSCORES";

    @Test
    public void testInitializationAndTableCreation() throws SQLException {
        HighScores highScores = new HighScores(5);
        try {
            // Verify the object is created without errors
            assertNotNull(highScores, "HighScores object should be initialized successfully");
        } finally {
            highScores.close();
        }
    }

    @Test
    public void testAddAndRetrieveHighScore() throws SQLException {
        HighScores highScores = new HighScores(5);
        String testName = "TestPlayer";
        int testScore = 100;
        String testDifficulty = "Easy";

        try {
            // Reset database
            resetDatabase(highScores);

            // Add a high score
            highScores.putHighScore(testName, testScore, testDifficulty);

            // Retrieve high scores for the specified difficulty
            ArrayList<HighScore> scores = highScores.getHighScores(testDifficulty);

            // Verify the high score is added
            boolean found = scores.stream().anyMatch(score ->
                    score.getName().equals(testName) &&
                            score.getScore() == testScore &&
                            score.getDifficulty().equals(testDifficulty)
            );
            assertTrue(found, "The test high score should be present in the database");
        } finally {
            resetDatabase(highScores);
            highScores.close();
        }
    }

    @Test
    public void testEmptyDatabase() throws SQLException {
        HighScores highScores = new HighScores(5);
        try {
            // Reset database
            resetDatabase(highScores);

            // Verify retrieving from an empty database
            ArrayList<HighScore> scores = highScores.getAllHighScores();
            assertTrue(scores.isEmpty(), "The database should be empty");
        } finally {
            resetDatabase(highScores);
            highScores.close();
        }
    }

    @Test
    public void testHighScoreLimitEnforcement() throws SQLException {
        HighScores highScores = new HighScores(3); // Limit to 3 scores
        try {
            // Reset database
            resetDatabase(highScores);

            // Add 3 high scores
            highScores.putHighScore("Player1", 300, "Normal");
            highScores.putHighScore("Player2", 200, "Normal");
            highScores.putHighScore("Player3", 100, "Normal");

            // Add a 4th high score with a better score (lower is better)
            highScores.putHighScore("Player4", 50, "Normal");

            // Retrieve high scores for "Normal"
            ArrayList<HighScore> scores = highScores.getHighScores("Normal");

            // Verify only top 3 scores remain
            assertEquals(3, scores.size(), "Only the top 3 scores should remain");
            assertEquals("Player4", scores.get(0).getName(), "The best score should be at the top");
            assertEquals(50, scores.get(0).getScore(), "The best score should be at the top");
        } finally {
            resetDatabase(highScores);
            highScores.close();
        }
    }

    @Test
    public void testGetAllHighScores() throws SQLException {
        HighScores highScores = new HighScores(5);
        try {
            // Reset database
            resetDatabase(highScores);

            // Add high scores across difficulties
            highScores.putHighScore("Player1", 100, "Easy");
            highScores.putHighScore("Player2", 200, "Normal");

            // Retrieve all high scores
            ArrayList<HighScore> scores = highScores.getAllHighScores();

            // Verify entries
            assertFalse(scores.isEmpty(), "HighScores should not be empty");
            assertEquals(2, scores.size(), "There should be 2 high scores");

            // Cleanup happens via reset
        } finally {
            resetDatabase(highScores);
            highScores.close();
        }
    }

    @Test
    public void testDuplicateEntries() throws SQLException {
        HighScores highScores = new HighScores(5);
        try {
            // Reset database
            resetDatabase(highScores);

            // Add duplicate high scores
            highScores.putHighScore("Player1", 100, "Easy");
            highScores.putHighScore("Player1", 100, "Easy");

            // Retrieve high scores
            ArrayList<HighScore> scores = highScores.getHighScores("Easy");

            // Verify duplicates are stored
            assertEquals(2, scores.size(), "Duplicates should be allowed in the database");
        } finally {
            resetDatabase(highScores);
            highScores.close();
        }
    }

    @Test
    public void testDeleteHighestScore() throws SQLException {
        HighScores highScores = new HighScores(2); // Set the maxScores limit to 2 for this test
        String difficulty = "Hard";

        try {
            // Reset database
            resetDatabase(highScores);

            // Add two high scores (reaching the limit)
            highScores.putHighScore("Player1", 300, difficulty);
            highScores.putHighScore("Player2", 200, difficulty);

            // Add a third high score with a better score (lower is better)
            highScores.putHighScore("Player3", 100, difficulty);

            // Retrieve high scores
            ArrayList<HighScore> scores = highScores.getHighScores(difficulty);

            // Verify that only the top 2 scores remain
            assertEquals(2, scores.size(), "Only the top 2 scores should remain");

            // Verify that the highest score (300) was deleted
            boolean foundDeleted = scores.stream().anyMatch(score -> score.getName().equals("Player1"));
            assertFalse(foundDeleted, "The highest score should be deleted when the limit is exceeded");

            // Verify the remaining scores are correct
            assertEquals("Player3", scores.get(0).getName(), "The best score should be at the top");
            assertEquals("Player2", scores.get(1).getName(), "The second-best score should remain");
        } finally {
            resetDatabase(highScores);
            highScores.close();
        }
    }


    private void resetDatabase(HighScores highScores) throws SQLException {
        try (var statement = highScores.getConnectionForTest().createStatement()) {
            // Drop the table if it exists
            statement.executeUpdate("DROP TABLE IF EXISTS " + tableName);

            // Recreate the table
            statement.executeUpdate("""
                CREATE TABLE HIGHSCORES (
                    TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    NAME VARCHAR(100) NOT NULL,
                    SCORE INT NOT NULL,
                    DIFFICULTY VARCHAR(50) NOT NULL
                )
            """);
        }
    }
}
