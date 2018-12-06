package com.dumbpug.nbp.spatialgridfixed;

import java.util.HashMap;
import java.util.HashSet;
import com.dumbpug.nbp.AABB;
import com.dumbpug.nbp.Dimension;

/**
 * Represents a spatial grid in which AABBs can be grouped into cells to facilitate broad phase collisions.
 */
public class SpatialGrid {
	/**
	 * The dimension of the grid.
	 */
	private Dimension dimension;
	/**
	 * The cell size.
	 */
	private float cellSize;
	/**
	 * The map of grid cells to the list of AABBs which have their origin residing in the cell.
	 */
	private HashMap<String, Cell> grid = new HashMap<String, Cell>();
	/**
	 * The list of AABBs to lay out in the grid.
	 */
	private HashMap<AABB, SpatiallyPoisitionedAABB> AABBToSpatialAABBMap = new HashMap<AABB, SpatiallyPoisitionedAABB>();
	
	/**
	 * Create a new instance of the SpatialGrid class.
	 * @param dimension The dimension of the grid.
	 * @param cellSize The size of cell to use in the spatial grid.
	 */
	public SpatialGrid(Dimension dimension, float cellSize) {
		this.dimension = dimension;
		this.cellSize  = cellSize;
	}
	
	/**
	 * Add an AABB to the grid.
	 * @param aabb The aabb to add.
	 */
	public void add(AABB aabb) {
		// If this AABB has already been added then there is nothing to do.
		if (this.AABBToSpatialAABBMap.containsKey(aabb)) {
			return;
		}
		
		// TODO Update the positioned AABB so that it is placed into its relevant cells.
		
		// Add the new AABB to the map of AABBs to their spatially positioned representations.
		this.AABBToSpatialAABBMap.put(aabb, new SpatiallyPoisitionedAABB(aabb));
	}
	
	/**
	 * Remove an AABB from the grid.
	 * @param aabb The aabb to remove.
	 */
	public void remove(AABB aabb) {
		// If this AABB does not exist in the grid then there is nothing to do.
		if (!AABBToSpatialAABBMap.containsKey(aabb)) {
			return;
		}
		
		// Get the spatially positioned AABB for the AABB.
		SpatiallyPoisitionedAABB spatiallyPositionedAABB = this.AABBToSpatialAABBMap.get(aabb);
		
		// Remove the positioned AABB from ever cell that it is in via spatiallyPositionedAABB.getCellKeys().
		for (String cellKey : spatiallyPositionedAABB.getCellKeys()) {
			this.getCell(cellKey).removeAABB(aabb);
		}
		
		// Remove the AABB from the AABB to Spatial AABB mapping.
		this.AABBToSpatialAABBMap.remove(aabb);
	}
	
	/**
	 * Update an AABB in the grid.
	 * 
	 * TODO Remove in favour or a per-AABB update!
	 * 
	 * @param aabb The aabb to update.
	 */
	public void update() {
		// Clear the grid.
		this.grid.clear();
		
		// How we add the AABBs to the grid depends on the dimension we are dealing with.
		if (this.dimension == Dimension.TWO_DIMENSIONS) {
			// For each AABB we will need to find every cell that it resides in.
			for (SpatiallyPoisitionedAABB positionedAABB : this.AABBToSpatialAABBMap.values()) {
				// Clear any existing cell keys that the positioned AABB has.
				positionedAABB.clearCellKeys();
				
				// Get the AABB.
				AABB aabb = positionedAABB.getAABB();
				
				// Get the cell bounds of the AABB.
				int xStartCell = this.getCellPosition(aabb.getX());
				int xEndCell   = this.getCellPosition(aabb.getX() + aabb.getWidth());
				int yStartCell = this.getCellPosition(aabb.getY());
				int yEndCell   = this.getCellPosition(aabb.getY() + aabb.getHeight());
				
				// Find all cells that the AABB intersects and record the AABB against each one.
				for (int cellX = xStartCell; cellX <= xEndCell; cellX++) {
					for (int cellY = yStartCell; cellY <= yEndCell; cellY++) {
						// Get the cell at the current position.
						Cell cell = this.getCell(cellX, cellY);
						
						// Add the AABB to the cell.
						cell.addAABB(aabb);
						
						// Add the cell key to the list of keys representing cells that the AABB intersects for quick lookup.
						positionedAABB.addCellKey(cell.getKey());
					}
				}
			}
		} else {
			// For each AABB we will need to find every cell that it resides in.
			for (SpatiallyPoisitionedAABB positionedAABB : this.AABBToSpatialAABBMap.values()) {
				// Clear any existing cell keys that the positioned AABB has.
				positionedAABB.clearCellKeys();
				
				// Get the AABB.
				AABB aabb = positionedAABB.getAABB();
				
				// Get the cell bounds of the AABB.
				int xStartCell = this.getCellPosition(aabb.getX());
				int xEndCell   = this.getCellPosition(aabb.getX() + aabb.getWidth());
				int yStartCell = this.getCellPosition(aabb.getY());
				int yEndCell   = this.getCellPosition(aabb.getY() + aabb.getHeight());
				int zStartCell = this.getCellPosition(aabb.getZ());
				int zEndCell   = this.getCellPosition(aabb.getZ() + aabb.getDepth());
				
				// Find all cells that the AABB intersects and record the AABB against each one.
				for (int cellX = xStartCell; cellX <= xEndCell; cellX++) {
					for (int cellY = yStartCell; cellY <= yEndCell; cellY++) {
						for (int cellZ = zStartCell; cellZ <= zEndCell; cellZ++) {
							// Get the cell at the current position.
							Cell cell = this.getCell(cellX, cellY, cellZ);
							
							// Add the AABB to the cell.
							cell.addAABB(aabb);
							
							// Add the cell key to the list of keys representing cells that the AABB intersects for quick lookup.
							positionedAABB.addCellKey(cell.getKey());
						}
					}
				}
			}
		}
	}
	
