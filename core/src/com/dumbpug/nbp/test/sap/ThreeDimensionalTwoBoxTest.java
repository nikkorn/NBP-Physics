package com.dumbpug.nbp.test.sap;

import java.util.ArrayList;
import java.util.HashMap;
import com.dumbpug.nbp.AABB;
import com.dumbpug.nbp.Dimension;
import com.dumbpug.nbp.sap.SAP;

public class ThreeDimensionalTwoBoxTest {
	
	public static void main(String[] args) {
		
		SAP sap = new SAP(Dimension.THREE_DIMENSIONS);
		
		sap.add(new AABB(0, 0, 0, 10, 10, 10));
		sap.add(new AABB(0, 0, 0, 10, 10, 10));
		
		for (int call = 0; call < 50; call++) {
			long time = System.currentTimeMillis();
			
			HashMap<AABB, ArrayList<AABB>> intersections = sap.getIntersections();
			
			System.out.println((call + 1) + ": Found " + intersections.size() + " intersecting boxes in " + (System.currentTimeMillis() - time) + "ms");
		}
	}
}
