package modelTest;


import model.Map;
import model.Monster;
import model.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MonsterTest {

    @Test
    public void testMonsterInitialization() {
        Position monsterPosition = new Position(0, 2); // Fixed monster position
        Monster monster = new Monster(monsterPosition);

        assertEquals(monsterPosition, monster.getPosition(), "Monster position should be initialized correctly");
    }

    @Test
    public void testRandomMoveWithinBounds() {
        Monster monster = new Monster(new Position(2, 2)); // Center of the grid
        Position randomMove = monster.randomMove();

        // Ensure the move is one step in any direction
        int rowDiff = Math.abs(randomMove.getI() - monster.getPosition().getI());
        int colDiff = Math.abs(randomMove.getJ() - monster.getPosition().getJ());
        assertTrue((rowDiff + colDiff) == 1, "Monster should only move one step in any direction");
    }

    @Test
    public void testMonsterActionValidMove() {
        Map map = new Map("TestPlayer"); // Using the predefined test map
        Monster monster = map.getMonster(); // Retrieve the monster from the map
        Position initialPosition = monster.getPosition();

        // Perform the action
        monster.action(map);

        // Validate that the monster has moved to a valid position
        Position newPosition = monster.getPosition();
        assertNotEquals(initialPosition, newPosition, "Monster should move to a new position");
        assertTrue(map.validateMove(initialPosition, newPosition), "Monster's move should be valid");
        assertTrue(map.getFields()[newPosition.getI()][newPosition.getJ()] instanceof Monster, "New position should contain the Monster");
        assertTrue(map.getFields()[initialPosition.getI()][initialPosition.getJ()] instanceof model.Empty, "Old position should now be empty");
    }
}
