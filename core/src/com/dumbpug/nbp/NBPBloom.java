package com.dumbpug.nbp;

/**
 * Represents a force bloom, place it anywhere in the physics world to create a force explosion.
 * @author nikolas.howard
 *
 */
public class NBPBloom {
	// Radius of the bloom.
	private float radius;
	// Force of bloom.
	private float force;
	// Position X of bloom.
	private float x;
	// Position Y of bloom.
	private float y;
	
	/**
	 * Create a new instance of the NBPBloom class.
	 * @param x
	 * @param y
	 * @param radius
	 * @param force
	 */
	public NBPBloom(float x, float y, float radius, float force) {
		this.setY(y);
		this.setX(x);
		this.setRadius(radius);
		this.setForce(force);
	}

	/**
	 * Get radius.
	 * @return radius
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * Set radius.
	 * @param radius
	 */
	public void setRadius(float radius) {
		this.radius = radius;
	}

	/**
	 * Get force.
	 * @return force.
	 */
	public float getForce() {
		return force;
	}

	/**
	 * Set force.
	 * @param force
	 */
	public void setForce(float force) {
		this.force = force;
	}

	/**
	 * Get X position.
	 * @return x
	 */
	public float getX() {
		return x;
	}

	/**
	 * Set X position.
	 * @param x
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Get Y position.
	 * @return y
	 */
	public float getY() {
		return y;
	}

	/**
	 * Set Y position.
	 * @param y
	 */
	public void setY(float y) {
		this.y = y;
	}
}
