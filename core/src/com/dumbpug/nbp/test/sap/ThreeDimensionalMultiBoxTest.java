package com.dumbpug.nbp.test.sap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.dumbpug.nbp.AABB;
import com.dumbpug.nbp.Dimension;
import com.dumbpug.nbp.sap.SAP;

public class ThreeDimensionalMultiBoxTest {
	
	public static void main(String[] args) {
		
		Random ran = new Random();
		
		SAP sap = new SAP(Dimension.THREE_DIMENSIONS);
		
		int boxCount = 1000;
		
		// Create some random AABBs to process.
		for (int i = 0; i < boxCount; i++) {
			AABB box = new AABB(ran.nextInt(1000), ran.nextInt(1000), 
					ran.nextInt(1000), ran.nextInt(10), 
					ran.nextInt(10), ran.nextInt(10));
			
			sap.add(box);
		}
		
		System.out.println("processing " + boxCount + " boxes...");
		
		for (int call = 0; call < 50; call++) {
			long time = System.currentTimeMillis();
			
			HashMap<AABB, ArrayList<AABB>> intersections = sap.getIntersections();
			
			System.out.println((call + 1) + ": Found " + intersections.size() + " intersecting boxes in " + (System.currentTimeMillis() - time) + "ms");
		}
	}
}
