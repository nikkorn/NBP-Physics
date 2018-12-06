package com.dumbpug.nbp.test.spatialgrid;

public class BruteForce {
	public static void main(String[] args) {
		int boxCount = 5000;
		
		long time = System.currentTimeMillis();
		
		for (int i = 0; i < boxCount; i++) {
			for (int l = 0; l < boxCount; l++) {
				long p = 50 + 50 / 12;
				p = 50 + 900 / 67;
			}
		}
		
		long end = System.currentTimeMillis() - time;
		
		System.out.println("Brute force took: " + end + "ms");
	}
}
