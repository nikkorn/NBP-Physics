package com.dumbpug.harness.launchers;

import java.util.ArrayList;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.harness.Basic2DBox;
import com.dumbpug.nbp.Axis;
import com.dumbpug.nbp.Box;
import com.dumbpug.nbp.BoxType;
import com.dumbpug.nbp.Dimension;
import com.dumbpug.nbp.Environment;
import com.dumbpug.nbp.Gravity;

/**
 * A simple 2D launcher with lots of individual dynamic boxes.
 */
public class Cluster2DStaticTowerLauncher extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture wimg;

    /** The physics entities. */
    private Environment environment;
    private ArrayList<Box> cluster = new ArrayList<Box>();
    
    private int clusterSize = 1;
    
    private int towerWidth  = 100;
    private int towerHeight = 100;
    private int towerBoxSize = 5;

    @Override
    public void create () {
        batch = new SpriteBatch();
        wimg  = new Texture("white_box.png");

        environment = new Environment(Dimension.TWO_DIMENSIONS, 20f, new Gravity(Axis.Y, -0.09f));
        
        // Create a tower of static boxes that do not overlap.
        for(int x = 0; x < towerWidth; x++) {
        	for(int y = 0; y < towerHeight; y++) {
                Box cBox = new Basic2DBox((x * towerBoxSize) + 80f, (y * towerBoxSize) - 300f, towerBoxSize, towerBoxSize, BoxType.STATIC);
                environment.addBox(cBox);
                cluster.add(cBox);
            }
        }
        
        // Create a cluster of dynamic boxes.
        for(int i = 0; i < clusterSize; i++) {
            Box cBox = new Basic2DBox(200f, 400, 5, 5, BoxType.DYNAMIC);
            cBox.setRestitution(0.9f); // Changing the value effects whether collision works as expected! 
            environment.addBox(cBox);
            cluster.add(cBox);
        }
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
		
        // Update the physics environment.
        environment.update();

        // Draw all boxes in cluster
        batch.begin();
        for(Box box : cluster) {
            // Draw the box.
            batch.draw(wimg, box.getX(), box.getY(), box.getWidth(), box.getHeight());
        }
        batch.end();
    }
}