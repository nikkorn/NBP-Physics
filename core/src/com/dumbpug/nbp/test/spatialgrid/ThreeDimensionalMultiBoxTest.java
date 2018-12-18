package com.dumbpug.nbp.test.spatialgrid;

import java.util.ArrayList;
import java.util.Random;
import com.dumbpug.nbp.AABB;
import com.dumbpug.nbp.Dimension;
import com.dumbpug.nbp.spatialgrid.SpatialGrid;

public class ThreeDimensionalMultiBoxTest {
	
	public static void main(String[] args) {
		
		Random ran = new Random();
		
		SpatialGrid grid = new SpatialGrid(Dimension.THREE_DIMENSIONS, 100f);
		
		ArrayList<AABB> boxes = new ArrayList<AABB>();
		
		int boxCount = 1000;
		
		// Create some random AABBs to process.
		for (int i = 0; i < boxCount; i++) {
			AABB box = new AABB(ran.nextInt(1000), ran.nextInt(1000), ran.nextInt(1000), 
					ran.nextInt(10), ran.nextInt(10), ran.nextInt(10));
			
			grid.add(box);
			boxes.add(box);
		}
		
		System.out.println("processing " + boxCount + " boxes...");
		
		long time = System.currentTimeMillis();
		
		for (AABB box : boxes) {
			grid.update(box);
		}
		
		long end = System.currentTimeMillis() - time;
		
		System.out.println("Grid update took: " + end + "ms");
	}
}