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
     * Handles a collision between two boxes.
     * @param box a
     * @param box b
     */
    public static void handleCollision(NBPBox a, NBPBox b) {
        // Are we dealing with a Kinetic/Static collision or a Kinetic/Kinetic one?
        if((a.getType() == NBPBoxType.KINETIC && b.getType() == NBPBoxType.STATIC) ||
                (b.getType() == NBPBoxType.KINETIC && a.getType() == NBPBoxType.STATIC)) {
            // Kinetic/Static collision, resolve it.
            NBPBox staticBox  = (a.getType() == NBPBoxType.STATIC) ? a : b;
            NBPBox kineticBox = (a.getType() == NBPBoxType.KINETIC) ? a : b;
            // Do our collision resolution.
            NBPIntersectionPoint intersectionPoint = doCollisionResolution(staticBox, kineticBox);
            // Notify boxes of collision.
            staticBox.onCollisonWithKineticBox(kineticBox, intersectionPoint);
            kineticBox.onCollisonWithStaticBox(staticBox, intersectionPoint);
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
     * Resolves a collision between a kinematic and static box.
     * @param staticBox
     * @param kineticBox
     * @return Origin of the kinematic object at the exact point of collision.
     */
    private static NBPIntersectionPoint doCollisionResolution(NBPBox staticBox, NBPBox kineticBox) {
    	// Create a new box and set its height to match the size of staticbox enlarged by the kinematicbox.
    	NBPBox enlargedBox;
    	float enlargedBoxPosX = staticBox.getX() - (kineticBox.getWidth()/2f);
    	float enlargedBoxPosY = staticBox.getY() - (kineticBox.getHeight()/2f);
    	float enlargedBoxWidth = staticBox.getWidth() + kineticBox.getWidth();
    	float enlargedBoxHeight = staticBox.getHeight() + kineticBox.getHeight();
    	enlargedBox = new NBPEnlargedBox(enlargedBoxPosX, enlargedBoxPosY, enlargedBoxWidth, enlargedBoxHeight, NBPBoxType.STATIC);
    	
    	// Keep list of intersection points where our movement line crosses the bounds of our static box.
    	ArrayList<NBPIntersectionPoint> extendedBoxIntersectionPoints = new ArrayList<NBPIntersectionPoint>();
    	
    	// Go over each edge of our enlarged box and check whether our line of movement intersects it.
    	NBPPoint intersection;
    	NBPPoint topLeftPoint = new NBPPoint(enlargedBox.getX(), enlargedBox.getY() + enlargedBox.getHeight());
    	NBPPoint topRightPoint = new NBPPoint(enlargedBox.getX() + enlargedBox.getWidth() , enlargedBox.getY() + enlargedBox.getHeight());
    	NBPPoint bottomLeftPoint = new NBPPoint(enlargedBox.getX(), enlargedBox.getY());
    	NBPPoint bottomRightPoint = new NBPPoint(enlargedBox.getX() + enlargedBox.getWidth() , enlargedBox.getY());
    	
    	// Check top.
    	intersection = getIntersectionPointOFTwoLineSegments(kineticBox.getLastOriginPoint(), kineticBox.getCurrentOriginPoint(),
    			topLeftPoint, topRightPoint);
    	if(intersection != null) {
    		// Our movement line intersects the top bound.
    		extendedBoxIntersectionPoints.add(new NBPIntersectionPoint(intersection, NBPIntersectionDirection.TOP));
    	}
    	
    	// Check bottom.
    	intersection = getIntersectionPointOFTwoLineSegments(kineticBox.getLastOriginPoint(), kineticBox.getCurrentOriginPoint(),
    			bottomLeftPoint, bottomRightPoint);
    	if(intersection != null) {
    		// Our movement line intersects the bottom bound.
    		extendedBoxIntersectionPoints.add(new NBPIntersectionPoint(intersection, NBPIntersectionDirection.BOTTOM));
    	}
    	
    	// Check left.
    	intersection = getIntersectionPointOFTwoLineSegments(kineticBox.getLastOriginPoint(), kineticBox.getCurrentOriginPoint(),
    			topLeftPoint, bottomLeftPoint);
    	if(intersection != null) {
    		// Our movement line intersects the left bound.
    		extendedBoxIntersectionPoints.add(new NBPIntersectionPoint(intersection, NBPIntersectionDirection.SIDE_LEFT));
    	}
    	
    	// Check right.
    	intersection = getIntersectionPointOFTwoLineSegments(kineticBox.getLastOriginPoint(), kineticBox.getCurrentOriginPoint(),
    			topRightPoint, bottomRightPoint);
    	if(intersection != null) {
    		// Our movement line intersects the right bound.
    		extendedBoxIntersectionPoints.add(new NBPIntersectionPoint(intersection, NBPIntersectionDirection.SIDE_RIGHT));
    	}
    	
    	NBPIntersectionPoint intersectionPoint = getClosestPoint(kineticBox.getLastOriginPoint(), extendedBoxIntersectionPoints);
		// Change box velocity based on intersection direction
    	// TODO Find out why getClosestPoint() sometimes gives us a null!!!
		switch(intersectionPoint.getIntersectionDir()) {
		case TOP:
			kineticBox.setY(intersectionPoint.getY() - (kineticBox.getHeight()/2f));
			// Reduce X velocity based on friction.
            kineticBox.setVelx(kineticBox.getVelx() * (kineticBox.getFriction() + staticBox.getFriction()));
            // Flip velocity
			kineticBox.setVely(-kineticBox.getVely() * (kineticBox.getRestitution() + staticBox.getRestitution()));
			break;
		case BOTTOM:
			kineticBox.setY(intersectionPoint.getY() - (kineticBox.getHeight()/2f));
			 // Flip velocity
			kineticBox.setVely(-kineticBox.getVely() * (kineticBox.getRestitution() + staticBox.getRestitution()));
			break;
		case SIDE_LEFT:
			kineticBox.setX(intersectionPoint.getX() - (kineticBox.getWidth()/2f));
			 // Flip velocity
			kineticBox.setVelx(-kineticBox.getVelx() * (kineticBox.getRestitution() + staticBox.getRestitution()));
			break;
		case SIDE_RIGHT:
			kineticBox.setX(intersectionPoint.getX() - (kineticBox.getWidth()/2f));
			 // Flip velocity
			kineticBox.setVelx(-kineticBox.getVelx() * (kineticBox.getRestitution() + staticBox.getRestitution()));
			break;
		default:
			break;
		}
		
		// Return the origin of the kinematic object at the exact point of collision.
		return intersectionPoint;
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
     * Gets the are of intersection between two boxes.
     * @param kin
     * @param sta
     * @return area of intersection
     */
    public static float getIntersectionArea(NBPBox kin, NBPBox sta) {
        // Bottom-left and top-right points of box A.
        float boxABottomLeftX = kin.getX();
        float boxABottomLeftY = kin.getY();
        float boxATopRightX   = kin.getX() + kin.getWidth();
        float boxATopRightY   = kin.getY() + kin.getHeight();
        // Bottom-left and top-right points of box B.
        float boxBBottomLeftX = sta.getX();
        float boxBBottomLeftY = sta.getY();
        float boxBTopRightX   = sta.getX() + sta.getWidth();
        float boxBTopRightY   = sta.getY() + sta.getHeight();
        // Get the intersecting rectangle.
        float intersectionBottomLeftX = Math.max(boxABottomLeftX, boxBBottomLeftX);
        float intersectionBottomLeftY = Math.max(boxABottomLeftY, boxBBottomLeftY);
        float intersectionTopRightX   = Math.min(boxATopRightX, boxBTopRightX);
        float intersectionTopRightY   = Math.min(boxATopRightY, boxBTopRightY);
        // Get intersection height and width.
        float intersectionWidth  = intersectionTopRightX - intersectionBottomLeftX;
        float intersectionHeight = intersectionTopRightY - intersectionBottomLeftY;
        return intersectionWidth * intersectionHeight;
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
