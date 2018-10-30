package com.dumbpug.nbp.zone;

import com.dumbpug.nbp.Axis;
import com.dumbpug.nbp.Box;

/**
 * Represents a square zone of force pushing in a specified direction.
 */
public class SquareZone extends Zone {
    /**
     * The width/height of the zone.
     */
    private float width, height;
    /**
     * The axis on which the force is to be applied.
     */
    private Axis axis;

    /**
     * Create a new instance of the SquareZone class.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param force
     * @param direction
     */
    public SquareZone(float x, float y, float width, float height, float force, Axis axis) {
        super(x, y, force);
        this.width  = width;
        this.height = height;
        this.axis   = axis;
    }

    /**
     * Get the width of the zone.
     * @return The width.
     */
    public float getWidth() {
        return width;
    }

    /**
     * Set the width of the zone.
     * @param width The width.
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * Get the height of the zone.
     * @return The height.
     */
    public float getHeight() {
        return height;
    }

    /**
     * Set the height of the zone.
     * @param height The height.
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Get the direction of the force to be applied.
     * @return The direction of the force to be applied.
     */
    public Axis getAxis() {
        return axis;
    }

    @Override
    public boolean intersects(Box box) {
       // TODO
    	return false;
    }

    @Override
    public void influence(Box box) {
        // TODO
    }
}