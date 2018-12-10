package com.dumbpug.nbp.spatialgrid;

import java.util.ArrayList;
import com.dumbpug.nbp.AABB;

/**
 * A spatially positioned AABB.
 */
public class SpatiallyPoisitionedAABB<TAABB extends AABB> {
	/**
	 * The AABB.
	 */
	private TAABB aabb;
	/**
	 * The list of cells that the AABB overlaps.
	 */
	private ArrayList<Cell<TAABB>> cells = new ArrayList<Cell<TAABB>>();
	
	/**
	 * Create a new instance of the SpatiallyPoisitionedAABB class.
	 * @param aabb The AABB.
	 */
	public SpatiallyPoisitionedAABB(TAABB aabb) {
		this.aabb = aabb;
	}
	
	/**
	 * Get the list of cells that the AABB overlaps.
	 * @return The list of cells that the AABB overlaps.
	 */
	public ArrayList<Cell<TAABB>> getCells() {
		return this.cells;
	}
	
	/**
	 * Add a cell to the list of cells that the AABB overlaps.
	 * @param cell The cell to add.
	 */
	public void addCell(Cell<TAABB> cell) {
		this.cells.add(cell);
	}
	
	/**
	 * Clear the list of cells that the AABB overlaps.
	 */
	public void clearCells() {
		this.cells.clear();
	}
	
	/**
	 * Get the AABB.
	 * @return The AABB.
	 */
	public TAABB getAABB() {
		return this.aabb;
	}
}
