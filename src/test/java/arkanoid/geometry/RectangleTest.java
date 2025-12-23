package arkanoid.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.util.List;

public class RectangleTest {

    @Test
    public void testIntersectionPoints() {
        // Rectangle from (0,0) with width 10, height 10
        Rectangle rect = new Rectangle(new Point(0, 0), 10, 10);

        // Line passing through the rectangle: (-5, 5) to (15, 5) -- horizontal middle
        Line line = new Line(-5, 5, 15, 5);

        List<Point> intersections = rect.intersectionPoints(line);

        // Should intersect at left wall (0,5) and right wall (10,5)
        assertEquals(2, intersections.size(), "Should have 2 intersection points");

        boolean hasLeft = intersections.stream().anyMatch(p -> p.getX() == 0 && p.getY() == 5);
        boolean hasRight = intersections.stream().anyMatch(p -> p.getX() == 10 && p.getY() == 5);

        assertTrue(hasLeft, "Should contain intersection at (0,5)");
        assertTrue(hasRight, "Should contain intersection at (10,5)");
    }

    @Test
    public void testWidthAndHeight() {
        Rectangle rect = new Rectangle(new Point(10, 20), 50, 30);

        assertEquals(50.0, rect.getWidth(), 0.001, "Width should be 50");
        assertEquals(30.0, rect.getHeight(), 0.001, "Height should be 30");
        assertEquals(10.0, rect.getUpperLeft().getX(), 0.001, "X should be 10");
        assertEquals(20.0, rect.getUpperLeft().getY(), 0.001, "Y should be 20");
    }
}
