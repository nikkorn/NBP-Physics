package com.dumbpug.harness.launchers;

import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.harness.Basic2DBox;
import com.dumbpug.nbp.AABB;
import com.dumbpug.nbp.Axis;
import com.dumbpug.nbp.Box;
import com.dumbpug.nbp.BoxType;
import com.dumbpug.nbp.Dimension;
import com.dumbpug.nbp.Environment;
import com.dumbpug.nbp.Gravity;
import com.dumbpug.nbp.zone.CircleZone;

/**
 * A simple 2D launcher with lots of individual dynamic boxes.
 */
public class Cluster2DLauncherWithProjections extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture wimg;
    private Texture gimg;

    /** The physics entities. */
    private Environment environment;
    private Box box1;
    private Box box2;
    private Box box3;
    private ArrayList<Box> cluster = new ArrayList<Box>();
    
    private int clusterSize = 1000;

    private Random ran;

    @Override
    public void create () {
        ran   = new Random();
        batch = new SpriteBatch();
        wimg  = new Texture("white_box.png");
        gimg  = new Texture("green_box.png");

        environment = new Environment(Dimension.TWO_DIMENSIONS, new Gravity(Axis.Y, -0.09f));
        
        box1 = new Basic2DBox(140, 80, 20, 180, BoxType.STATIC);
        box1.setName("box1");
        
        box2 = new Basic2DBox(200, 100, 200, 20, BoxType.STATIC);
        box2.setName("box2");
        
        box3 = new Basic2DBox(480, 80, 20, 180, BoxType.STATIC);
        box3.setName("box3");
        
        CircleZone cz = new CircleZone(300, 200, -1f, 50f);
        
        // Create a cluster.
        for(int i = 0; i < clusterSize; i++) {
            Box cBox = new Basic2DBox((ran.nextFloat()*300f) + 160f, 440, 5, 5, BoxType.DYNAMIC);
            cBox.applyImpulse(Axis.X, (ran.nextFloat()*6f)-3f);
            cBox.setFriction(ran.nextFloat());
            cBox.setRestitution(ran.nextFloat());
            environment.addBox(cBox);
            cluster.add(cBox);
        }
        
        environment.addBox(box1);
        environment.addBox(box2);
        environment.addBox(box3);
        environment.addZone(cz);

        cluster.add(box1);
        cluster.add(box2);
        cluster.add(box3);
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
        	// Get the projection of the box.
        	AABB projection = box.getProjection();
        	// Draw the box projection.
            batch.draw(gimg, projection.getX(), projection.getY(), projection.getWidth(), projection.getHeight());
            // Draw the box.
            batch.draw(wimg, box.getX(), box.getY(), box.getWidth(), box.getHeight());
        }
        batch.end();
    }
}