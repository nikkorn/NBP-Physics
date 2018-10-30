package com.dumbpug.nbp.zone;

import com.dumbpug.nbp.Box;
import com.dumbpug.nbp.NBPMath;

/**
 * Represents a circular zone of force pushing outwards from its origin.
 */
public class CircleZone extends Zone {
    /**
     * The radius of the circle zone.
     */
    private float radius;

    /**
     * Create a new instance of the CircleZone class.
     * @param x
     * @param y
     * @param force
     * @param radius
     */
    public CircleZone(float x, float y, float force, float radius) {
        super(x, y, force);
        this.radius = radius;
    }

    /**
     * Get the radius of this zone.
     * @return The radius of the zone.
     */
    public float getRadius() {
        return radius;
    }

    /**
     * Set the radius of this zone.
     * @param radius The radius of the zone.
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public boolean intersects(Box box) {
        // Get the distance between the position of this zone (its centre) and the origin of the specified box.
        float distance = NBPMath.getDistanceBetweenPoints(this.getPosition(), box.getCurrentOriginPoint());
        // Return whether the box is even in the range of this zone.
        return distance <= this.getRadius();
    }

    @Override
    public void influence(Box box) {
        // Get the angle of difference between our zone position and the intersecting box.
        float angleBetweenBloomAndBox = NBPMath.getAngleBetweenPoints(box.getCurrentOriginPoint(), this.getPosition());
        // Apply the force of the zone to the intersecting box at the angle of difference.
        box.applyVelocityInDirection(angleBetweenBloomAndBox, this.getForce());
    }
}