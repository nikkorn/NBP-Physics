package com.dumbpug.nbp.spatialgridfixed;

import java.util.ArrayList;
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
	 * The list of keys of cells that the AABB overlaps.
	 */
	private ArrayList<String> cellKeys = new ArrayList<String>();
	
	/**
	 * Create a new instance of the SpatiallyPoisitionedAABB class.
	 * @param aabb The AABB.
	 */
	public SpatiallyPoisitionedAABB(AABB aabb) {
		this.aabb = aabb;
	}
	
	/**
	 * Get the list of keys of cells that the AABB overlaps.
	 * @return The list of keys of cells that the AABB overlaps.
	 */
	public ArrayList<String> getCellKeys() {
		return this.cellKeys;
	}
	
	/**
	 * Add a cell key to list of cells that the AABB overlaps.
	 * @param cellKey The cell key to add.
	 */
	public void addCellKey(String cellKey) {
		this.cellKeys.add(cellKey);
	}
	
	/**
	 * Clear the list of keys of cells that the AABB overlaps.
	 */
	public void clearCellKeys() {
		this.cellKeys.clear();
	}
	
	/**
	 * Get the AABB.
	 * @return The AABB.
	 */
	public AABB getAABB() {
		return this.aabb;
	}
}
