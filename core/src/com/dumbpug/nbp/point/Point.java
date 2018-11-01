package com.dumbpug.nbp.point;

import com.dumbpug.nbp.Dimension;

/**
 * Defines an x/y point in a physics world.
 */
public class Point {
    /**
     * The position of the point.
     */
    private float x, y, z = 0f;
    /**
     * The dimension of the point.
     */
    private Dimension dimension;

    /**
     * Create a new instance of a point in 2D space.
     * @param x The X position.
     * @param y The Y position.
     */
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
        this.dimension = Dimension.TWO_DIMENSIONS;
    }

    /**
     * Create a new instance of a point in 3D space.
     * @param x The X position.
     * @param y The Y position.
     * @param z The Z position.
     */
    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = Dimension.THREE_DIMENSIONS;
    }

    /**
     * Get the X position of this point.
     * @return The X position of this point.
     */
    public float getX() { return x; }

    /**
     * Set the X position of this point.
     * @param x The X position of this point.
     */
    public void setX(float x) { this.x = x; }

    /**
     * Get the Y position of this point.
     * @return The Y position of this point.
     */
    public float getY() { return y; }

    /**
     * Set the Y position of this point.
     * @param y The Y position of this point.
     */
    public void setY(float y) { this.y = y; }

    /**
     * Get the Z position of this point.
     * @return The Z position of this point.
     */
    public float getZ() { return z; }

    /**
     * Set the Z position of this point.
     * @param z The Z position of this point.
     */
    public void setZ(float z) { this.z = z; }

    /**
     * Get the dimension of this point.
     * @return The dimension of this point.
     */
    public Dimension getDimension() { return this.dimension; }
    
    /**
     * Get a clone of this point.
     * @return A clone of this point. 
     */
    public Point clone() {
    	if (this.dimension == Dimension.THREE_DIMENSIONS) {
    		return new Point(this.x, this.y, this.z);
    	} else {
    		return new Point(this.x, this.y);
    	}
    }
}
