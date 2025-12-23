package arkanoid.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import arkanoid.geometry.Point;

public class VelocityTest {

    @Test
    public void testFromAngleAndSpeed() {
        // Angle 0 is straight up. dx=0, dy=-speed
        double speed = 10.0;
        Velocity v = Velocity.fromAngleAndSpeed(0, speed);
        assertNotNull(v, "Velocity should not be null");
        assertEquals(0.0, v.getDx(), 0.001, "dx should be 0 for angle 0");
        assertEquals(-10.0, v.getDy(), 0.001, "dy should be -speed for angle 0");

        // Angle 90 is right. dx=speed, dy=0
        v = Velocity.fromAngleAndSpeed(90, speed);
        assertEquals(10.0, v.getDx(), 0.001, "dx should be speed for angle 90");
        assertEquals(0.0, v.getDy(), 0.001, "dy should be 0 for angle 90");
    }
}
