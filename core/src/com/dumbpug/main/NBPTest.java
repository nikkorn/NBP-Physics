package com.dumbpug.main;

import com.dumbpug.nbp.NBPBox;
import com.dumbpug.nbp.NBPBoxType;

public class NBPTest {

	public static void main(String[] args) {
		NBPBox b1 = new NBPBox(0, 0, 2, 4, NBPBoxType.KINETIC);
		System.out.println("orx: " + b1.getCurrentOriginPoint().getX());
		System.out.println("ory: " + b1.getCurrentOriginPoint().getY());
	}
}
