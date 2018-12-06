package com.dumbpug.nbp.spatialgriddynamic;

import java.util.ArrayList;
import com.dumbpug.nbp.AABB;

/**
 * Represents a cell in a spatial grid.
 */
public class Cell {
	/**
	 * The cell key.
	 */
	private String key;
	/**
	 * The position of the cell.
	 */
	private int x, y, z;
	/**
	 * The list of AABBs in the cell.
	 */
	private ArrayList<AABB> aabbList = new ArrayList<AABB>();
	
	/**
	 * Create a new instance of the Cell class.
	 * @param key The cell key.
	 * @param x The x posititon of the cell.
	 * @param y The y posititon of the cell.
	 */
	public Cell(String key, int x, int y) {
		this.key = key;
		this.x   = x;
		this.y   = y;
	}
	
	/**
	 * Create a new instance of the Cell class.
	 * @param key The cell key.
	 * @param x The x posititon of the cell.
	 * @param y The y posititon of the cell.
	 * @param z The z posititon of the cell.
	 */
	public Cell(String key, int x, int y, int z) {
		this.key = key;
		this.x   = x;
		this.y   = y;
		this.z   = z;
	}
	
	/**
	 * Get the cell key.
	 * @return The cell key.
	 */
	public String getKey() {
		return this.key;
	}
	
	/**
	 * Get the X position of this cell.
	 * @return The x position.
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Get the Y position of this cell.
	 * @return The y position.
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Get the Z position of this cell.
	 * @return The Z position.
	 */
	public int getZ() {
		return this.z;
	}
	
	/**
	 * Get the list of AABBs in this cell.
	 * @return The list of AABBs in this cell.
	 */
	public ArrayList<AABB> getAABBList() {
		return this.aabbList;
	}
	
	/**
	 * Add the specified AABB to this cell.
	 * @param aabb The AABB to add to this cell. 
	 */
	public void addAABB(AABB aabb) {
		this.aabbList.add(aabb);
	}
	
	/**
	 * Remove the specified AABB from this cell. 
	 * @param aabb The AABB to remove from this cell. 
	 */
	public void removeAABB(AABB aabb) {
		this.aabbList.remove(aabb);
	}
}
