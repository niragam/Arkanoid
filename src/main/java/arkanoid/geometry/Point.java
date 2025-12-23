package arkanoid.geometry;

/**
 * A point in the 2D plane.
 */
public class Point {

    private final double x;
    private final double y;

    /**
     * Create a point given x and y coordinates.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Return the distance between this point and another point.
     *
     * @param other the other point
     * @return the distance between this point and the other point
     */
    public double distance(Point other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Return true if the points are equal, false otherwise.
     *
     * @param other the other point
     * @return true if the points are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Point point = (Point) other;
        return Double.compare(this.x, point.x) == 0
                && Double.compare(this.y, point.y) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }

    /**
     * Return the x value of this point.
     *
     * @return x value
     */
    public double getX() {
        return this.x;
    }

    /**
     * Return the y value of this point.
     *
     * @return y value
     */
    public double getY() {
        return this.y;
    }
}
