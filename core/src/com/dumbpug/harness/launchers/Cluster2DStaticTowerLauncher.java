package com.dumbpug.harness.launchers;

import java.util.ArrayList;
import java.util.Random;
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
    private Texture gimg;

    /** The physics entities. */
    private Environment environment;
    private ArrayList<Box> staticBoxes  = new ArrayList<Box>();
    private ArrayList<Box> dynamicBoxes = new ArrayList<Box>();
    
    private int clusterSize = 500;
    
    private int towerWidth  = 100;
    private int towerHeight = 100;
    private int towerBoxSize = 5;

    @Override
    public void create () {
    	Random random = new Random();
        batch         = new SpriteBatch();
        wimg          = new Texture("white_box.png");
        gimg          = new Texture("green_box.png");
        environment   = new Environment(Dimension.TWO_DIMENSIONS, 20f, new Gravity(Axis.Y, -0.09f));
        
        // Create a tower of static boxes that do not overlap.
        for(int x = 0; x < towerWidth; x++) {
        	for(int y = 0; y < towerHeight; y++) {
                Box cBox = new Basic2DBox((x * towerBoxSize) + 80f, (y * towerBoxSize) - 300f, towerBoxSize, towerBoxSize, BoxType.STATIC);
                environment.addBox(cBox);
                staticBoxes.add(cBox);
            }
        }
        
        // Create a cluster of dynamic boxes.
        for(int i = 0; i < clusterSize; i++) {
            Box cBox = new Basic2DBox((random.nextFloat()*500f) + 80f, 300f, 5, 5, BoxType.DYNAMIC);
            cBox.applyImpulse(Axis.X, (random.nextFloat()*6f)-3f);
            cBox.setRestitution(random.nextFloat());
            cBox.setFriction(random.nextFloat());
            environment.addBox(cBox);
            dynamicBoxes.add(cBox);
        }
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
		
        // Update the physics environment.
        environment.update();

        // Draw all boxes.
        batch.begin();
        for(Box box : staticBoxes) {
            batch.draw(wimg, box.getX(), box.getY(), box.getWidth(), box.getHeight());
        }
        for(Box box : dynamicBoxes) {
            batch.draw(gimg, box.getX(), box.getY(), box.getWidth(), box.getHeight());
        }
        batch.end();
    }
}