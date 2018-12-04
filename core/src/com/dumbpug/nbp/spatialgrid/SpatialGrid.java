package com.dumbpug.nbp.spatialgrid;

import java.util.ArrayList;
import java.util.HashMap;
import com.dumbpug.nbp.AABB;
import com.dumbpug.nbp.Axis;
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
	 * Whether the cell sizes can change dynamically.
	 */
	private boolean isCellSizeDynamic = true;
	/**
	 * The cell size to use, this can change dynamically to match the greatest size of AABB.
	 */
	private float cellWidth = 0f, cellHeight = 0f, cellDepth = 0f;
	/**
	 * The map of grid cells to the list of AABBs which have their origin residing in the cell.
	 */
	private HashMap<String, ArrayList<AABB>> grid = new HashMap<String, ArrayList<AABB>>();
	/**
	 * The map of AABBs to the key of which cell the AABB is in.
	 */
	private HashMap<AABB, SpatiallyPoisitionedAABB> AABBToSpatialAABBMap = new HashMap<AABB, SpatiallyPoisitionedAABB>();
	
	/**
	 * Create a new instance of the SpatialGrid class.
	 * @param dimension The dimension of the grid.
	 */
	public SpatialGrid(Dimension dimension) {
		this.dimension = dimension;
	}
	
	/**
	 * Create a new instance of the SpatialGrid class.
	 * @param dimension The dimension of the grid.
	 * @param width The static width of cells to use in the grid.
	 * @param height The static height of cells to use in the grid.
	 */
	public SpatialGrid(Dimension dimension, float width, float height) {
		this(dimension);
		// We have specified a static 2D cell size, so cell sizes are not to be updated dynamically.
		this.isCellSizeDynamic = false;
		// Set the static width and height of the cells.
		this.cellWidth  = width;
		this.cellHeight = height;
	}
	
	/**
	 * Create a new instance of the SpatialGrid class.
	 * @param dimension The dimension of the grid.
	 * @param width The static width of cells to use in the grid.
	 * @param height The static height of cells to use in the grid.
	 * @param depth The static depth of cells to use in the grid.
	 */
	public SpatialGrid(Dimension dimension, float width, float height, float depth) {
		this(dimension, width, height);
		// We have specified a static 3D cell size, so cell sizes are not to be updated dynamically.
		this.isCellSizeDynamic = false;
		// Set the static width and height of the cells.
		this.cellDepth = depth;
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
		// Create a representation of the origin of the AABB.
		Origin aabbOrigin = new Origin(aabb);
		// The AABB is not already in the grid, get a key for it based on the AABBs origin.
		String cellKey = this.getCellKey(aabbOrigin);
		// Add the new aabb to the map of aabbs to their spatially positioned representations.
		this.AABBToSpatialAABBMap.put(aabb, new SpatiallyPoisitionedAABB(aabb, aabbOrigin, cellKey));
		// Add the new aabb to the spatial grid.
		
		// TODO We will need to add a Cell if it does not exist yet.
		
		this.grid.get(cellKey).add(aabb);
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
		// Get the spatially positioned AABB.
		SpatiallyPoisitionedAABB spatiallyPositionedAABB = this.AABBToSpatialAABBMap.get(aabb);
		// Get the key of the cell that the aabb was last in.
		String originalCellKey = spatiallyPositionedAABB.getCellKey();
		// Remove the AABB from its current cell list.
		this.grid.get(originalCellKey).remove(aabb);
		// Finally, remove the AABB from the AABB to Spatial AABB mapping.
		this.AABBToSpatialAABBMap.remove(aabb);
	}
	
	/**
	 * Update an AABB in the grid.
	 * @param aabb The aabb to update.
	 */
	public void update(AABB aabb) {
		// If this AABB does not exist in the grid then there is nothing to do.
		if (!this.AABBToSpatialAABBMap.containsKey(aabb)) {
			return;
		}
		// Get the spatially positioned AABB.
		SpatiallyPoisitionedAABB spatiallyPositionedAABB = this.AABBToSpatialAABBMap.get(aabb);
		// Get the key of the cell that the aabb was last in.
		String originalCellKey = spatiallyPositionedAABB.getCellKey();
		// The AABB is not already in the grid, get a key for it.
		String currentCellKey = this.getCellKey(spatiallyPositionedAABB.getAABBOrigin());
		// If the original and current cell keys do not differ then the AABB is still in the correct cell.
		if (originalCellKey == currentCellKey) {
			return;
		}
		// The AABB has moved into a different cell since its last update.
		// Firstly, We will need to physically remove the AABB from its original grid cell list.
		this.grid.get(originalCellKey).remove(aabb);
		// Secondly, we need to move it into the grid cell list that it should be in.
		
		// TODO WE will need to add a Cell if it does not exist yet.
		
		this.grid.get(currentCellKey).add(aabb);
		// Lastly, we need to update the cell key for the aabb.
		spatiallyPositionedAABB.setCellKey(currentCellKey);
	}
	
	public ArrayList<AABB> getNeighbouringAABBs(AABB aabb) {
		// Get the spatially positioned AABB for the AABB.
		SpatiallyPoisitionedAABB spatiallyPositionedAABB = this.AABBToSpatialAABBMap.get(aabb);
		// If there was no spatially positioned AABB then there is nothing to do.
		if (spatiallyPositionedAABB == null) {
			return new ArrayList<AABB>();
		}
		
		// TODO return a concatenation of all ABBB lists in cell surrounding the current one. 
		// TODO Add Cell to use instead of ArrayList<AABB> and pass it its cell position as we onyl have the cell key now.
		
		
		return null;
	}
	
	/**
	 * Generate the spatial cell key for the current position of an AABB.
	 * @param aabb The AABB for which to find a spatial grid key
	 * @return The spatial grid key for the current position of an AABB.
	 */
	private String getCellKey(Origin origin) {
		// The type of key we create will depend on the grid dimension.
		if (this.dimension == Dimension.THREE_DIMENSIONS) {
			return this.getCellPosition(Axis.X, origin.getX()) + "_" + this.getCellPosition(Axis.Y, origin.getY()) + "_" + this.getCellPosition(Axis.Z, origin.getZ()); 
		} else {
			return this.getCellPosition(Axis.X, origin.getX()) + "_" + this.getCellPosition(Axis.Y, origin.getY());
		}
	}
	
	/**
	 * Get the cell position based on an absolute world position and axis. 
	 * @param axis The axis on which we are getting the cell position.
	 * @param value The absolute world position.
	 * @return The cell position based on an absolute world position and axis. 
	 */
	private int getCellPosition(Axis axis, double value) {
		switch (axis) {
			case X:
				return (int) Math.floor(value / this.cellWidth);
			case Y:
				return (int) Math.floor(value / this.cellHeight);
			case Z:
				return (int) Math.floor(value / this.cellDepth);
			default:
				throw new RuntimeException("Invalid Axis Value: " + axis);
		}
	}
}
