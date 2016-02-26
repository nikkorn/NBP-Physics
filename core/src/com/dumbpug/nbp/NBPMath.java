package com.dumbpug.nbp;

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
        TOP_BOTTOM,
        SIDE_LEFT,
        SIDE_RIGHT,
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
            	 kineticBox.setVelx(0);
                 kineticBox.setX(staticBox.getX() - kineticBox.getWidth());
            } else if(penDir == NBPIntersectionDirection.SIDE_RIGHT) {
            	// We are moving away from the static box, give it a little push out.
            	kineticBox.setX(staticBox.getX() + staticBox.getWidth());
            }
        } else if(kineticBox.getVelx() < 0) {
        	// The kinetic box entered the static one while moving right.
            if(penDir == NBPIntersectionDirection.SIDE_RIGHT) {
            	kineticBox.setVelx(0);
                kineticBox.setX(staticBox.getX() + staticBox.getWidth());
            } else if(penDir == NBPIntersectionDirection.SIDE_LEFT) {
            	// We are moving away from the static box, give it a little push out.
            	kineticBox.setX(staticBox.getX() - kineticBox.getWidth());
            }
        }
        // Do collision resolution on Y axis.
        if(kineticBox.getVely() > 0) {
            // Came from bottom
            if(penDir == NBPIntersectionDirection.TOP_BOTTOM) {
                kineticBox.setVely(0);
                kineticBox.setY(staticBox.getY() - kineticBox.getHeight());
            }
        } else if(kineticBox.getVely() < 0) {
            // Came from top
            if(penDir == NBPIntersectionDirection.TOP_BOTTOM) {
                kineticBox.setVely(0);
                kineticBox.setY(staticBox.getY() + staticBox.getHeight());
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
        if(intersectionHeight > intersectionWidth) {
        	// If kinetic center < static center then we entered on the left, otherwise right
        	float kineticBoxCenter = kin.getX() + (kin.getWidth()/2);
        	float staticBoxCenter  = sta.getX() + (sta.getWidth()/2);
        	if(kineticBoxCenter < staticBoxCenter) {
        		return NBPIntersectionDirection.SIDE_LEFT;
        	} else {
        		return NBPIntersectionDirection.SIDE_RIGHT;
        	}
        } else if (intersectionHeight < intersectionWidth) {
            return NBPIntersectionDirection.TOP_BOTTOM;
        } else {
            return NBPIntersectionDirection.EQUAL;
        }
    }
}
