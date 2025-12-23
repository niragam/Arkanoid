package arkanoid.geometry;

/**
 * A line segment in the 2D plane, defined by two points: start and end.
 */
public class Line {

    private final Point start;
    private final Point end;

    private static final double EPSILON = 1e-10;

    /**
     * Create a line segment from two points.
     *
     * @param start start point
     * @param end   end point
     */
    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Create a line segment from raw coordinates.
     *
     * @param x1 x coordinate of the start point
     * @param y1 y coordinate of the start point
     * @param x2 x coordinate of the end point
     * @param y2 y coordinate of the end point
     */
    public Line(double x1, double y1, double x2, double y2) {
        this(new Point(x1, y1), new Point(x2, y2));
    }

    /**
     * Return the start point of this line segment.
     *
     * @return the start point of this line segment
     */
    public Point start() {
        return this.start;
    }

    /**
     * Return the end point of this line segment.
     *
     * @return the end point of this line segment
     */
    public Point end() {
        return this.end;
    }

    /**
     * Return the closest intersection point to the start of this line with a
     * rectangle.
     *
     * @param rect the rectangle to check against
     * @return the closest intersection point or null if none exists
     */
    public Point closestIntersectionToStartOfLine(Rectangle rect) {
        java.util.List<Point> intersections = rect.intersectionPoints(this);
        if (intersections.isEmpty()) {
            return null;
        }
        Point closest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Point p : intersections) {
            double d = this.start.distance(p);
            if (d < minDistance) {
                minDistance = d;
                closest = p;
            }
        }
        return closest;
    }

    /**
     * Compute the intersection point of this line segment with another segment.
     *
     * @param other the other line segment
     * @return the intersection point, or null if there is no unique intersection
     */
    public Point intersectionWith(Line other) {
        if (other == null) {
            return null;
        }

        Point p = this.start;
        Point p2 = this.end;
        Point q = other.start;
        Point q2 = other.end;

        boolean thisIsPoint = this.isPoint();
        boolean otherIsPoint = other.isPoint();

        if (thisIsPoint && otherIsPoint) {
            if (p.equals(q)) {
                return new Point(p.getX(), p.getY());
            }
            return null;
        }

        if (thisIsPoint) {
            if (pointOnSegment(p, q, q2)) {
                return new Point(p.getX(), p.getY());
            }
            return null;
        }

        if (otherIsPoint) {
            if (pointOnSegment(q, p, p2)) {
                return new Point(q.getX(), q.getY());
            }
            return null;
        }

        double x1 = p.getX();
        double y1 = p.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();
        double x3 = q.getX();
        double y3 = q.getY();
        double x4 = q2.getX();
        double y4 = q2.getY();

        double rX = x2 - x1;
        double rY = y2 - y1;
        double sX = x4 - x3;
        double sY = y4 - y3;

        double rCrossS = cross(rX, rY, sX, sY);
        double qMinusPCrossR = cross(x3 - x1, y3 - y1, rX, rY);

        if (isZero(rCrossS) && isZero(qMinusPCrossR)) {
            double t0;
            double t1;

            if (Math.abs(rX) >= Math.abs(rY)) {
                t0 = (x3 - x1) / rX;
                t1 = (x4 - x1) / rX;
            } else {
                t0 = (y3 - y1) / rY;
                t1 = (y4 - y1) / rY;
            }

            double tMin = Math.min(t0, t1);
            double tMax = Math.max(t0, t1);

            double tStart = Math.max(0.0, tMin);
            double tEnd = Math.min(1.0, tMax);

            if (tStart > tEnd + EPSILON) {
                return null;
            } else if (approximatelyEqual(tStart, tEnd)) {
                double ix = x1 + tStart * rX;
                double iy = y1 + tStart * rY;
                return new Point(ix, iy);
            } else {
                return null;
            }
        }

        if (isZero(rCrossS) && !isZero(qMinusPCrossR)) {
            return null;
        }

        double t = cross(x3 - x1, y3 - y1, sX, sY) / rCrossS;
        double u = cross(x3 - x1, y3 - y1, rX, rY) / rCrossS;

        if (t < -EPSILON || t > 1.0 + EPSILON || u < -EPSILON || u > 1.0 + EPSILON) {
            return null;
        }

        double ix = x1 + t * rX;
        double iy = y1 + t * rY;

        return new Point(ix, iy);
    }

    /**
     * Return true if the lines are equal.
     *
     * @param other the other line
     * @return true if the segments are equal
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Line otherLine = (Line) other;
        boolean sameDirection = this.start.equals(otherLine.start) && this.end.equals(otherLine.end);
        boolean oppositeDirection = this.start.equals(otherLine.end) && this.end.equals(otherLine.start);

        return sameDirection || oppositeDirection;
    }

    @Override
    public int hashCode() {
        // Order independent hash code since start/end can be swapped
        return java.util.Objects.hash(start.hashCode() + end.hashCode());
    }

    private boolean isPoint() {
        return this.start.equals(this.end);
    }

    private static double cross(double ax, double ay, double bx, double by) {
        return ax * by - ay * bx;
    }

    private static boolean isZero(double value) {
        return Math.abs(value) <= EPSILON;
    }

    private static boolean approximatelyEqual(double a, double b) {
        return Math.abs(a - b) <= EPSILON;
    }

    private static boolean pointOnSegment(Point p, Point a, Point b) {
        double x = p.getX();
        double y = p.getY();
        double x1 = a.getX();
        double y1 = a.getY();
        double x2 = b.getX();
        double y2 = b.getY();

        double crossVal = cross(x2 - x1, y2 - y1, x - x1, y - y1);
        if (!isZero(crossVal)) {
            return false;
        }

        double minX = Math.min(x1, x2) - EPSILON;
        double maxX = Math.max(x1, x2) + EPSILON;
        double minY = Math.min(y1, y2) - EPSILON;
        double maxY = Math.max(y1, y2) + EPSILON;

        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }
}
