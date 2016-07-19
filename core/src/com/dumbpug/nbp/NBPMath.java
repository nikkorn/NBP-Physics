package com.dumbpug.nbp;

import java.util.ArrayList;

/**
 * Created by Nikolas Howard on 25/02/16.
 * Class for basic box collision detection and resolution.
 */
public class NBPMath {

    /**
     * Defines a direction at which a kinetic box enters a static box.
     * @author Nikolas Howard
     *
     */
    public enum NBPIntersectionDirection {
        SIDE_LEFT,
        SIDE_RIGHT,
        TOP,
        BOTTOM,
        NONE
    }
    
    /**
     * Calculates whether two boxes intersect.
     * @param box a
     * @param box b
     * @return intersection exists
     */
    public static boolean doBoxesCollide(NBPBox a, NBPBox b) {
        if(a.getX() < (b.getX() + b.getWidth()) &&
                (a.getX() + a.getWidth()) > b.getX() &&
                a.getY() < (b.getY() + b.getHeight()) &&
                (a.getY() + a.getHeight()) > b.getY()) {
            return true;
        }
        return false;
    }

    /**
     * Calculates whether a sensor and box intersect.
     * @param sensor a
     * @param box b
     * @return intersection exists
     */
    public static boolean doesSensorCollideWithBox(NBPSensor sensor, NBPBox box) {
        if(sensor.getX() < (box.getX() + box.getWidth()) &&
                (sensor.getX() + sensor.getWidth()) > box.getX() &&
                sensor.getY() < (box.getY() + box.getHeight()) &&
                (sensor.getY() + sensor.getHeight()) > box.getY()) {
            return true;
        }
        return false;
    }
    
