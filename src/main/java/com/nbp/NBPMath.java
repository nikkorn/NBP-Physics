package com.nbp;

import com.nbp.point.IntersectionPoint;
import com.nbp.point.Point;

/**
 * Helper class for basic box collision detection and resolution.
 */
public class NBPMath {

    /**
     * Calculates whether two boxes intersect.
     * @param firstBox  The first box.
     * @param secondBox The second box.
     * @return Whether an intersection exists.
     */
    public static boolean doBoxesCollide(Box firstBox, Box secondBox) {
        return doSquaresIntersect(firstBox.getX(), firstBox.getY(), firstBox.getWidth(), firstBox.getHeight(), secondBox.getX(), secondBox.getY(), secondBox.getWidth(), secondBox.getHeight());
    }

    /**
     * Calculates whether a sensor and box intersect.
     * @param sensor The sensor.
     * @param box    The box.
     * @return Whether an intersection exists.
     */
    public static boolean doesSensorCollideWithBox(Sensor sensor, Box box) {
        return doSquaresIntersect(sensor.getX(), sensor.getY(), sensor.getWidth(), sensor.getHeight(), box.getX(), box.getY(), box.getWidth(), box.getHeight());
    }

    /**
     * Calculates whether two squares intersect.
     * @param aX      The X position of the first box.
     * @param aY      The Y position of the first box.
     * @param aWidth  The width of the first box.
     * @param aHeight The height of the first box.
     * @param bX      The X position of the second box.
     * @param bY      The Y position of the second box.
     * @param bWidth  The width of the second box.
     * @param bHeight The height of the second box.
     * @return Whether an intersection exists.
     */
    public static boolean doSquaresIntersect(float aX, float aY, float aWidth, float aHeight,
                                             float bX, float bY, float bWidth, float bHeight) {
        return (aX < (bX + bWidth) && (aX + aWidth) > bX && aY < (bY + bHeight) && (aY + aHeight) > bY);
    }

    /**
     * Handles a collision between two boxes on an axis.
     * @param firstBox  The first box.
     * @param secondBox The second box.
     * @param axis      The axis on which to handle this collision.
     */
    public static void handleCollision(Box firstBox, Box secondBox, CollisionAxis axis) {
        // Are we dealing with a Kinetic/Static collision or a Kinetic/Kinetic one?
        if ((firstBox.getType() == BoxType.KINETIC && secondBox.getType() == BoxType.STATIC) ||
                (secondBox.getType() == BoxType.KINETIC && firstBox.getType() == BoxType.STATIC)) {
            // Kinetic/Static collision, resolve it.
            Box staticBox = (firstBox.getType() == BoxType.STATIC) ? firstBox : secondBox;
            Box kineticBox = (firstBox.getType() == BoxType.KINETIC) ? firstBox : secondBox;
            // Do our collision resolution based on the specified axis.
            switch (axis) {
                case X:
                    if (kineticBox.getVelX() > 0) {
                        kineticBox.setX(staticBox.getX() - kineticBox.getWidth());
                        // Flip velocity
                        kineticBox.setVelX(-kineticBox.getVelX() * (kineticBox.getRestitution() + staticBox.getRestitution()));
                        // Notify boxes of collision.
                        staticBox.onCollisonWithKineticBox(kineticBox, new IntersectionPoint(kineticBox.getCurrentOriginPoint(), BoxEdge.LEFT));
                        kineticBox.onCollisonWithStaticBox(staticBox, new IntersectionPoint(kineticBox.getCurrentOriginPoint(), BoxEdge.LEFT));
                    } else if (kineticBox.getVelX() < 0) {
                        kineticBox.setX(staticBox.getX() + staticBox.getWidth());
                        // Flip velocity
                        kineticBox.setVelX(-kineticBox.getVelX() * (kineticBox.getRestitution() + staticBox.getRestitution()));
                        // Notify boxes of collision.
                        staticBox.onCollisonWithKineticBox(kineticBox, new IntersectionPoint(kineticBox.getCurrentOriginPoint(), BoxEdge.RIGHT));
                        kineticBox.onCollisonWithStaticBox(staticBox, new IntersectionPoint(kineticBox.getCurrentOriginPoint(), BoxEdge.RIGHT));
                    }
                    break;
                case Y:
                    if (kineticBox.getVelY() > 0) {
                        kineticBox.setY(staticBox.getY() - kineticBox.getHeight());
                        // Flip velocity
                        kineticBox.setVelY(-kineticBox.getVelY() * (kineticBox.getRestitution() + staticBox.getRestitution()));
                        // Notify boxes of collision.
                        staticBox.onCollisonWithKineticBox(kineticBox, new IntersectionPoint(kineticBox.getCurrentOriginPoint(), BoxEdge.BOTTOM));
                        kineticBox.onCollisonWithStaticBox(staticBox, new IntersectionPoint(kineticBox.getCurrentOriginPoint(), BoxEdge.BOTTOM));
                    } else if (kineticBox.getVelY() < 0) {
                        kineticBox.setY(staticBox.getY() + staticBox.getHeight());
                        // Flip velocity
                        kineticBox.setVelY(-kineticBox.getVelY() * (kineticBox.getRestitution() + staticBox.getRestitution()));
                        // Reduce X velocity based on friction.
                        kineticBox.setVelX(kineticBox.getVelX() * (kineticBox.getFriction() + staticBox.getFriction()));
                        // Notify boxes of collision.
                        staticBox.onCollisonWithKineticBox(kineticBox, new IntersectionPoint(kineticBox.getCurrentOriginPoint(), BoxEdge.TOP));
                        kineticBox.onCollisonWithStaticBox(staticBox, new IntersectionPoint(kineticBox.getCurrentOriginPoint(), BoxEdge.TOP));
                    }
                    break;
            }
        } else if (secondBox.getType() == BoxType.KINETIC && firstBox.getType() == BoxType.KINETIC) {
            // We have a Kinetic/Kinetic collision. We can't resolve this at the moment, so the best
            // we can do is notify each one of the collision. And while there is no actual intersection
            // we can pass an IntersectionPoint point which points to the mid-point between the entities.
            float midpointX = (secondBox.getX() + firstBox.getX()) / 2f;
            float midpointY = (secondBox.getY() + firstBox.getY()) / 2f;
            IntersectionPoint point = new IntersectionPoint(midpointX, midpointY, BoxEdge.NONE);
            firstBox.onCollisonWithKineticBox(secondBox, point);
            secondBox.onCollisonWithKineticBox(firstBox, point);
        }
    }

    /**
     * Get the distance between two points.
     * @param pointA The first point.
     * @param pointB The second point.
     * @return The distance between the two points.
     */
    public static float getDistanceBetweenPoints(Point pointA, Point pointB) {
        return (float) Math.sqrt(Math.pow((pointA.getX() - pointB.getX()), 2) + Math.pow((pointA.getY() - pointB.getY()), 2));
    }

    /**
     * Get the angle between two points.
     * @param pointA The first point.
     * @param pointB The second point.
     * @return The angle between the two points.
     */
    public static float getAngleBetweenPoints(Point pointA, Point pointB) {
        float deltaY = pointB.getY() - pointA.getY();
        float deltaX = pointB.getX() - pointA.getX();
        return (float) (Math.atan2(deltaY, deltaX));
    }
}
