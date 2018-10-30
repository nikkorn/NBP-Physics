package com.dumbpug.nbp;

/**
 * Represents a force bloom, place it anywhere in the physics world to create a force explosion.
 */
public class Bloom {
    /**
     * The radius of the bloom.
     */
    private float radius;
    /**
     * The force of the bloom.
     */
    private float force;
    /**
     * The position of the bloom.
     */
    private float x, y;

    /**
     * Create a new instance of the Bloom class.
     * @param x The X position of the bloom.
     * @param y The Y position of the bloom.
     * @param radius The radius of the bloom.
     * @param force The force of the bloom.
     */
    public Bloom(float x, float y, float radius, float force) {
        this.setY(y);
        this.setX(x);
        this.setRadius(radius);
        this.setForce(force);
    }

    /**
     * Get the radius of the bloom.
     * @return The radius.
     */
    public float getRadius() {
        return radius;
    }

    /**
     * Set the radius of the bloom.
     * @param radius The radius.
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    /**
     * Get the force of the bloom.
     * @return The force.
     */
    public float getForce() {
        return force;
    }

    /**
     * Set the force of the bloom.
     * @param force The force.
     */
    public void setForce(float force) {
        this.force = force;
    }

    /**
     * Get the X position.
     * @return The x position.
     */
    public float getX() {
        return x;
    }

    /**
     * Set the X position.
     * @param x The x position.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Get the Y position.
     * @return The y position.
     */
    public float getY() {
        return y;
    }

    /**
     * Set the Y position.
     * @param y The y position.
     */
    public void setY(float y) {
        this.y = y;
    }
}
