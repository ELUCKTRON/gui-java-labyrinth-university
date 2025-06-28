package modelTest;

import model.Map;
import model.Player;
import model.Position;
import model.Empty;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testPlayerInitialization() {
        Position startPosition = new Position(4, 0);
        Player player = new Player("TestPlayer", startPosition);

        assertEquals("TestPlayer", player.getName(), "Player name should be initialized correctly");
        assertEquals(0, player.getMoves(), "Player moves should start at 0");
        assertEquals(startPosition, player.getPosition(), "Player position should be initialized correctly");
    }

    @Test
    public void testActionUpdatesPosition() {
        // Initialize the player and map
        Map map = new Map("TestPlayer");

        // Retrieve the player's initial position
        Position startPosition = map.getPlayer().getPosition();
        Player player = map.getPlayer();

        // Move the player to a valid new position
        Position newPosition = new Position(3, 0); // Valid position
        if (map.validateMove(startPosition, newPosition)) {
            player.action(newPosition, map);
        }

        // Check if the player's position and moves are updated
        assertEquals(newPosition, player.getPosition(), "Player position should update after action");
        assertEquals(1, player.getMoves(), "Player moves should increment after action");
        assertTrue(map.getFields()[3][0] instanceof Player, "New position should contain the Player");
        assertTrue(map.getFields()[4][0] instanceof Empty, "Old position should be cleared");
    }

    @Test
    public void testActionInvalidMove() {
        // Initialize the player and map
        Map map = new Map("TestPlayer");

        // Retrieve the player's initial position
        Position startPosition = map.getPlayer().getPosition();
        Player player = map.getPlayer();

        // Attempt to move the player to an invalid position
        Position invalidPosition = new Position(4, 1); // Invalid because it's a wall
        if (map.validateMove(startPosition, invalidPosition)) {
            player.action(invalidPosition, map);
        }

        // Verify that the player's position and moves do not change
        assertEquals(startPosition, player.getPosition(), "Player position should not change for an invalid move");
        assertEquals(0, player.getMoves(), "Player moves should not increment for an invalid move");
    }
}
