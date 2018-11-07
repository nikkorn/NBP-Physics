package com.dumbpug.nbp;

import java.util.ArrayList;
import java.util.Iterator;

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
    	if (box.getType() == BoxType.DYNAMIC) {
    		this.dynamicBoxes.add(box);
    	} else {
    		this.staticBoxes.add(box);
    	}
    	this.boxes.add(box);
    }
    
    /**
     * Removes the specified box from this collection.
     * @param box The box to remove.
     */
    public void remove(Box box) {
    	if (box.getType() == BoxType.DYNAMIC) {
    		this.dynamicBoxes.remove(box);
    	} else {
    		this.staticBoxes.remove(box);
    	}
    	this.boxes.remove(box);
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
            }
        }
    }
}
