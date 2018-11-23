package com.dumbpug.nbp.sap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import com.dumbpug.nbp.AABB;
import com.dumbpug.nbp.Axis;

/**
 * A list of sortable AABBs.
 */
public class SortableAABBList {
	/**
	 * All AABBs.
	 */
	private ArrayList<AABB> boxes = new ArrayList<AABB>();
	/**
	 * The axis on which to sort and find intersections.
	 */
	private Axis axis;
	/**
	 * The comparator to use in sorting the list.
	 */
	private Comparator<AABB> comparator;
	
	/**
	 * Create a new instance of the SortableAABBList class.
	 * @param axis The axis on which to sort the AABBs.
	 */
	public SortableAABBList(final Axis axis) {
		// Create the comparator to use in sorting the list.
		this.comparator = new Comparator<AABB>() {
			@Override
			public int compare(AABB o1, AABB o2) {
				return Float.compare(o1.getPosition(axis), o2.getPosition(axis));
			}
		};
	}
	
	/**
	 * Add a box to the sortable list.
	 * @param box The box to add.
	 */
	public void add(AABB box) {
		if (!this.boxes.contains(box)) {
			this.boxes.add(box);
		}
	}
	
	/**
	 * Remove a box from the sortable list.
	 * @param box The box to remove.
	 */
	public void remove(AABB box) {
		this.boxes.remove(box);
	}
	
	/**
	 * Get the mapping of AABBs to lists of possible intersections.
	 * @return The mapping of AABBs to lists of possible intersections.
	 */
	public HashMap<AABB, ArrayList<AABB>> getAxisIntesections() {
		// We need a map to store the axis intersections.
		HashMap<AABB, ArrayList<AABB>> axisIntersectionMap = new HashMap<AABB, ArrayList<AABB>>();
		
		// If there are no boxes then just return.
		if (boxes.isEmpty()) {
			return axisIntersectionMap;
		}
		
		// We need to sort the AABBs based on axis position.
		Collections.sort(this.boxes, this.comparator);
		
		// Create the active list, adding the lowest positioned box on the axis.
		ArrayList<AABB> activeBoxes = new ArrayList<AABB>();
		activeBoxes.add(boxes.get(0));
		
		// Iterate over the sorted boxes, going from lowest positioned box on the axis to highest.
	    for (int boxIndex = 1; boxIndex < boxes.size(); boxIndex++) {
	    	// Get the next box along the axis.
	    	AABB current = boxes.get(boxIndex);
	    	
	    	// Try to get the list of current intersections for this box.
			ArrayList<AABB> currentBoxIntersections = axisIntersectionMap.get(current);
			
			// Check whether this box actually has an entry in the axis intersection map. 
			if (currentBoxIntersections == null) {
				currentBoxIntersections = new ArrayList<AABB>();
				axisIntersectionMap.put(current, currentBoxIntersections);
			}
			
			// Remove any active boxes that we have moved on from and do not intersect the current box.
			Iterator<AABB> activeBoxIterator = activeBoxes.iterator();
			while(activeBoxIterator.hasNext()){
				// Get the next active box.
				AABB active = activeBoxIterator.next();
				
				// Remove the active box if it is too low on the axis to intersect the current box.
				if (current.getPosition(axis) > (active.getPosition(axis) + active.getLength(axis))) {
					activeBoxIterator.remove();
				}
			}
			
			// Every item remaining in the active box list is intersecting the current box on this axis.
			for (AABB intersecting : activeBoxes) {
				// Try to get the list of intersections for the intersecting active box.
				ArrayList<AABB> activeBoxintersections = axisIntersectionMap.get(intersecting);
				
				// Check whether this box actually has an entry in the axis intersection map. 
				if (activeBoxintersections == null) {
					activeBoxintersections = new ArrayList<AABB>();
					axisIntersectionMap.put(intersecting, activeBoxintersections);
				}
				
				// The current box and the active box are intresecting on the axis.
				currentBoxIntersections.add(intersecting);
				activeBoxintersections.add(current);
			}
	    }
		
	    // Return the map of AABB intersections on the axis.
		return axisIntersectionMap;
	}
}
