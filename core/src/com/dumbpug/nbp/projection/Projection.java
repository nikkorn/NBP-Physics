package com.dumbpug.nbp.projection;

import com.dumbpug.nbp.AABB;

/**
 * A projection of an AABB in 2D/3D space.
 */
public class Projection extends AABB {
	/**
	 * The type of projection.
	 */
	private ProjectionType type;

	/**
	 * Creates a new instance of the Projection class.
	 * @param type
	 * @param x
	 * @param y
	 * @param z
	 * @param width
	 * @param height
	 * @param depth
	 */
	public Projection(ProjectionType type, float x, float y, float z, float width, float height, float depth) {
		super(x, y, z, width, height, depth);
	}
	
	/**
	 * Creates a new instance of the Projection class.
	 * @param type
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Projection(ProjectionType type, float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	/**
	 * Get the type of the projection.
	 * @return The type of the projection.
	 */
	public ProjectionType getProjectionType() {
		return this.type;
	}
}
