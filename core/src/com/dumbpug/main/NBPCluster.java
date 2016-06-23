package com.dumbpug.main;

import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.nbp.NBPBox;
import com.dumbpug.nbp.NBPBoxType;
import com.dumbpug.nbp.NBPMath;
import com.dumbpug.nbp.NBPWorld;

/**
 * Testing for the physics engine.
 * @author Nikolas Howard
 *
 */
public class NBPCluster extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture wimg;
    private Texture rimg;

    private NBPWorld world;
    private NBPBox box1;
    private NBPBox box2;
    private NBPBox box3;

    private ArrayList<NBPBox> cluster = new ArrayList<NBPBox>();
    private int clusterSize = 500;

    private Random ran;

    @Override
    public void create () {
        ran = new Random();
        batch = new SpriteBatch();
        wimg = new Texture("wbox.png");
        rimg = new Texture("rbox.png");

        world = new NBPWorld(0.09f, 4f);
        box1 = new NBPBox(140,80,20,180, NBPBoxType.STATIC);
        box1.setName("box1");
        
        box2 = new NBPBox(205,100,230,20,NBPBoxType.STATIC);
        box2.setName("box2");
        
        box3 = new NBPBox(480,80,20,180,NBPBoxType.STATIC);
        box3.setName("box3");
        
        // Create a cluster
        for(int i = 0; i < clusterSize; i++) {
            NBPBox cBox = new NBPBox((ran.nextFloat()*300f) + 160f,440,5,5,NBPBoxType.KINETIC);
            world.addBox(cBox);
            cBox.applyImpulse((ran.nextFloat()*6f)-3f, 0);
            cBox.setFriction(ran.nextFloat());
            cBox.setRestitution(ran.nextFloat());
            cluster.add(cBox);
        }
        
        world.addBox(box1);
        world.addBox(box2);
        world.addBox(box3);

        cluster.add(box1);
        cluster.add(box2);
        cluster.add(box3);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());

        world.update();
        
        // Draw all boxes in cluster
        batch.begin();
        for(NBPBox box : cluster) {
            batch.draw(isBoxColliding(box) ? rimg : wimg, box.getX(), box.getY(), box.getWidth(), box.getHeight());
        }
        batch.end();
    }

    public boolean isBoxColliding(NBPBox tBox) {
        for(NBPBox box : world.getWorldBoxes()) {
            if(!(tBox == box)) {
                if(NBPMath.doBoxesCollide(tBox, box)) {
                    return true;
                }
            }
        }
        return false;
    }
}