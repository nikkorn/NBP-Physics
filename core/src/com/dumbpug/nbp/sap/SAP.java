package com.dumbpug.nbp.sap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.dumbpug.nbp.AABB;
import com.dumbpug.nbp.Axis;
import com.dumbpug.nbp.Dimension;

/**
 * Implementation of a Sweep and Prune algorithm.
 */
public class SAP {
	/**
	 * The list of axis-sorted AABB lists.
	 */
	private ArrayList<SortableAABBList> sortedAABBLists = new ArrayList<SortableAABBList>();
	
	/**
	 * Create a new instance of the SAP class.
	 * @param dimension The dimension in which we are searching for intersections.
	 */
	public SAP(Dimension dimension) {
		sortedAABBLists.add(new SortableAABBList(Axis.X));
		sortedAABBLists.add(new SortableAABBList(Axis.Y));
		if (dimension == Dimension.THREE_DIMENSIONS) {
			sortedAABBLists.add(new SortableAABBList(Axis.Z));
		}
	}
	
	/**
	 * Add an AABB to be included in intersections tests.
	 * @param box The AABB to include in intersection tests.
	 */
	public void add(AABB box) {
		for (SortableAABBList list : this.sortedAABBLists) {
			list.add(box);
		}
	}
	
	/**
	 * Remove an AABB for consideration in intersection tests.
	 * @param box The AABB to remove.
	 */
	public void remove(AABB box) {
		for (SortableAABBList list : this.sortedAABBLists) {
			list.remove(box);
		}
	}
	
	/**
	 * Sort the AABBs on each relevant axis and get the map of intersections
	 * @return The map of intersections.
	 */
	public HashMap<AABB, ArrayList<AABB>> getIntersections() {
		// Create the map of intersections.
		HashMap<AABB, ArrayList<AABB>> intersections = new HashMap<AABB, ArrayList<AABB>>();
		
		boolean isInitalAxis = true;
		
		// Iterate over each sortable axis list.
		for (SortableAABBList sortedAxisList : sortedAABBLists) {
			// Get any intersections on this axis.
			HashMap<AABB, ArrayList<AABB>> axisIntersections = sortedAxisList.getAxisIntesections();
			
			for (Map.Entry<AABB, ArrayList<AABB>> entry : axisIntersections.entrySet()) {
				// Get the current AABB.
				AABB current = entry.getKey();
				
				// Get the list of AABBs intersecting the current AABB on the axis.
				ArrayList<AABB> boxesIntersectingCurrent = entry.getValue();
				
				if (isInitalAxis) {
					// Any time we find no intersections for an AABB on any axis then it cannot be intersecting with anything.
					if (boxesIntersectingCurrent.isEmpty()) {
						continue;
					}
					
					// There are some AABBs intersecting the current one on the initial axis.
					intersections.put(current, boxesIntersectingCurrent);
				} else {
					// Get the list of possible intersections for the current box.
					ArrayList<AABB> possibleIntersections = intersections.get(current);
					
					if (possibleIntersections == null) {
						// There were no intersections on any other axis.
					} else if (boxesIntersectingCurrent.isEmpty()) {
						// There were no intersections on this axis.
						intersections.remove(current);
					} else {
						// We need to remove any intersections that have occurred on other axis but not on this one.
						Iterator<AABB> iterator = possibleIntersections.iterator();
						while (iterator.hasNext()) {
							// Get the next possible intersection.
							AABB possibleIntersection = iterator.next();
							
							// Check whether there was an AABB-AABB intersection that did not occur on the current axis.
							if (!boxesIntersectingCurrent.contains(possibleIntersection)) {
								iterator.remove();
							}
						}
						
						// Check whether any intersections still exist.
						if (possibleIntersections.isEmpty()) {
							intersections.remove(current);
						}
					}
				}
			}
			
			// We are done with this axis.
			isInitalAxis = false;
		}
		
		// Return the map of intersections.
		return intersections;
	}
}