    /**
     * Handles a collision between two boxes on an axis.
     * @param box a
     * @param box b
     * @param NBPCollisionAxis axis
     */
    public static void handleCollision(NBPBox a, NBPBox b, NBPCollisionAxis axis) {
        // Are we dealing with a Kinetic/Static collision or a Kinetic/Kinetic one?
        if((a.getType() == NBPBoxType.KINETIC && b.getType() == NBPBoxType.STATIC) ||
                (b.getType() == NBPBoxType.KINETIC && a.getType() == NBPBoxType.STATIC)) {
            // Kinetic/Static collision, resolve it.
            NBPBox staticBox  = (a.getType() == NBPBoxType.STATIC) ? a : b;
            NBPBox kineticBox = (a.getType() == NBPBoxType.KINETIC) ? a : b;
            // Do our collision resolution based on the specified axis.
            switch(axis) {
			case X:
				 if(kineticBox.getVelx() > 0) {
	            	kineticBox.setX(staticBox.getX() - kineticBox.getWidth());
	            	// Flip velocity
	    			kineticBox.setVelx(-kineticBox.getVelx() * (kineticBox.getRestitution() + staticBox.getRestitution()));
	            	// Notify boxes of collision.
	                staticBox.onCollisonWithKineticBox(kineticBox, new NBPIntersectionPoint(kineticBox.getCurrentOriginPoint(), NBPIntersectionDirection.SIDE_LEFT));
	                kineticBox.onCollisonWithStaticBox(staticBox, new NBPIntersectionPoint(kineticBox.getCurrentOriginPoint(), NBPIntersectionDirection.SIDE_LEFT));
	            } else if(kineticBox.getVelx() < 0) {
	            	kineticBox.setX(staticBox.getX() + staticBox.getWidth());
	            	// Flip velocity
	    			kineticBox.setVelx(-kineticBox.getVelx() * (kineticBox.getRestitution() + staticBox.getRestitution()));
	            	// Notify boxes of collision.
	                staticBox.onCollisonWithKineticBox(kineticBox, new NBPIntersectionPoint(kineticBox.getCurrentOriginPoint(), NBPIntersectionDirection.SIDE_RIGHT));
	                kineticBox.onCollisonWithStaticBox(staticBox, new NBPIntersectionPoint(kineticBox.getCurrentOriginPoint(), NBPIntersectionDirection.SIDE_RIGHT));
	            } 
				break;
			case Y:
				if(kineticBox.getVely() > 0) {
	            	kineticBox.setY(staticBox.getY() - kineticBox.getHeight());
	            	// Flip velocity
	    			kineticBox.setVely(-kineticBox.getVely() * (kineticBox.getRestitution() + staticBox.getRestitution()));
	            	// Notify boxes of collision.
	                staticBox.onCollisonWithKineticBox(kineticBox, new NBPIntersectionPoint(kineticBox.getCurrentOriginPoint(), NBPIntersectionDirection.BOTTOM));
	                kineticBox.onCollisonWithStaticBox(staticBox, new NBPIntersectionPoint(kineticBox.getCurrentOriginPoint(), NBPIntersectionDirection.BOTTOM));
	            } else if(kineticBox.getVely() < 0) {
	            	kineticBox.setY(staticBox.getY() + staticBox.getHeight());
	            	// Flip velocity
	    			kineticBox.setVely(-kineticBox.getVely() * (kineticBox.getRestitution() + staticBox.getRestitution()));
	    			// Reduce X velocity based on friction.
	                kineticBox.setVelx(kineticBox.getVelx() * (kineticBox.getFriction() + staticBox.getFriction()));
	            	// Notify boxes of collision.
	                staticBox.onCollisonWithKineticBox(kineticBox, new NBPIntersectionPoint(kineticBox.getCurrentOriginPoint(), NBPIntersectionDirection.TOP));
	                kineticBox.onCollisonWithStaticBox(staticBox, new NBPIntersectionPoint(kineticBox.getCurrentOriginPoint(), NBPIntersectionDirection.TOP));
	            } 
				break;
            }
        } else if (b.getType() == NBPBoxType.KINETIC && a.getType() == NBPBoxType.KINETIC) {
            // We have a Kinetic/Kinetic collision. We can't resolve this at the moment, so the best
        	// we can do is notify each one of the collision. And while there is no actual intersection
        	// we can pass an NBPIntersectionPoint point which points to the mid-point between the entities.
        	float midpointX = (b.getX() + a.getX()) / 2f;
        	float midpointY = (b.getY() + a.getY()) / 2f;
        	NBPIntersectionPoint point = new NBPIntersectionPoint(midpointX, midpointY, NBPIntersectionDirection.NONE);
            a.onCollisonWithKineticBox(b, point);
            b.onCollisonWithKineticBox(a, point);
        } else {
            // TODO For some wacky reason we have a Static/Static collision, do whatever.
        }
    }
    
    /**
     * Returns intersection of two lines, returns null if no intersection exists.
     * @param LnAptA
     * @param LnAptB
     * @param LnBptA
     * @param LnBptB
     * @return intersection point, null if no intersection
     */
    public static NBPPoint getIntersectionPointOFTwoLineSegments(NBPPoint LnAptA, NBPPoint LnAptB, NBPPoint LnBptA, NBPPoint LnBptB) {
        float inVal = (LnAptA.getX()-LnAptB.getX())*(LnBptA.getY()-LnBptB.getY())
                - (LnAptA.getY()-LnAptB.getY())*(LnBptA.getX()-LnBptB.getX());
        if (inVal == 0) {
            return null;
        }
        float intersectionX = ((LnBptA.getX()-LnBptB.getX())*(LnAptA.getX()*LnAptB.getY()-LnAptA.getY()*LnAptB.getX())
                - (LnAptA.getX()-LnAptB.getX())*(LnBptA.getX()*LnBptB.getY()-LnBptA.getY()*LnBptB.getX()))/inVal;
        float intersectionY = ((LnBptA.getY()-LnBptB.getY())*(LnAptA.getX()*LnAptB.getY()-LnAptA.getY()*LnAptB.getX())
                - (LnAptA.getY()-LnAptB.getY())*(LnBptA.getX()*LnBptB.getY()-LnBptA.getY()*LnBptB.getX()))/inVal;
        return new NBPPoint(intersectionX, intersectionY);
    }
    
