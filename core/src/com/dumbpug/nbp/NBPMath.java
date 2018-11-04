package com.dumbpug.nbp;

import java.util.ArrayList;
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
     * Finds the closest point of intersection between a dynamic and static box.
     * @param preUpdateDynamicOrigin The origin of the dynamic box before intersection.
     * @param dynamicBox The dynamic box.
     * @param staticBox The static box.
     * @return The closest point of intersection between a dynamic and static box.
     */
    public static IntersectionPoint getClosestIntersectionPoint(Point preUpdateDynamicOrigin, Box dynamicBox, Box staticBox) {
    	// Get the new origin of the dynamic box.
    	Point postUpdateDynamicOrigin = dynamicBox.getOrigin();
    	// Firstly, we need to get the Minkowski sum of the static box.
    	// Find the position of the sum.
		float x = staticBox.getX() - (dynamicBox.getWidth() / 2f);
		float y = staticBox.getY() - (dynamicBox.getHeight() / 2f);
		// Get the width/height of the sum.
		float height = staticBox.getHeight() + dynamicBox.getHeight();
		float width  = staticBox.getWidth() + dynamicBox.getWidth();
		// Keep track of the intersections.
		// TODO We can stop looking when we get two intersections.
		ArrayList<IntersectionPoint> intersections = new ArrayList<IntersectionPoint>();
    	// Check for an intersection with the top edge of the box.
		Point topEdgeIntersection = NBPMath.getLineVsLineIntersection(
				preUpdateDynamicOrigin.getX(), preUpdateDynamicOrigin.getY(),
				postUpdateDynamicOrigin.getX(), postUpdateDynamicOrigin.getY(),
				x, y + height,
				x + width, y + height
		);
		if (topEdgeIntersection != null && doesPointIntersectBox(topEdgeIntersection, x, y, width, height)) {
			intersections.add(new IntersectionPoint(topEdgeIntersection.getX(), topEdgeIntersection.getY(), BoxEdge.TOP));
		}
		// Check for an intersection with the right edge of the box.
		Point rightEdgeIntersection = NBPMath.getLineVsLineIntersection(
				preUpdateDynamicOrigin.getX(), preUpdateDynamicOrigin.getY(),
				postUpdateDynamicOrigin.getX(), postUpdateDynamicOrigin.getY(),
				x + width, y,
				x + width, y + height
		);
		if (rightEdgeIntersection != null && doesPointIntersectBox(rightEdgeIntersection, x, y, width, height)) {
			intersections.add(new IntersectionPoint(rightEdgeIntersection.getX(), rightEdgeIntersection.getY(), BoxEdge.RIGHT));
		}
		// Check for an intersection with the bottom edge of the box.
		Point bottomEdgeIntersection = NBPMath.getLineVsLineIntersection(
				preUpdateDynamicOrigin.getX(), preUpdateDynamicOrigin.getY(),
				postUpdateDynamicOrigin.getX(), postUpdateDynamicOrigin.getY(),
				x, y,
				x + width, y
		);
		if (bottomEdgeIntersection != null && doesPointIntersectBox(bottomEdgeIntersection, x, y, width, height)) {
			intersections.add(new IntersectionPoint(bottomEdgeIntersection.getX(), bottomEdgeIntersection.getY(), BoxEdge.BOTTOM));
		}
		// Check for an intersection with the left edge of the box.
		Point leftEdgeIntersection = NBPMath.getLineVsLineIntersection(
				preUpdateDynamicOrigin.getX(), preUpdateDynamicOrigin.getY(),
				postUpdateDynamicOrigin.getX(), postUpdateDynamicOrigin.getY(),
				x, y,
				x, y + height
		);
		if (leftEdgeIntersection != null && doesPointIntersectBox(leftEdgeIntersection, x, y, width, height)) {
			intersections.add(new IntersectionPoint(leftEdgeIntersection.getX(), leftEdgeIntersection.getY(), BoxEdge.LEFT));
		}
		// We should only have two intersections.
		if (intersections.size() != 2) {
			throw new RuntimeException("Expected two intersection edges, got " + intersections.size());
		}
		// Get the distances of both intersections from the original dynamic box origin.
		float firstIntersectionDistance  = getDistanceBetweenPoints(preUpdateDynamicOrigin, intersections.get(0));
		float secondIntersectionDistance = getDistanceBetweenPoints(preUpdateDynamicOrigin, intersections.get(1));
		// Return the closest intersection.
    	return firstIntersectionDistance < secondIntersectionDistance ? intersections.get(0) : intersections.get(1);
    }
    
    /**
     * Resolve a collision with a dynamic and static box, this involves moving the dynamic box out of the static one and updating its velocity.
     * @param intersection The point of intersection, defining the dynalic box origin at the exact point of collision.
     * @param dynamicBox The dynamic box.
     * @param staticBox The static box.
     */
    public static void resolveDynamicAndStaticBoxCollision(IntersectionPoint intersection, Box dynamicBox, Box staticBox) {
    	// Firstly, we need to move the dynamic box to the position it was at when it collided with the static one.
    	// To avoid rounding issues we will handle this based on the intersection edge.
    	switch (intersection.getIntersectionEdge()) {
			case BOTTOM:
				// We hit the bottom edge of the static box.
				dynamicBox.setY(staticBox.getY() - dynamicBox.getHeight());
				dynamicBox.setX(intersection.getX() - (dynamicBox.getWidth() / 2f));
				break;
			case TOP:
				// We hit the top edge of the static box.
				dynamicBox.setY(staticBox.getY() + staticBox.getHeight());
				dynamicBox.setX(intersection.getX() - (dynamicBox.getWidth() / 2f));
				break;
			case LEFT:
				// We hit the left edge of the static box.
				dynamicBox.setY(intersection.getY() - (dynamicBox.getHeight() / 2f));
				dynamicBox.setX(staticBox.getX() - dynamicBox.getWidth());
				break;
			case RIGHT:
				// We hit the right edge of the static box.
				dynamicBox.setY(intersection.getY() - (dynamicBox.getHeight() / 2f));
				dynamicBox.setX(staticBox.getX() + staticBox.getWidth());
				break;
			default:
				throw new RuntimeException("Invalid box edge: " + intersection.getIntersectionEdge());
    	}
    	// TODO Update the velocity of the dynamic box, taking friction and restitution into account.
    }
    
    /**
     * Returns the point of intersection between two lines, returns null if no intersection exists.
     * @param aX
     * @param aY
     * @param bX
     * @param bY
     * @param cX
     * @param cY
     * @param dX
     * @param dY
     * @return The point of intersection between two lines, returns null if no intersection exists.
     */
    public static Point getLineVsLineIntersection(float aX, float aY, float bX, float bY, float cX, float cY, float dX, float dY) {
		float inVal = (aX - bX) * (cY - dY) - (aY - bY) * (cX - dX);
		// Check whether there is no intersection (lines are parallel).
		if (inVal == 0) {
			 return null;
		}
		// Get the point of intersection.
		float x = ((cX - dX) * (aX * bY - aY * bX) - (aX - bX) * (cX * dY - cY * dX)) / inVal;
		float y = ((cY - dY) * (aX * bY - aY * bX) - (aY - bY) * (cX * dY - cY * dX)) / inVal;
		return new Point(x, y);
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

	/**
	 * Gets whether a point is within the bounds of a box.
	 * @param point The point.
	 * @param x The x position of the box.
	 * @param y The y position of the box.
	 * @param width The width of the box.
	 * @param height The height of the box.
     * @return Whether a point is within the bounds of a box.
     */
	public static boolean doesPointIntersectBox(Point point, float x, float y, float width, float height) {
		// Does the point exceed the bounds of the box on the X axis.
		if (point.getX() < x || point.getX() > (x + width)) {
			return false;
		}
		// Does the point exceed the bounds of the box on the Y axis.
		if (point.getY() < y || point.getY() > (y + height)) {
			return false;
		}
		// The point is within the bounds of the box.
		return true;
	}
}
