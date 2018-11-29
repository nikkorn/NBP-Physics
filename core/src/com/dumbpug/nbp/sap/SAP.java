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
	 * Sort the AABBs on each relevant axis and get the map of intersections.
	 * @return The map of intersections.
	 */
	public HashMap<AABB, ArrayList<AABB>> getIntersections() {
		// Create the map of resolved intersections that occur on every axis.
		HashMap<AABB, ArrayList<AABB>> absoluteIntersections = new HashMap<AABB, ArrayList<AABB>>();
		
		// Create the list of AABB->AABB[] intersection maps per axis.
		ArrayList<HashMap<AABB, ArrayList<AABB>>> axisIntersectionMapList = new ArrayList<HashMap<AABB, ArrayList<AABB>>>();
		
		// Populate the axis intersection list.
		for (SortableAABBList sortedAxisList : sortedAABBLists) {
			axisIntersectionMapList.add(sortedAxisList.getAxisIntesections());
		}
		
		// Iterate over the list of every AABB that has an intersection on the first axis intersection map in our list.
		for (AABB current : axisIntersectionMapList.get(0).keySet()) {
			
			// Get the axis intersections that are common across all axis for the current box.
			ArrayList<AABB> commonAxisIntersections = getCommonAxisIntersections(current, axisIntersectionMapList);
			
			if (!commonAxisIntersections.isEmpty()) {
				absoluteIntersections.put(current, commonAxisIntersections);
			}
		}
		
		// Return the map of intersections.
		return absoluteIntersections;
	}
	
	/**
	 * Get the intersections that are found across every axis intersection map for the specified AABB.
	 * @param current The AABB to find common intersections for.
	 * @param axisIntersectionMapList The list of AABB->AABB[] intersection maps per axis.
	 * @return The intersections that are found across every axis intersection map for the specified AABB.
	 */
	private ArrayList<AABB> getCommonAxisIntersections(AABB current, ArrayList<HashMap<AABB, ArrayList<AABB>>> axisIntersectionMapList) {
		// Create a list to store the common intersections found across all axis.
		ArrayList<AABB> commonIntersections = new ArrayList<AABB>();
		
		// Create a list to store the lists of intersections with the current box on each axis.
		ArrayList<ArrayList<AABB>> axisIntersectionsForBox = new ArrayList<ArrayList<AABB>>();
		
		for (HashMap<AABB, ArrayList<AABB>> axisIntersectionMap : axisIntersectionMapList) {
			
			// Check whether the current box had any intersections recorded on the current axis.
			if (!axisIntersectionMap.containsKey(current)) {
				
				// We found an axis where the current box did not intersect with anything, we can give up.
				return commonIntersections;
			}
			
			// Add the list of boxes that intersect the current one on this axis to the list of axis intersections for the box.
			axisIntersectionsForBox.add(axisIntersectionMap.get(current));		
		}
		
		// Populate common intersections with values found in every list in axisIntersectionsForBox.
		for (int axisIndex = 0; axisIndex < axisIntersectionsForBox.size(); axisIndex++) {
			if (axisIndex == 0) {
				commonIntersections = axisIntersectionsForBox.get(axisIndex);
			} else {
				commonIntersections.retainAll(axisIntersectionsForBox.get(axisIndex));
			}
		}
		
		return commonIntersections;
	}
}
