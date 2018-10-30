package com.dumbpug.nbp.point;

import com.dumbpug.nbp.BoxEdge;

/**
 * A point of intersection between a kinematic and static physics box.
 */
public class IntersectionPoint extends Point {
    /**
     * The edge at which a kinematic box enters a static box.
     */
    private BoxEdge intersectionEdge;

    /**
     * Create a new instance of the IntersectionPoint class.
     * @param x                The X position of the point.
     * @param y                The Y position of the point.
     * @param intersectionEdge The edge of intersection.
     */
    public IntersectionPoint(float x, float y, BoxEdge intersectionEdge) {
        super(x, y);
        this.intersectionEdge = intersectionEdge;
    }

    /**
     * Create a new instance of the IntersectionPoint class.
     * @param point            The point of intersection.
     * @param intersectionEdge The edge of intersection.
     */
    public IntersectionPoint(Point point, BoxEdge intersectionEdge) {
        this(point.getX(), point.getY(), intersectionEdge);
    }

    /**
     * Get the edge of intersection.
     * @return The edge of intersection.
     */
    public BoxEdge getIntersectionEdge() {
        return this.intersectionEdge;
    }
}
