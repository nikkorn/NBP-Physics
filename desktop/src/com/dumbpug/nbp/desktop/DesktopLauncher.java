package com.dumbpug.nbp.desktop;

import java.util.Scanner;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dumbpug.main.*;
import com.dumbpug.main.gamedevicestesting.NBPStage;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("NiksBasicPhysics");
        System.out.println("----------------");
        System.out.println("1. Slippery/Bouncy Surfaces");
        System.out.println("2. Cluster");
        System.out.println("3. Force Bloom");
        System.out.println("4. Fast Ballistic (To test for accurate collision resolution)");
        System.out.println("5. Sample stage with grenades and rockets");
        
        System.out.print("Please make a selection (e.g. 5): ");
        int selection = Integer.parseInt(scanner.nextLine());
 
    	switch(selection) {
    	case 1: 
    		new LwjglApplication(new NBP(), config);
    		System.out.println("Movement: WASD");
    		break;
    	case 2: 
    		new LwjglApplication(new NBPCluster(), config);
    		break;
    	case 3: 
    		new LwjglApplication(new NBPBloomTest(), config);
    		break;
    	case 4: 
    		new LwjglApplication(new NBPBallistic(), config);
    		break;
    	case 5: 
    		new LwjglApplication(new NBPStage(), config);
    		System.out.println("Movement: WASD      Grenade: G     Rocket: R");
    		System.out.println("   RubberGrenade: B     StickyGrenade: K");
    		System.out.println("        Mine: M     ClusterGrenade: C");
    		System.out.println("                LaserMine: L");
    		break;
    	default: 
    		System.out.println("Invalid selection");
    		break;
    	}
    }
}
