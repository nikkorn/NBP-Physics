package com.dumbpug.nbp;

import java.util.ArrayList;
import java.util.ListIterator;

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
        EQUAL
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
            
            // --------- TEMP -----
            // doCollisionResolution(staticBox, kineticBox);
            doCollisionResolutionViaSweep(staticBox, kineticBox);
            // --------- TEMP -----
            
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
     * Resolves a collision between a kinetic and static box.
     * @param staticBox
     * @param kineticBox
     */
    private static void doCollisionResolution(NBPBox staticBox, NBPBox kineticBox) {
        // Get penetration direction.
        NBPIntersectionDirection penDir = getIntersectionDirection(kineticBox, staticBox);
        // Do collision resolution on X axis.
        if(kineticBox.getVelx() > 0){
            // The kinetic box entered the static one while moving left.
            if(penDir == NBPIntersectionDirection.SIDE_LEFT) {
                 kineticBox.setX(staticBox.getX() - kineticBox.getWidth());
                 // Bounce our object based on its restitution.
                 kineticBox.setVelx(-kineticBox.getVelx() * (kineticBox.getRestitution() + staticBox.getRestitution()));
            } else if(penDir == NBPIntersectionDirection.SIDE_RIGHT) {
                // We are moving away from the static box, give it a little push out.
                kineticBox.setX(staticBox.getX() + staticBox.getWidth());
            }
        } else if(kineticBox.getVelx() < 0) {
            // The kinetic box entered the static one while moving right.
            if(penDir == NBPIntersectionDirection.SIDE_RIGHT) {
                kineticBox.setX(staticBox.getX() + staticBox.getWidth());
                // Bounce our object based on its restitution.
                kineticBox.setVelx(-kineticBox.getVelx() * (kineticBox.getRestitution() + staticBox.getRestitution()));
            } else if(penDir == NBPIntersectionDirection.SIDE_LEFT) {
                // We are moving away from the static box, give it a little push out.
                kineticBox.setX(staticBox.getX() - kineticBox.getWidth());
            }
        }
        // Do collision resolution on Y axis.
        // TODO Will have to eventually add the box teleporting bug fix we applied on the X axis to this one (where we define top and bottom separately)
        if(kineticBox.getVely() > 0) {
            // Came from bottom
            if(penDir == NBPIntersectionDirection.TOP) {
                kineticBox.setY(staticBox.getY() - kineticBox.getHeight());
                // Bounce our object based on its restitution.
                kineticBox.setVely(-kineticBox.getVely() * (kineticBox.getRestitution() + staticBox.getRestitution()));
            } else if(penDir == NBPIntersectionDirection.BOTTOM) {
                // We are moving away from the static box, give it a little push out.
                kineticBox.setY(staticBox.getY() + staticBox.getHeight());
            }
        } else if(kineticBox.getVely() < 0) {
            // Came from top
            if(penDir == NBPIntersectionDirection.BOTTOM) {
                kineticBox.setY(staticBox.getY() + staticBox.getHeight());
                // Reduce X velocity based on friction.
                kineticBox.setVelx(kineticBox.getVelx() * (kineticBox.getFriction() + staticBox.getFriction()));
                // Bounce our object based on its restitution.
                kineticBox.setVely(-kineticBox.getVely() * (kineticBox.getRestitution() + staticBox.getRestitution()));
            } else if(penDir == NBPIntersectionDirection.TOP) {
                // We are moving away from the static box, give it a little push out.
                kineticBox.setY(staticBox.getY() - kineticBox.getHeight());
            }
        }
        // TODO Handle square intersections.
    }

    /**
     * Gets the direction at which a kinetic box enters a static one. 
     * @param kinetic box
     * @param static box
     * @return intersection direction
     */
    public static NBPIntersectionDirection getIntersectionDirection(NBPBox kin, NBPBox sta) {
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
        // Return which side the static box was penetrated from.
        if(intersectionHeight >= intersectionWidth) {
            // If kinetic center < static center then we entered on the left, otherwise right
            float kineticBoxCenter = kin.getX() + (kin.getWidth()/2);
            float staticBoxCenter  = sta.getX() + (sta.getWidth()/2);
            if(kineticBoxCenter < staticBoxCenter) {
                return NBPIntersectionDirection.SIDE_LEFT;
            } else {
                return NBPIntersectionDirection.SIDE_RIGHT;
            }
        } else if (intersectionHeight <= intersectionWidth) {
            // If kinetic center < static center then we entered from the top, otherwise bottom
            float kineticBoxCenter = kin.getY() + (kin.getHeight()/2);
            float staticBoxCenter  = sta.getY() + (sta.getHeight()/2);
            if(kineticBoxCenter < staticBoxCenter) {
                return NBPIntersectionDirection.TOP;
            } else {
                return NBPIntersectionDirection.BOTTOM;
            }
        } else {
            return NBPIntersectionDirection.EQUAL;
        }
    }
    
    /**
     * Resolves a collision between a kinetic and static box.
     * @param staticBox
     * @param kineticBox
     */
    private static void doCollisionResolutionViaSweep(NBPBox staticBox, NBPBox kineticBox) {
    	// Create a new box and set its height to match the size of staticbox enlarged by the kinematicbox.
    	NBPBox enlargedBox;
    	float enlargedBoxPosX = staticBox.getX() - (kineticBox.getWidth()/2f);
    	float enlargedBoxPosY = staticBox.getY() - (kineticBox.getHeight()/2f);
    	float enlargedBoxWidth = staticBox.getWidth();
    	float enlargedBoxHeight = staticBox.getHeight();
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
    	
    	// Go over each intersection point and ensure it is valid, it is only valid if it lies in the boundary of or movement line.
    	ListIterator<NBPIntersectionPoint> pointIterator = extendedBoxIntersectionPoints.listIterator();
    	while(pointIterator.hasNext()){
    		// Does this point reside in our box?
    		if(!doesPointResideInBoxDefinedByLine(pointIterator.next(), 
    				kineticBox.getLastOriginPoint(), kineticBox.getCurrentOriginPoint())) {
    			// This intersection is not valid.
    			pointIterator.remove();
    		}
    	}
    	
    	// ------------- TEMP!
    	System.out.println("POINTS NUM: " + extendedBoxIntersectionPoints.size());
    	for(NBPIntersectionPoint p : extendedBoxIntersectionPoints) {
    		System.out.println("INTERSECT DIR: " + p.getIntersectionDir());
    	}
    	// ------------- TEMP!
    	
    	// If we have only one point then great, that is the new origin of the kinematic box.
    	// Also, change box velocity based on intersection direction
    	
    	// If we have more than one point left then the one we want is the one that is closest to 
    	// the last origin of our moving box.
    	
    	// Careful as if we have two points that share the exact same position, then we entered 
    	// the static box exactly on a corner
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
     * 
     * @param P
     * @param points
     * @return closest point
     */
    public static NBPPoint getClosestPoint(NBPPoint P, ArrayList<NBPPoint> points) {
        return null;
    }
    
    /**
     * Checks to see if a point resides in a box that is defined by two other points.
     * @param P The point we are checking
     * @param LnPtA First point of our line
     * @param LnPtB Second point of our line
     * @return
     */
    public static boolean doesPointResideInBoxDefinedByLine(NBPPoint P, NBPPoint LnPtA, NBPPoint LnPtB) {
    	boolean inYBounds = (P.getY() >= Math.min(LnPtA.getY(), LnPtB.getY()) &&  
    			P.getY() <= Math.max(LnPtA.getY(), LnPtB.getY()));
    	boolean inXBounds = (P.getY() >= Math.min(LnPtA.getY(), LnPtB.getY()) &&  
    			P.getY() <= Math.max(LnPtA.getY(), LnPtB.getY()));
        return inYBounds && inXBounds;
    }
    
    
}
