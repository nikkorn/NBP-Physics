package com.dumbpug.nbp;

import com.dumbpug.nbp.point.IntersectionPoint;
import com.dumbpug.nbp.point.Point;

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
    public static void handleCollision(Box firstBox, Box secondBox, Axis axis) {
        // Are we dealing with a Dynamic/Static collision or a Dynamic/Dynamic one?
        if ((firstBox.getType() == BoxType.DYNAMIC && secondBox.getType() == BoxType.STATIC) ||
                (secondBox.getType() == BoxType.DYNAMIC && firstBox.getType() == BoxType.STATIC)) {
            // Dynamic/Static collision, resolve it.
            Box staticBox  = (firstBox.getType() == BoxType.STATIC) ? firstBox : secondBox;
            Box dynamicBox = (firstBox.getType() == BoxType.DYNAMIC) ? firstBox : secondBox;
            // Do our collision resolution based on the specified axis.
            switch (axis) {
                case X:
                    if (dynamicBox.getVelX() > 0) {
                        dynamicBox.setX(staticBox.getX() - dynamicBox.getWidth());
                        // Flip velocity
                        dynamicBox.setVelX(-dynamicBox.getVelX() * (dynamicBox.getRestitution() + staticBox.getRestitution()));
                        // Notify boxes of collision.
                        staticBox.onCollisionWithDynamicBox(dynamicBox, new IntersectionPoint(dynamicBox.getCurrentOriginPoint(), BoxEdge.LEFT));
                        dynamicBox.onCollisionWithStaticBox(staticBox, new IntersectionPoint(dynamicBox.getCurrentOriginPoint(), BoxEdge.LEFT));
                    } else if (dynamicBox.getVelX() < 0) {
                        dynamicBox.setX(staticBox.getX() + staticBox.getWidth());
                        // Flip velocity
                        dynamicBox.setVelX(-dynamicBox.getVelX() * (dynamicBox.getRestitution() + staticBox.getRestitution()));
                        // Notify boxes of collision.
                        staticBox.onCollisionWithDynamicBox(dynamicBox, new IntersectionPoint(dynamicBox.getCurrentOriginPoint(), BoxEdge.RIGHT));
                        dynamicBox.onCollisionWithStaticBox(staticBox, new IntersectionPoint(dynamicBox.getCurrentOriginPoint(), BoxEdge.RIGHT));
                    }
                    break;
                case Y:
                    if (dynamicBox.getVelY() > 0) {
                        dynamicBox.setY(staticBox.getY() - dynamicBox.getHeight());
                        // Flip velocity
                        dynamicBox.setVelY(-dynamicBox.getVelY() * (dynamicBox.getRestitution() + staticBox.getRestitution()));
                        // Notify boxes of collision.
                        staticBox.onCollisionWithDynamicBox(dynamicBox, new IntersectionPoint(dynamicBox.getCurrentOriginPoint(), BoxEdge.BOTTOM));
                        dynamicBox.onCollisionWithStaticBox(staticBox, new IntersectionPoint(dynamicBox.getCurrentOriginPoint(), BoxEdge.BOTTOM));
                    } else if (dynamicBox.getVelY() < 0) {
                        dynamicBox.setY(staticBox.getY() + staticBox.getHeight());
                        // Flip velocity
                        dynamicBox.setVelY(-dynamicBox.getVelY() * (dynamicBox.getRestitution() + staticBox.getRestitution()));
                        // Reduce X velocity based on friction.
                        dynamicBox.setVelX(dynamicBox.getVelX() * (dynamicBox.getFriction() + staticBox.getFriction()));
                        // Notify boxes of collision.
                        staticBox.onCollisionWithDynamicBox(dynamicBox, new IntersectionPoint(dynamicBox.getCurrentOriginPoint(), BoxEdge.TOP));
                        dynamicBox.onCollisionWithStaticBox(staticBox, new IntersectionPoint(dynamicBox.getCurrentOriginPoint(), BoxEdge.TOP));
                    }
                    break;
				case Z:
					// TODO ...
					break;
				default:
					throw new RuntimeException("Invalid Axis: " + axis);
            }
        } else if (secondBox.getType() == BoxType.DYNAMIC && firstBox.getType() == BoxType.DYNAMIC) {
            // We have a Dynamic/Dynamic collision. We can't resolve this at the moment, so the best
            // we can do is notify each one of the collision. And while there is no actual intersection
            // we can pass an IntersectionPoint point which points to the mid-point between the entities.
            float midpointX = (secondBox.getX() + firstBox.getX()) / 2f;
            float midpointY = (secondBox.getY() + firstBox.getY()) / 2f;
            IntersectionPoint point = new IntersectionPoint(midpointX, midpointY, BoxEdge.NONE);
            firstBox.onCollisionWithDynamicBox(secondBox, point);
            secondBox.onCollisionWithDynamicBox(firstBox, point);
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
     * Get the angle between two points as a 0-360 degree value.
     * @param pointA The first point.
     * @param pointB The second point.
     * @param flipY Whether the Y axis is flipped.
     * @param flipX Whether the X axis is flipped.
     * @return The angle between the two points.
     */
    public static float getAngleBetweenPoints(Point pointA, Point pointB, boolean flipY, boolean flipX) {
        float deltaY   = flipY ? pointA.getY() - pointB.getY() : pointB.getY() - pointA.getY();
        float deltaX   = flipX ? pointA.getX() - pointB.getX() : pointB.getX() - pointA.getX();
        double degrees = Math.toDegrees(Math.atan2(deltaY, deltaX));
        degrees        = degrees < 0.0d ? degrees + 360.0d : degrees;
        return (float) degrees;
    }

    /**
     * Get the angle between two points as a 0-360 degree value.
     * @param pointA The first point.
     * @param pointB The second point.
     * @return The angle between the two points.
     */
    public static float getAngleBetweenPoints(Point pointA, Point pointB) {
        return getAngleBetweenPoints(pointA, pointB, false, false);
    }
}
