package arkanoid.util;

/**
 * Velocity specifies the change in position on the x and y axes.
 */
public class Velocity {

    private final double dx;
    private final double dy;

    /**
     * Create a velocity from dx, dy.
     *
     * @param dx change in x
     * @param dy change in y
     */
    public Velocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Create a velocity from an angle (degrees) and speed. Angle 0 is up.
     *
     * @param angle angle in degrees, 0 is up, positive clockwise
     * @param speed speed (magnitude of velocity)
     * @return velocity instance
     */
    public static Velocity fromAngleAndSpeed(double angle, double speed) {
        double radians = Math.toRadians(angle);
        double dx = speed * Math.sin(radians);
        double dy = -speed * Math.cos(radians);
        return new Velocity(dx, dy);
    }

    /**
     * Get the dx component.
     *
     * @return dx
     */
    public double getDx() {
        return dx;
    }

    /**
     * Get the dy component.
     *
     * @return dy
     */
    public double getDy() {
        return dy;
    }

}
