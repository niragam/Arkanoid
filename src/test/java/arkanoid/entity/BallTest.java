package arkanoid.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import javafx.scene.paint.Color;
import arkanoid.geometry.Point;
import arkanoid.util.Velocity;

public class BallTest {

    @Test
    public void testBallState() {
        Point center = new Point(50, 50);
        Ball ball = new Ball(center, 5, Color.RED);

        assertEquals(50.0, ball.getCenter().getX(), 0.001);
        assertEquals(50.0, ball.getCenter().getY(), 0.001);
        assertEquals(5, ball.getRadius());
        assertEquals(Color.RED, ball.getColor());
    }
}
