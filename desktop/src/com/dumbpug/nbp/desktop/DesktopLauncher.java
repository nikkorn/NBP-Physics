package com.dumbpug.nbp.desktop;

import java.util.Scanner;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dumbpug.harness.launchers.Cluster2DLauncher;
import com.dumbpug.harness.launchers.Grid2DLauncher;
import com.dumbpug.harness.launchers.SAP3DTestLauncher;
import com.dumbpug.harness.launchers.Simple2DLauncher;
import com.dumbpug.harness.launchers.SpatialGrid3DTestLauncher;

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
        System.out.println("3. Player in 2D Grid");
        System.out.println("4. SAP 3D test");
        System.out.println("5. Spatial grid 3D test");
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
	    	case 3: 
	    		new LwjglApplication(new Grid2DLauncher(), config);
	    		break;
	    	case 4: 
	    		new LwjglApplication(new SAP3DTestLauncher(), config);
	    		break;
	    	case 5: 
	    		new LwjglApplication(new SpatialGrid3DTestLauncher(), config);
	    		break;
	    	default: 
	    		System.out.println("Invalid selection");
	    		break;
    	}
    	
    	scanner.close();
	}
}
