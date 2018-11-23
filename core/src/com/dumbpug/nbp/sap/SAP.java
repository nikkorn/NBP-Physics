package com.dumbpug.nbp.sap;

import java.util.ArrayList;
import java.util.HashMap;
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

		// TODO Get the intersections on each axis and find the common intresections across all axis.
		
		// Return the map of intersections.
		return intersections;
	}
}
