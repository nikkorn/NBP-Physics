package com.dumbpug.nbp.spatialgrid;

import java.util.ArrayList;
import java.util.HashSet;
import com.dumbpug.nbp.AABB;

/**
 * Represents a cell in a spatial grid.
 */
public class Cell<TAABB extends AABB> {
	/**
	 * The cell key.
	 */
	private String key;
	/**
	 * The list of AABBs in the cell.
	 */
	private ArrayList<TAABB> aabbList = new ArrayList<TAABB>();
	
	/**
	 * Create a new instance of the Cell class.
	 * @param key The cell key.
	 */
	public Cell(String key) {
		this.key = key;
	}
	
	/**
	 * Get the cell key.
	 * @return The cell key.
	 */
	public String getKey() {
		return this.key;
	}
	
	/**
	 * Add all of the AABBs that intersect the cell to the specified set.
	 * @param existing The set of existing AABBs.
	 */
	public void collect(HashSet<TAABB> existing) {
		existing.addAll(aabbList);
	}
	
	/**
	 * Add the specified AABB to this cell.
	 * @param aabb The AABB to add to this cell. 
	 */
	public void addAABB(TAABB aabb) {
		this.aabbList.add(aabb);
	}
	
	/**
	 * Remove the specified AABB from this cell.
	 * @param aabb The AABB to remove from this cell. 
	 */
	public void removeAABB(TAABB aabb) {
		this.aabbList.remove(aabb);
	}
}
