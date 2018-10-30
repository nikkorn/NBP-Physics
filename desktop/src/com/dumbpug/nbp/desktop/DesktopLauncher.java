package com.dumbpug.nbp.desktop;

import java.util.Scanner;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dumbpug.nbp.harness.launchers.Cluster2DLauncher;
import com.dumbpug.nbp.harness.launchers.Simple2DLauncher;

/**
 * The desktop launcher.
 */
public class DesktopLauncher {
	
	/**
	 * Program entry point.
	 * @param arg cmd args.
	 */
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        Scanner scanner                      = new Scanner(System.in);
        
        System.out.println("NiksBasicPhysics");
        System.out.println("----------------------------------");
        System.out.println("1. Simple 2D Box");
        System.out.println("2. Cluster of 2D Boxes");
        System.out.println("----------------------------------");
        System.out.print("Please make a selection (e.g. 5): ");
        
        // Get the user selection.
        int selection = Integer.parseInt(scanner.nextLine());
 
        // Invoke the launcher for the selection.
    	switch(selection) {
	    	case 1: 
	    		new LwjglApplication(new Simple2DLauncher(), config);
	    		break;
	    	case 2: 
	    		new LwjglApplication(new Cluster2DLauncher(), config);
	    		break;
	    	default: 
	    		System.out.println("Invalid selection");
	    		break;
    	}
    	
    	scanner.close();
	}
}
