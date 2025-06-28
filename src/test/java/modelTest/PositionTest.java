package modelTest;

import model.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @Test
    public void testEqualsSameObject() {
        Position position = new Position(2, 3);
        assertTrue(position.equals(position), "Position should be equal to itself");
    }

    @Test
    public void testEqualsEqualObjects() {
        Position position1 = new Position(2, 3);
        Position position2 = new Position(2, 3);
        assertTrue(position1.equals(position2), "Positions with the same coordinates should be equal");
    }

    @Test
    public void testEqualsDifferentObjects() {
        Position position1 = new Position(2, 3);
        Position position2 = new Position(3, 2);
        assertFalse(position1.equals(position2), "Positions with different coordinates should not be equal");
    }

    @Test
    public void testEqualsNullObject() {
        Position position = new Position(2, 3);
        assertFalse(position.equals(null), "Position should not be equal to null");
    }

    @Test
    public void testEqualsDifferentType() {
        Position position = new Position(2, 3);
        String other = "Not a Position";
        assertFalse(position.equals(other), "Position should not be equal to an object of a different type");
    }
}
