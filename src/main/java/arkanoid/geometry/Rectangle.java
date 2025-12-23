package arkanoid.geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Axis-aligned rectangle represented by its upper-left point, width and height.
 */
public class Rectangle {

    private final Point upperLeft;
    private final double width;
    private final double height;

    /**
     * Create a new rectangle with upper-left point and width/height.
     *
     * @param upperLeft upper left corner
     * @param width     width
     * @param height    height
     */
    public Rectangle(Point upperLeft, double width, double height) {
        this.upperLeft = upperLeft;
        this.width = width;
        this.height = height;
    }

    /**
     * Return a (possibly empty) list of intersection points with a line.
     * Duplicate points (e.g., at corners) are removed.
     *
     * @param line line to intersect with
     * @return list of unique intersection points
     */
    public List<Point> intersectionPoints(Line line) {
        List<Point> points = new ArrayList<>();

        double x = upperLeft.getX();
        double y = upperLeft.getY();
        double xRight = x + width;
        double yBottom = y + height;

        Line top = new Line(x, y, xRight, y);
        Line bottom = new Line(x, yBottom, xRight, yBottom);
        Line left = new Line(x, y, x, yBottom);
        Line right = new Line(xRight, y, xRight, yBottom);

        addIfUnique(points, line.intersectionWith(top));
        addIfUnique(points, line.intersectionWith(bottom));
        addIfUnique(points, line.intersectionWith(left));
        addIfUnique(points, line.intersectionWith(right));

        return points;
    }

    /**
     * Add a point to the list if it's not null and not already present.
     *
     * @param points list of points
     * @param point  point to add
     */
    private void addIfUnique(List<Point> points, Point point) {
        if (point != null && !points.contains(point)) {
            points.add(point);
        }
    }

    /**
     * Return the width of the rectangle.
     *
     * @return width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Return the height of the rectangle.
     *
     * @return height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Return the upper-left point of the rectangle.
     *
     * @return upper-left point
     */
    public Point getUpperLeft() {
        return upperLeft;
    }

    /**
     * Get the center point of the rectangle.
     *
     * @return center point
     */
    public Point getCenter() {
        return new Point(
                upperLeft.getX() + width / 2,
                upperLeft.getY() + height / 2);
    }

    /**
     * Check if this rectangle intersects with another rectangle.
     *
     * @param other other rectangle
     * @return true if intersects
     */
    public boolean intersects(Rectangle other) {
        double ax = this.upperLeft.getX();
        double ay = this.upperLeft.getY();
        double bx = other.upperLeft.getX();
        double by = other.upperLeft.getY();

        return ax < bx + other.width &&
                ax + this.width > bx &&
                ay < by + other.height &&
                ay + this.height > by;
    }
}
