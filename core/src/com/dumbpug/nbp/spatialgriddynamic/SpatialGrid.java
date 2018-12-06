package com.dumbpug.nbp.spatialgriddynamic;

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
	private HashMap<String, Cell> grid = new HashMap<String, Cell>();
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
		// Get the grid cell that the origin resides in.
		Cell cell = this.getCellForOrigin(aabbOrigin);
		// Add the new aabb to the map of aabbs to their spatially positioned representations.
		this.AABBToSpatialAABBMap.put(aabb, new SpatiallyPoisitionedAABB(aabb, aabbOrigin, cell.getKey()));
		// Add the new aabb to the cell.
		cell.addAABB(aabb);
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
		this.grid.get(originalCellKey).removeAABB(aabb);
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
		// Get the key of the cell that the AABB was last in.
		String originalCellKey = spatiallyPositionedAABB.getCellKey();
		// Get the grid cell that the origin resides in.
		Cell cell = this.getCellForOrigin(spatiallyPositionedAABB.getAABBOrigin());
		// If the original and current cell keys do not differ then the AABB is still in the correct cell.
		if (originalCellKey == cell.getKey()) {
			return;
		}
		// The AABB has moved into a different cell since its last update.
		// Firstly, We will need to physically remove the AABB from its original grid cell list.
		this.grid.get(originalCellKey).removeAABB(aabb);
		// Secondly, we need to move it into the grid cell list that it should be in.
		cell.addAABB(aabb);
		// Lastly, we need to update the cell key for the aabb.
		spatiallyPositionedAABB.setCellKey(cell.getKey());
	}
	
	/**
	 * Get a list of AABBs that reside in the current or adjacent cells to the specified one excluding it.
	 * @param aabb The AABB for which to find a list of AABBs that reside in the current or adjacent cells to it.
	 * @return A list of AABBs that reside in the current or adjacent cells to the specified one excluding it.
	 */
	public ArrayList<AABB> getNeighbouringAABBs(AABB aabb) {
		// Get the spatially positioned AABB for the AABB.
		SpatiallyPoisitionedAABB spatiallyPositionedAABB = this.AABBToSpatialAABBMap.get(aabb);
		
		// Create an empty list in which to add all neighbouring AABBs.
		ArrayList<AABB> neighbouring = new ArrayList<AABB>();
		
		// If there was no spatially positioned AABB then there is nothing to do.
		if (spatiallyPositionedAABB == null) {
			return neighbouring;
		}
		
		// Get the cell that the specified AABB resides in.
		Cell cell = this.grid.get(spatiallyPositionedAABB.getCellKey());
		
		// The way that we find neighbouring AABBs will depend on the grid dimension.
		if (this.dimension == Dimension.THREE_DIMENSIONS) {
			for (int x = (cell.getX() - 1); x <= (cell.getX() + 1); x++) {
				for (int y = (cell.getY() - 1); y <= (cell.getY() + 1); y++) {
					for (int z = (cell.getZ() - 1); z <= (cell.getZ() + 1); z++) {
						// Try to get the cell at this position.
						Cell neighbouringCell = this.getCellForPosition(x, y, z);
						
						// There is nothing to do if the cell does not exist.
						if (neighbouringCell == null) {
							continue;
						}
						
						// We found a neighbouring cell. Add all of the AABBs that reside in it into our list.
						neighbouring.addAll(neighbouringCell.getAABBList());
					}
				}
			}
		} else {
			for (int x = (cell.getX() - 1); x <= (cell.getX() + 1); x++) {
				for (int y = (cell.getY() - 1); y <= (cell.getY() + 1); y++) {
					// Try to get the cell at this position.
					Cell neighbouringCell = this.getCellForPosition(x, y);
					
					// There is nothing to do if the cell does not exist.
					if (neighbouringCell == null) {
						continue;
					}
					
					// We found a neighbouring cell. Add all of the AABBs that reside in it into our list.
					neighbouring.addAll(neighbouringCell.getAABBList());
				}
			}
		}
		// Return the list of neighbouring AABBs.
		return neighbouring;
	}
	
	/**
	 * Get the existing cell at the specified position, or null if it does not exist.
	 * @param x The x position of the cell.
	 * @param y The y position of the cell.
	 * @return The existing cell at the specified position, or null if it does not exist.
	 */
	public Cell getCellForPosition(int x, int y) {
		// Create the cell key.
		String cellKey = x + "_" + y;
		
		// Return the cell in the grid with this key.
		return this.grid.get(cellKey);
	}
	
	/**
	 * Get the existing cell at the specified position, or null if it does not exist.
	 * @param x The x position of the cell.
	 * @param y The y position of the cell.
	 * @param z The z position of the cell.
	 * @return The existing cell at the specified position, or null if it does not exist.
	 */
	public Cell getCellForPosition(int x, int y, int z) {
		// Create the cell key.
		String cellKey = x + "_" + y + "_" + z;
		
		// Return the cell in the grid with this key.
		return this.grid.get(cellKey);
	}
	
	/**
	 * Get the existing cell that contains the specified origin, or create it if it does not exist.
	 * @param origin The origin for which we are looking for the contianing cell.
	 * @return The cell that contains the origin.
	 */
	public Cell getCellForOrigin(Origin origin) {
		// The type of key we create will depend on the grid dimension.
		if (this.dimension == Dimension.THREE_DIMENSIONS) {
			// Get the position of the cell in which the origin resides.
			int cellX = this.getCellPosition(Axis.X, origin.getX());
			int cellY = this.getCellPosition(Axis.Y, origin.getY());
			int cellZ = this.getCellPosition(Axis.Z, origin.getZ());
			
			// Create the cell key.
			String cellKey = cellX + "_" + cellY + "_" + cellZ;
			
			// If the cell already exists then just return it, otherwise we will need to create a new one and add it to our grid.
			if (this.grid.containsKey(cellKey)) {
				return this.grid.get(cellKey);
			} else {
				// Create the new cell.
				Cell cell = new Cell(cellKey, cellX, cellY, cellZ);
				
				// Add the new cell to the grid.
				this.grid.put(cellKey, cell);
				
				// Return the newly created cell.
				return cell;
			}
		} else {
			// Get the position of the cell in which the origin resides.
			int cellX = this.getCellPosition(Axis.X, origin.getX());
			int cellY = this.getCellPosition(Axis.Y, origin.getY());
			
			// Create the cell key.
			String cellKey = cellX + "_" + cellY;
			
			// If the cell already exists then just return it, otherwise we will need to create a new one and add it to our grid.
			if (this.grid.containsKey(cellKey)) {
				return this.grid.get(cellKey);
			} else {
				// Create the new cell.
				Cell cell = new Cell(cellKey, cellX, cellY);
				
				// Add the new cell to the grid.
				this.grid.put(cellKey, cell);
				
				// Return the newly created cell.
				return cell;
			}
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
