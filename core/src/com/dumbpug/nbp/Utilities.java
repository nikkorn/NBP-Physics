package com.dumbpug.nbp;

import com.dumbpug.nbp.point.Point;
import com.dumbpug.nbp.projection.BoxProjection;

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
     * Creates a projection for a box, represesting the space in which an AABB may collide with the box as part of the box's update. 
     * @param box The box to get the projection for.
     * @return A projection for a box.
     */
    public static BoxProjection createBoxProjection(Box box) {
    	// The type of projection we create depends on whether we are in 2D or 3D space.
    	if (box.getDimension() == Dimension.THREE_DIMENSIONS) {
    		// Find the x/y/z position of the box after the physics update.
    		float destX = box.getX() + box.getVelocity(Axis.X);
    		float destY = box.getY() + box.getVelocity(Axis.Y);
    		float destZ = box.getZ() + box.getVelocity(Axis.Z);
    		// Find the size/dimensions of the projection.
    		float projectionX      = Math.min(box.getX(), destX);
    		float projectionY      = Math.min(box.getY(), destY);
    		float projectionZ      = Math.min(box.getZ(), destZ);
    		float projectionWidth  = (Math.max(box.getX(), destX) + box.getWidth()) - projectionX;
    		float projectionHeight = (Math.max(box.getY(), destY) + box.getHeight()) - projectionY;
    		float projectionDepth  = (Math.max(box.getZ(), destZ) + box.getDepth()) - projectionZ;
    		// Create and return the projection.
    		return new BoxProjection(box, projectionX, projectionY, projectionY, projectionWidth, projectionHeight, projectionDepth);
    	} else {
    		// Find the x/y position of the box after the physics update.
    		float destX = box.getX() + box.getVelocity(Axis.X);
    		float destY = box.getY() + box.getVelocity(Axis.Y);
    		// Find the size/dimensions of the projection.
    		float projectionX      = Math.min(box.getX(), destX);
    		float projectionY      = Math.min(box.getY(), destY);
    		float projectionWidth  = (Math.max(box.getX(), destX) + box.getWidth()) - projectionX;
    		float projectionHeight = (Math.max(box.getY(), destY) + box.getHeight()) - projectionY;
    		// Create and return the projection.
    		return new BoxProjection(box, projectionX, projectionY, projectionWidth, projectionHeight);
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
