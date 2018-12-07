package com.dumbpug.nbp.spatialgrid;

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
	 * The list of cells that the AABB overlaps.
	 */
	private ArrayList<Cell> cells = new ArrayList<Cell>();
	
	/**
	 * Create a new instance of the SpatiallyPoisitionedAABB class.
	 * @param aabb The AABB.
	 */
	public SpatiallyPoisitionedAABB(AABB aabb) {
		this.aabb = aabb;
	}
	
	/**
	 * Get the list of cells that the AABB overlaps.
	 * @return The list of cells that the AABB overlaps.
	 */
	public ArrayList<Cell> getCells() {
		return this.cells;
	}
	
	/**
	 * Add a cell to the list of cells that the AABB overlaps.
	 * @param cell The cell to add.
	 */
	public void addCell(Cell cell) {
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
	public AABB getAABB() {
		return this.aabb;
	}
}