	/**
	 * Get a set of AABBs that reside in the current or adjacent cells to the specified one excluding it.
	 * @param aabb The AABB for which to find a set of AABBs that reside in the current or adjacent cells to it.
	 * @return A set of AABBs that reside in the current or adjacent cells to the specified one excluding it.
	 */
	public HashSet<AABB> getNeighbouringAABBs(AABB aabb) {
		// Get the spatially positioned AABB for the AABB.
		SpatiallyPoisitionedAABB spatiallyPositionedAABB = this.AABBToSpatialAABBMap.get(aabb);
		
		// Create an empty set in which to add all neighbouring AABBs.
		HashSet<AABB> neighbouringAABBs = new HashSet<AABB>();
		
		// For each cell key that the AABB overlaps, add every AABB that overlaps that cell into the neighbouring set.
		for (String cellKey : spatiallyPositionedAABB.getCellKeys()) {
			// Get the grid cell.
			Cell cell = this.grid.get(cellKey);
			
			// Add every AABB that overlaps the cell to our set.
			cell.collect(neighbouringAABBs);
		}
		
		// Remove the specified AABB from the set.
		neighbouringAABBs.remove(aabb);
		
		// Return the list of neighbouring AABBs.
		return neighbouringAABBs;
	}
	
	/**
	 * Get the existing cell at the specified position.
	 * @param x The x position of the cell.
	 * @param y The y position of the cell.
	 * @return The existing cell at the specified position.
	 */
	private Cell getCell(int x, int y) {
		// Create the cell key.
		String cellKey = x + "_" + y;
		
		// Return the cell.
		return this.getCell(cellKey);
	}
	
	/**
	 * Get the existing cell at the specified position.
	 * @param x The x position of the cell.
	 * @param y The y position of the cell.
	 * @param z The z position of the cell.
	 * @return The existing cell at the specified position.
	 */
	private Cell getCell(int x, int y, int z) {
		// Create the cell key.
		String cellKey = x + "_" + y + "_" + z;
		
		// Return the cell.
		return this.getCell(cellKey);
	}
	
	/**
	 * Get the existing cell with the specified key, or create one if it does not already exist.
	 * @param key The cell key.
	 * @return The existing cell with the specified key, or create one if it does not already exist.
	 */
	private Cell getCell(String key) {
		// Try to get the cell using the key.
		Cell cell = this.grid.get(key);
		
		// If the cell does not exist then we must create one and add it to our grid.
		if (cell == null) {
			// Create a new cell...
			cell = new Cell(key);
			
			// ... and add it to the grid.
			this.grid.put(key, cell);
		}
		
		// Return the cell.
		return cell;
	}
	
	/**
	 * Get the cell position based on an absolute world position. 
	 * @param value The absolute world position.
	 * @return The cell position based on an absolute world position. 
	 */
	private int getCellPosition(double value) {
		return (int) Math.floor(value / this.cellSize);
	}
}
