package com.dumbpug.nbp;

import java.util.ArrayList;
import java.util.Iterator;
import com.dumbpug.nbp.spatialgrid.SpatialGrid;

/**
 * A collection of static and dynamic boxes.
 */
public class Boxes {
	/**
     * The dynamic box entities that are in this environment.
     */
    private ArrayList<Box> boxes = new ArrayList<Box>();
	/**
     * The dynamic box entities that are in this environment.
     */
    private ArrayList<Box> dynamicBoxes = new ArrayList<Box>();
    /**
     * The static box entities that are in this environment.
     */
    private ArrayList<Box> staticBoxes = new ArrayList<Box>();
    /**
     * The spatial grid to use in broad phase collision detection.
     */
    private SpatialGrid spatialGrid;
    
    /**
     * Create a new instance of the Boxes class.
     * @param grid The spatial grid to use in finding collision candidates between boxes.
     */
    public Boxes(SpatialGrid grid) {
    	this.spatialGrid = grid;
    }
    
    /**
     * Get all dynamic boxes.
     * @return All dynamic boxes.
     */
    public ArrayList<Box> getDynamicBoxes() {
    	return this.dynamicBoxes;
    }
    
    /**
     * Get all static boxes.
     * @return All static boxes.
     */
    public ArrayList<Box> getStaticBoxes() {
    	return this.staticBoxes;
    }
    
    /**
     * Get all dynamic and static boxes.
     * @return All dynamic and static boxes.
     */
    public ArrayList<Box> getBoxes() {
    	return this.boxes;
    }
    
    /**
     * Add a box.
     * @param box The box to add.
     */
    public void add(Box box) {
    	// Add the box to its type-specific list.
    	if (box.getType() == BoxType.DYNAMIC) {
    		this.dynamicBoxes.add(box);
    	} else {
    		this.staticBoxes.add(box);
    	}
    	// Add the box to the overall list of boxes.
    	this.boxes.add(box);
    	// Add the box to the spatial grid, along with any sensors.
    	this.addBoxToSpatialGrid(box);
    }
    
    /**
     * Removes the specified box from this collection.
     * @param box The box to remove.
     */
    public void remove(Box box) {
    	// Remove the box from its type-specific list.
    	if (box.getType() == BoxType.DYNAMIC) {
    		this.dynamicBoxes.remove(box);
    	} else {
    		this.staticBoxes.remove(box);
    	}
    	// Remove the box from the overall list of boxes.
    	this.boxes.remove(box);
    	// Remove the box from the spatial grid, along with any sensors.
    	this.removeBoxFromSpatialGrid(box);
    }
    
    /**
     * Gets whether this collection contains the specified box.
     * @param box The box to check for.
     * @return Whether this collection contains the specified box.
     */
    public boolean contains(Box box) {
    	if (box.getType() == BoxType.DYNAMIC) {
    		return this.dynamicBoxes.contains(box);
    	} else {
    		return this.staticBoxes.contains(box);
    	}
    }
    
    /**
     * Remove any dynamic/static boxes that have been marked for deletion.
     * TODO This can be made quicker by each box telling the Boxes class when they are marked and store a list of them.
     */
    public void removeDeletedBoxes() {
    	// Remove any dynamic boxes which were marked for deletion.
        Iterator<Box> boxIterator = dynamicBoxes.iterator();
        while (boxIterator.hasNext()) {
            Box box = boxIterator.next();
            if (box.isMarkedForDeletion()) {
                box.setDeleted();
                boxIterator.remove();
                // Call user specified behaviour on deletion.
                box.onDeletion();
                // Remove the dynamic box from the collection of all boxes.
                this.boxes.remove(box);
                // Remove the box from the spatial grid, along with any sensors.
            	this.removeBoxFromSpatialGrid(box);
            }
        }
        // Remove any boxes which were marked for deletion.
        boxIterator = staticBoxes.iterator();
        while (boxIterator.hasNext()) {
            Box box = boxIterator.next();
            if (box.isMarkedForDeletion()) {
                box.setDeleted();
                boxIterator.remove();
                // Call user specified behaviour on deletion.
                box.onDeletion();
                // Remove the static box from the collection of all boxes.
                this.boxes.remove(box);
                // Remove the box from the spatial grid, along with any sensors.
            	this.removeBoxFromSpatialGrid(box);
            }
        }
    }
    
    /**
     * Add the projection of a box and any attached sensors to the spatial grid.
     * @param box The box to add.
     */
    private void addBoxToSpatialGrid(Box box) {
    	// Add the box to the spatial grid.
    	this.spatialGrid.add(box.getProjection());
    	// Add any sensors to the spatial grid.
    	for (Sensor sensor : box.getAttachedSensors()) {
    		this.spatialGrid.add(sensor);
    	}
    }
    
    /**
     * Remove the projection of a box and any attached sensors from the spatial grid.
     * @param box The box to remove.
     */
    private void removeBoxFromSpatialGrid(Box box) {
    	// Remove the box from the spatial grid.
    	this.spatialGrid.remove(box.getProjection());
    	// Remove any sensors from the spatial grid.
    	for (Sensor sensor : box.getAttachedSensors()) {
    		this.spatialGrid.remove(sensor);
    	}
    }
}
