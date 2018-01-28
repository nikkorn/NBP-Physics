package com.nbp.point;

/**
 * Defines an x/y point in a physics world.
 */
public class Point {
    /**
     * The position of the point.
     */
    private float x, y = 0f;

    /**
     * Create a new instance of the Point class.
     * @param x The X position.
     * @param y The Y position.
     */
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the X position of this point.
     * @return The X position of this point.
     */
    public float getX() {
        return x;
    }

    /**
     * Set the X position of this point.
     * @param x The X position of this point.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Get the Y position of this point.
     * @return The Y position of this point.
     */
    public float getY() {
        return y;
    }

    /**
     * Set the Y position of this point.
     * @param y The Y position of this point.
     */
    public void setY(float y) {
        this.y = y;
    }
}
