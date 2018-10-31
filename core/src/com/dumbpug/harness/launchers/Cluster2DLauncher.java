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
public class Cluster2DLauncher extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture wimg;

    /** The physics entities. */
    private Environment environment;
    private Box box1;
    private Box box2;
    private Box box3;
    private ArrayList<Box> cluster = new ArrayList<Box>();
    
    private int clusterSize = 500;

    private Random ran;

    @Override
    public void create () {
        ran   = new Random();
        batch = new SpriteBatch();
        wimg  = new Texture("white_box.png");

        environment = new Environment(Dimension.TWO_DIMENSIONS, new Gravity(Axis.Y, -0.09f));
        
        box1 = new Basic2DBox(140, 80, 20, 180, BoxType.STATIC);
        box1.setName("box1");
        
        box2 = new Basic2DBox(205, 100, 230, 20, BoxType.STATIC);
        box2.setName("box2");
        
        box3 = new Basic2DBox(480, 80, 20, 180, BoxType.STATIC);
        box3.setName("box3");
        
        // Create a cluster.
        for(int i = 0; i < clusterSize; i++) {
            Box cBox = new Basic2DBox((ran.nextFloat()*300f) + 160f, 440, 5, 5, BoxType.DYNAMIC);
            environment.addBox(cBox);
            cBox.applyImpulse((ran.nextFloat()*6f)-3f, 0);
            cBox.setFriction(ran.nextFloat());
            cBox.setRestitution(ran.nextFloat());
            cluster.add(cBox);
        }
        
        environment.addBox(box1);
        environment.addBox(box2);
        environment.addBox(box3);

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
            batch.draw(wimg, box.getX(), box.getY(), box.getWidth(), box.getHeight());
        }
        batch.end();
    }
}