package com.dumbpug.nbp;

import com.dumbpug.nbp.point.IntersectionPoint;
import com.dumbpug.nbp.point.Point;

/**
 * Helper class for basic box collision detection and resolution.
 */
public class Utilities {
	
	/**
     * Handles a collision between a dynamic and static box on an axis.
     * @param dynamicBox The dynamic box.
     * @param staticBox  The static box.
     * @param axis       The axis on which to handle this collision.
     */
    public static void handleCollision(Box dynamicBox, Box staticBox, Axis axis) {
    	// Get the velocity of the dynamic box on the current axis.
    	float velocity = dynamicBox.getVelocity(axis);
    	// Do our collision resolution based on the specified axis.
        switch (axis) {
            case X:
                if (velocity > 0) {
                    dynamicBox.setX(staticBox.getX() - dynamicBox.getWidth());
                } else if (velocity < 0) {
                    dynamicBox.setX(staticBox.getX() + staticBox.getWidth());
                }
                // Flip velocity
                dynamicBox.setVelocity(Axis.X, -velocity * (dynamicBox.getRestitution() + staticBox.getRestitution()));
                break;
            case Y:
                if (velocity > 0) {
                    dynamicBox.setY(staticBox.getY() - dynamicBox.getHeight());
                } else if (velocity < 0) {
                    dynamicBox.setY(staticBox.getY() + staticBox.getHeight());
                }
                // Flip velocity
                dynamicBox.setVelocity(Axis.Y, -velocity * (dynamicBox.getRestitution() + staticBox.getRestitution()));
                break;
			case Z:
				if (velocity > 0) {
                    dynamicBox.setZ(staticBox.getZ() - dynamicBox.getDepth());
                } else if (velocity < 0) {
                    dynamicBox.setZ(staticBox.getZ() + staticBox.getDepth());
                }
				// Flip velocity
                dynamicBox.setVelocity(Axis.Z, -velocity * (dynamicBox.getRestitution() + staticBox.getRestitution()));
				break;
			default:
				throw new RuntimeException("Invalid axis: " + axis);
        }
    }
    
    /**
     * Creates a dynamic projection for a box, represesting the space in which an 
     * AABB may collide with the dynamic box as part of the bynamic box's update. 
     * @param dynamicBox The dynamic box to get the projection for.
     * @return A dynamic projection for a box.
     */
    public static AABB createDynamicProjection(Box dynamicBox) {
    	// The type of projection we create depends on whether we are in 2D or 3D space.
    	if (dynamicBox.getDimension() == Dimension.THREE_DIMENSIONS) {
    		// Find the x/y/z position of the box after the physics update.
    		float destX = dynamicBox.getX() + dynamicBox.getVelocity(Axis.X);
    		float destY = dynamicBox.getY() + dynamicBox.getVelocity(Axis.Y);
    		float destZ = dynamicBox.getZ() + dynamicBox.getVelocity(Axis.Z);
    		// Find the size/dimensions of the projection.
    		float projectionX      = Math.min(dynamicBox.getX(), destX);
    		float projectionY      = Math.min(dynamicBox.getY(), destY);
    		float projectionZ      = Math.min(dynamicBox.getZ(), destZ);
    		float projectionWidth  = (Math.max(dynamicBox.getX(), destX) + dynamicBox.getWidth()) - projectionX;
    		float projectionHeight = (Math.max(dynamicBox.getY(), destY) + dynamicBox.getHeight()) - projectionY;
    		float projectionDepth  = (Math.max(dynamicBox.getZ(), destZ) + dynamicBox.getDepth()) - projectionZ;
    		// Create and return the projection.
    		return new AABB(projectionX, projectionY, projectionY, projectionWidth, projectionHeight, projectionDepth);
    	} else {
    		// Find the x/y position of the box after the physics update.
    		float destX = dynamicBox.getX() + dynamicBox.getVelocity(Axis.X);
    		float destY = dynamicBox.getY() + dynamicBox.getVelocity(Axis.Y);
    		// Find the size/dimensions of the projection.
    		float projectionX      = Math.min(dynamicBox.getX(), destX);
    		float projectionY      = Math.min(dynamicBox.getY(), destY);
    		float projectionWidth  = (Math.max(dynamicBox.getX(), destX) + dynamicBox.getWidth()) - projectionX;
    		float projectionHeight = (Math.max(dynamicBox.getY(), destY) + dynamicBox.getHeight()) - projectionY;
    		// Create and return the projection.
    		return new AABB(projectionX, projectionY, projectionWidth, projectionHeight);
    	}
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
    	// Secondly, we need to flip the velocity of the box, taking restitution and friction into account.
    	switch (intersection.getIntersectionEdge()) {
			case BOTTOM:
			case TOP:
				dynamicBox.setVelocity(Axis.Y, -dynamicBox.getVelocity(Axis.Y) * (dynamicBox.getRestitution() + staticBox.getRestitution()));
				break;
			case LEFT:
			case RIGHT:
				dynamicBox.setVelocity(Axis.X, -dynamicBox.getVelocity(Axis.X) * (dynamicBox.getRestitution() + staticBox.getRestitution()));
				break;
			default:
				throw new RuntimeException("Invalid box edge: " + intersection.getIntersectionEdge());
		}
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
		double inVal = (aX - bX) * (cY - dY) - (aY - bY) * (cX - dX);
		// Check whether there is no intersection (lines are parallel).
		if (inVal == 0) {
			 return null;
		}
		// Get the x/y of intersection.
		double x = ((cX - dX) * (aX * bY - aY * bX) - (aX - bX) * (cX * dY - cY * dX)) / inVal;
		double y = ((cY - dY) * (aX * bY - aY * bX) - (aY - bY) * (cX * dY - cY * dX)) / inVal;
		// Return the intersection point, rounding the values to avoid wacky floating point errors.
		return new Point((float) (Math.round(x * 1000.0) / 1000.0), (float) (Math.round(y * 1000.0) / 1000.0));
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
