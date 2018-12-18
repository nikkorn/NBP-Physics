package com.dumbpug.nbp.test.spatialgrid;

import com.dumbpug.nbp.AABB;
import com.dumbpug.nbp.Dimension;
import com.dumbpug.nbp.spatialgrid.SpatialGrid;

public class SGNegativeAABBTest {

	public static void main(String[] args) {
		SpatialGrid<AABB> spatialGrid = new SpatialGrid<AABB>(Dimension.THREE_DIMENSIONS, 20f);
		
		AABB aabb = new AABB(0f, -10.001f, 0f, 10f, 10f, 10f);
		
		spatialGrid.add(aabb);
		
		System.out.println("AABB should be in cell 0_-1_0");
	}

}
