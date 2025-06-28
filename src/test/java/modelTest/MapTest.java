package modelTest;

import model.Map;
import model.Player;
import model.Monster;
import model.Position;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapTest {

    @Test
    public void testMapInitialization() {
        Map map = new Map("TestPlayer");

        // Check if the player is correctly initialized
        Player player = map.getPlayer();
        Position startPosition = new Position(4, 0);
        assertNotNull(player, "Player should be initialized");
        assertEquals("TestPlayer", player.getName(), "Player name should match");
        assertEquals(startPosition, player.getPosition(), "Player should start at the correct position");

        // Check if the monster is correctly placed
        Monster monster = map.getMonster();
        assertNotNull(monster, "Monster should be initialized");
        Position monsterPosition = monster.getPosition();
        assertTrue(map.getFields()[monsterPosition.getI()][monsterPosition.getJ()] instanceof Monster,
                "Monster should be placed in the correct position");

        // Check if the start and end positions are set
        assertEquals(startPosition, map.getFields()[4][0].getPosition(), "Start position should match");
        assertEquals(new Position(0, 4), map.getFields()[0][4].getPosition(), "End position should match");
    }

    @Test
    public void testValidateMoveValid() {
        Map map = new Map("TestPlayer");

        Position currentPosition = new Position(4, 0);
        Position newPosition = new Position(3, 0); // Valid move
        assertTrue(map.validateMove(currentPosition, newPosition), "The move should be valid");
    }

    @Test
    public void testValidateMoveInvalid() {
        Map map = new Map("TestPlayer");

        Position currentPosition = new Position(4, 0);
        Position invalidPosition = new Position(4, 1); // Wall
        assertFalse(map.validateMove(currentPosition, invalidPosition),
                "The move should be invalid (blocked by a wall)");

        Position outOfBoundsPosition = new Position(-1, 0); // Out of bounds
        assertFalse(map.validateMove(currentPosition, outOfBoundsPosition),
                "The move should be invalid (out of bounds)");

        Position diagonalPosition = new Position(3, 1); // Diagonal move
        assertFalse(map.validateMove(currentPosition, diagonalPosition),
                "The move should be invalid (diagonal moves not allowed)");
    }

    @Test
    public void testIsWon() {
        Map map = new Map("TestPlayer");

        // Move player to the end position
        Player player = map.getPlayer();
        Position endPosition = new Position(0, 4);
        player.action(endPosition, map);

        assertTrue(map.isWon(), "The player should win if they reach the end position");
    }

    @Test
    public void testIsLost() {
        Map map = new Map("TestPlayer");

        // Place the monster near the player
        Monster monster = map.getMonster();
        Player player = map.getPlayer();
        player.action(new Position(3, 3), map); // Move the player

        // Place the monster next to the player
        Position monsterPosition = new Position(3, 4);
        monster.action(map);
        monster.setPositionForTest(monsterPosition);
        map.getFields()[monsterPosition.getI()][monsterPosition.getJ()] = monster;

        assertTrue(map.isLost(), "The player should lose if the monster is adjacent");
    }

}
