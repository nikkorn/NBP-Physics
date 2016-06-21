package com.dumbpug.main;

import com.dumbpug.nbp.NBPBox;
import com.dumbpug.nbp.NBPBoxType;
import com.dumbpug.nbp.NBPMath;
import com.dumbpug.nbp.NBPPoint;

public class NBPTest {

	public static void main(String[] args) {
		// perfect plus (1.0, 0.0)
//		NBPPoint p1 = new NBPPoint(0,0);
//		NBPPoint p2 = new NBPPoint(2,0);
//		NBPPoint p3 = new NBPPoint(1,1);
//		NBPPoint p4 = new NBPPoint(1,-1);

		// perfect cross over 0 (0.0, 0.0)
//		NBPPoint p1 = new NBPPoint(-1,-1);
//		NBPPoint p2 = new NBPPoint(1,1);
//		NBPPoint p3 = new NBPPoint(-1,1);
//		NBPPoint p4 = new NBPPoint(1,-1);

		// no intersect
//		NBPPoint p1 = new NBPPoint(1,1);
//		NBPPoint p2 = new NBPPoint(3,1);
//		NBPPoint p3 = new NBPPoint(3,9);
//		NBPPoint p4 = new NBPPoint(3.1f,8);
//
//		NBPPoint intersection = NBPMath.getIntersectionPointOFTwoLineSegments(p1,p2,p3,p4);
//
//		if(intersection != null) {
//			System.out.println("Intersect!");
//			System.out.println("X: " + intersection.getX());
//			System.out.println("Y: " + intersection.getY());
//		} else {
//			System.out.println("No intersect");
//		}
		
		NBPPoint p = new NBPPoint(20,20);
		NBPPoint bp1 = new NBPPoint(10,10);
		NBPPoint bp2 = new NBPPoint(40,40);
		
		if(NBPMath.doesPointResideInBoxDefinedByLine(p, bp1, bp2)) {
			System.out.println("In There!!");
		} else {
			System.out.println("Not in there!");
		}
	}
}
