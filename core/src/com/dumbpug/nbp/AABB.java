package com.dumbpug.nbp;

/**
 * A 2D/3D AABB.
 */
public class AABB {
	/**
	 * The position of the AABB.
	 */
	private float x, y, z;
	/**
	 * The size of the AABB.
	 */
	private float width, height, depth;
	/**
	 * The dimension of the AABB.
	 */
	private Dimension dimension;

	/**
	 * Creates a new instance of a 2D AABB.
	 * 
	 * @param x
	 *            The X position of the AABB.
	 * @param y
	 *            The Y position of the AABB.
	 * @param width
	 *            The width of the AABB.
	 * @param height
	 *            The height of the AABB.
	 */
	public AABB(float x, float y, float width, float height) {
		this.x         = x;
		this.y         = y;
		this.width     = width;
		this.height    = height;
		this.dimension = Dimension.TWO_DIMENSIONS;
	}

	/**
	 * Creates a new instance of a 3D AABB.
	 * 
	 * @param x
	 *            The X position of the AABB.
	 * @param y
	 *            The Y position of the AABB.
	 * @param z
	 *            The Z position of the AABB.
	 * @param width
	 *            The width of the AABB.
	 * @param height
	 *            The height of the AABB.
	 * @param depth
	 *            The depth of the AABB.
	 */
	public AABB(float x, float y, float z, float width, float height, float depth) {
		this.x         = x;
		this.y         = y;
		this.z         = z;
		this.width     = width;
		this.height    = height;
		this.depth     = depth;
		this.dimension = Dimension.THREE_DIMENSIONS;
	}

	/**
	 * Get the X position of this AABB.
	 * 
	 * @return The x position.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Set the X position of this AABB.
	 * 
	 * @param x
	 *            The X position of this AABB.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Get the Y position of this AABB.
	 * 
	 * @return The y position.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Set the Y position of this AABB.
	 * 
	 * @param y
	 *            The Y position of this AABB.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Get the Z position of this AABB.
	 * 
	 * @return The Z position.
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Set the Z position of this AABB.
	 * 
	 * @param z
	 *            The Z position of this AABB.
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Get the width of this AABB.
	 * 
	 * @return The width of this AABB.
	 */
	public float getWidth() {
		return this.width;
	}

	/**
	 * Get the height of this AABB.
	 * 
	 * @return The height of this AABB.
	 */
	public float getHeight() {
		return this.height;
	}

	/**
	 * Get the depth of this AABB.
	 * 
	 * @return The depth of this AABB.
	 */
	public float getDepth() {
		return this.depth;
	}

	/**
	 * Get the dimension of this AABB.
	 * 
	 * @return The dimension of this AABB.
	 */
	public Dimension getDimension() {
		return this.dimension;
	}
}
