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
        BOTTOM
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
            // TODO Have a think about whether we should notify boxes of collision after or before resolution.
            staticBox.onCollisonWithKineticBox(kineticBox);
            kineticBox.onCollisonWithStaticBox(staticBox);
            // Do our collision resolution.
            doCollisionResolution(staticBox, kineticBox);
        } else if (b.getType() == NBPBoxType.KINETIC && a.getType() == NBPBoxType.KINETIC) {
            // We have a Kinetic/Kinetic collision, one day when i get super smart i can find a
            // safe solution of resolving this. But for now we are happy with simply notifying
            // both boxes of the collision.
            a.onCollisonWithKineticBox(b);
            b.onCollisonWithKineticBox(a);
        } else {
            // TODO For some wacky reason we have a Static/Static collision, do whatever.
        }
    }
    
    /**
     * Resolves a collision between a kinematic and static box.
     * @param staticBox
     * @param kineticBox
     */
    private static void doCollisionResolution(NBPBox staticBox, NBPBox kineticBox) {
    	// Create a new box and set its height to match the size of staticbox enlarged by the kinematicbox.
    	NBPBox enlargedBox;
    	float enlargedBoxPosX = staticBox.getX() - (kineticBox.getWidth()/2f);
    	float enlargedBoxPosY = staticBox.getY() - (kineticBox.getHeight()/2f);
    	float enlargedBoxWidth = staticBox.getWidth() + kineticBox.getWidth();
    	float enlargedBoxHeight = staticBox.getHeight() + kineticBox.getHeight();
    	enlargedBox = new NBPBox(enlargedBoxPosX, enlargedBoxPosY, enlargedBoxWidth, enlargedBoxHeight, NBPBoxType.STATIC);
    	
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
    			bottomRightPoint, topRightPoint);
    	if(intersection != null) {
    		// Our movement line intersects the right bound.
    		extendedBoxIntersectionPoints.add(new NBPIntersectionPoint(intersection, NBPIntersectionDirection.SIDE_RIGHT));
    	}
    	
    	NBPIntersectionPoint intersectionPoint = getClosestPoint(kineticBox.getLastOriginPoint(), extendedBoxIntersectionPoints);
		// Change box velocity based on intersection direction
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
}
