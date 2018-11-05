package com.dumbpug.nbp.test;

import com.dumbpug.nbp.NBPMath;
import com.dumbpug.nbp.point.Point;

public class NBPMathTest {
	
	public static void main(String[] args) {
		// Expecting the intersection th be 0,0.
		Point point = NBPMath.getLineVsLineIntersection(-0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, 0.5f);
		// Print the intersection point.
		printPoint(point);
	}
	
	public static void printPoint(Point point) {
		System.out.println("POINT X: " + point.getX() + " Y: " + point.getY());
	}
}
