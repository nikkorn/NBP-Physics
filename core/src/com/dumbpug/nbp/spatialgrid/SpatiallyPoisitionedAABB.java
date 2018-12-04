package com.dumbpug.nbp.spatialgrid;

import com.dumbpug.nbp.AABB;

/**
 * A spatially positioned AABB.
 */
public class SpatiallyPoisitionedAABB {
	/**
	 * The AABB.
	 */
	private AABB aabb;
	/**
	 * The origin of the AABB.
	 */
	private Origin origin;
	/**
	 * The cell key.
	 */
	private String cellKey;
	
	/**
	 * Create a new instance of the SpatiallyPoisitionedAABB class.
	 * @param aabb The AABB.
	 * @param origin the AABB origin.
	 * @param cellKey The initial cell key.
	 */
	public SpatiallyPoisitionedAABB(AABB aabb, Origin origin, String cellKey) {
		this.aabb    = aabb;
		this.origin  = origin;
		this.cellKey = cellKey;
	}
	
	/**
	 * Get the cell key.
	 * @return The cell key
	 */
	public String getCellKey() {
		return this.cellKey;
	}
	
	/**
	 * Set the cell key.
	 * @param cellKey The cell key.
	 */
	public void setCellKey(String cellKey) {
		this.cellKey = cellKey;
	}
	
	/**
	 * Get the origin of the AABB.
	 * @return The origin of the AABB.
	 */
	public Origin getAABBOrigin() {
		return this.origin;
	}
	
	/**
	 * Get the AABB.
	 * @return The AABB.
	 */
	public AABB getAABB() {
		return this.aabb;
	}
}
