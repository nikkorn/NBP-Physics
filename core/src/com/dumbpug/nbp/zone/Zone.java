package com.dumbpug.nbp.zone;

import com.dumbpug.nbp.Box;
import com.dumbpug.nbp.point.Point;

/**
 * Represents an area in a physics environment where a continuous force influences overlapping dynamic objects.
 */
public abstract class Zone {
    /**
     * The position point of the zone.
     */
    private Point position;
    /**
     * The amount of force to apply to intersecting physics boxes.
     */
    private float force;

    /**
     * Create a new instance of the Zone class.
     * @param x     The X position of the zone.
     * @param y     The Y position of the zone.
     * @param force The force to apply to intersecting objects.
     */
    public Zone(float x, float y, float force) {
        this.position = new Point(x, y);
        this.force    = force;
    }

    /**
     * Get the position of this zone.
     * @return The zone position.
     */
    public Point getPosition() {
        return this.position;
    }

    /**
     * Get the force that is applied to intersecting physics boxes.
     * @return force.
     */
    public float getForce() {
        return force;
    }

    /**
     * Set the force that is applied to intersecting physics boxes.
     * @param force
     */
    public void setForce(float force) {
        this.force = force;
    }

    /**
     * Determine whether the specified physics box intersects with this zone of force.
     * @param box The physics box to check for intersection.
     * @return Whether the specified physics box intersects with this zone of force.
     */
    public abstract boolean intersects(Box box);

    /**
     * Apply a force to an overlapping physics box.
     * @param box The physics box to influence.
     */
    public abstract void influence(Box box);
}