    /**
     * Get closest NBPIntersectionPoint to target NBPPoint.
     * @param P
     * @param points
     * @return closest point
     */
    public static NBPIntersectionPoint getClosestPoint(NBPPoint P, ArrayList<NBPIntersectionPoint> points) {
    	NBPIntersectionPoint closestPoint = null;
    	double closestDistance = 0;
    	for(NBPIntersectionPoint point : points) {
    		if(closestPoint == null) {
    			closestPoint = point;
    			closestDistance = Math.sqrt(Math.pow((point.getX() - P.getX()), 2) + Math.pow((point.getY() - P.getY()), 2));
    		} else {
    			double distance = Math.sqrt(Math.pow((point.getX() - P.getX()), 2) + Math.pow((point.getY() - P.getY()), 2));
    			if(distance < closestDistance) {
    				closestPoint = point;
    				closestDistance = distance;
    			}
    		}
    	}
        return closestPoint;
    }
    
    /**
     * Gets the area of intersection between two boxes.
     * @param kin
     * @param sta
     * @return area of intersection
     */
    public static float getIntersectionArea(NBPBox kin, NBPBox sta) {
        return getIntersectionWidth(kin, sta) * getIntersectionHeight(kin, sta);
    }
    
    /**
     * Gets the width of intersection between two boxes.
     * @param kin
     * @param sta
     * @return width of intersection
     */
    public static float getIntersectionWidth(NBPBox kin, NBPBox sta) {
        // Bottom-left and top-right points of box A.
        float boxABottomLeftX = kin.getX();
        float boxATopRightX   = kin.getX() + kin.getWidth();
        // Bottom-left and top-right points of box B.
        float boxBBottomLeftX = sta.getX();
        float boxBTopRightX   = sta.getX() + sta.getWidth();
        // Get the intersecting rectangle.
        float intersectionBottomLeftX = Math.max(boxABottomLeftX, boxBBottomLeftX);
        float intersectionTopRightX   = Math.min(boxATopRightX, boxBTopRightX);
        // Get intersection height and width.
        return intersectionTopRightX - intersectionBottomLeftX;
    }
    
    /**
     * Gets the height of intersection between two boxes.
     * @param kin
     * @param sta
     * @return height of intersection
     */
    public static float getIntersectionHeight(NBPBox kin, NBPBox sta) {
        // Bottom-left and top-right points of box A.
        float boxABottomLeftY = kin.getY();
        float boxATopRightY   = kin.getY() + kin.getHeight();
        // Bottom-left and top-right points of box B.
        float boxBBottomLeftY = sta.getY();
        float boxBTopRightY   = sta.getY() + sta.getHeight();
        // Get the intersecting rectangle.
        float intersectionBottomLeftY = Math.max(boxABottomLeftY, boxBBottomLeftY);
        float intersectionTopRightY   = Math.min(boxATopRightY, boxBTopRightY);
        // Get intersection height and width.
        return intersectionTopRightY - intersectionBottomLeftY;
    }
    
    /**
     * Find whether a point lies within the bounds of a circle
     * @param point
     * @param circleCenter
     * @param circleRadius
     * @return point lies in the circle.
     */
    public static boolean isPointInCircle(NBPPoint point, NBPPoint circleCenter, float circleRadius) {
    	float dist = getDistanceBetweenPoints(point, circleCenter);
    	return dist <= circleRadius;
    }
    
    /**
     * Get the distance between two points.
     * @param pointA
     * @param pointB
     * @return distance.
     */
    public static float getDistanceBetweenPoints(NBPPoint pointA, NBPPoint pointB) {
    	return (float) Math.sqrt(Math.pow((pointA.getX() - pointB.getX()), 2) + Math.pow((pointA.getY() - pointB.getY()), 2));
    }
    
    /**
     * Get the angle between two points.
     * @param a
     * @param b
     * @return
     */
    public static float getAngleBetweenPoints(NBPPoint a, NBPPoint b) {
    	float deltaY = b.getY() - a.getY();
    	float deltaX = b.getX() - a.getX();
    	return (float) (Math.atan2(deltaY, deltaX)); 
    }
}
