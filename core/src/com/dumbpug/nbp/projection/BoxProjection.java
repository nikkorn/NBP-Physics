package com.dumbpug.nbp.projection;

import com.dumbpug.nbp.Box;

/**
 * A projection of a box.
 */
public class BoxProjection extends Projection {
	/**
	 * The box that the projection represents.
	 */
	private Box box;

	/**
	 * Create a new instance of the BoxProjection class.
	 * @param box The box that the projection represents.
	 * @param x
	 * @param y
	 * @param z
	 * @param width
	 * @param height
	 * @param depth
	 */
	public BoxProjection(Box box, float x, float y, float z, float width, float height, float depth) {
		super(ProjectionType.BOX, x, y, z, width, height, depth);
	}
	
	/**
	 * Create a new instance of the BoxProjection class.
	 * @param box The box that the projection represents.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public BoxProjection(Box box, float x, float y, float width, float height) {
		super(ProjectionType.BOX, x, y, width, height);
	}
	
	/**
	 * Get the box that the projection represents.
	 * @return The box that the projection represents.
	 */
	public Box getProjectedBox() {
		return this.box;
	}
}
