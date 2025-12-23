package arkanoid.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class LineTest {

    @Test
    public void testIntersectionWithIntersectingLines() {
        // Line 1: (0,0) to (10,10)
        Line l1 = new Line(0, 0, 10, 10);
        // Line 2: (0,10) to (10,0) - Should cross at (5,5)
        Line l2 = new Line(0, 10, 10, 0);

        Point intersection = l1.intersectionWith(l2);
        assertNotNull(intersection, "Lines should intersect");
        assertEquals(5.0, intersection.getX(), 0.001, "Intersection X should be 5");
        assertEquals(5.0, intersection.getY(), 0.001, "Intersection Y should be 5");
    }

    @Test
    public void testIntersectionWithParallelLines() {
        // Line 1: (0,0) to (10,0) - Horizontal
        Line l1 = new Line(0, 0, 10, 0);
        // Line 2: (0,5) to (10,5) - Parallel Horizontal
        Line l2 = new Line(0, 5, 10, 5);

        Point intersection = l1.intersectionWith(l2);
        assertNull(intersection, "Parallel lines should not intersect");
    }
}
