package com.dumbpug.nbp.spatialgriddynamic;

import com.dumbpug.nbp.AABB;
import com.dumbpug.nbp.Axis;

/**
 * The origin of an AABB.
 */
public class Origin {
	/**
	 * The AABB for which this is the origin.
	 */
	private AABB aabb;
	
	/**
	 * Create a new instance of the Origin
	 * @param aabb The AABB for which this is the origin.
	 */
	public Origin(AABB aabb) {
		this.aabb = aabb;
	}
	
	/**
	 * Get the X position of this origin.
	 * @return The x position.
	 */
	public float getX() {
		return this.aabb.getPosition(Axis.X) + (this.aabb.getLength(Axis.X) / 2f);
	}

	/**
	 * Get the Y position of this origin.
	 * @return The y position.
	 */
	public float getY() {
		return this.aabb.getPosition(Axis.Y) + (this.aabb.getLength(Axis.Y) / 2f);
	}

	/**
	 * Get the Z position of this origin.
	 * @return The Z position.
	 */
	public float getZ() {
		return this.aabb.getPosition(Axis.Z) + (this.aabb.getLength(Axis.Z) / 2f);
	}
}
