package com.dumbpug.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.nbp.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by nik on 25/03/16.
 */
public class NBPBallistic extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture wimg;

    private NBPWorld world;
    private NBPBox box1;

    private ArrayList<NBPBox> cluster = new ArrayList<NBPBox>();
    private int clusterSize = 1;

    private Random ran;

    @Override
    public void create () {
        ran = new Random();
        batch = new SpriteBatch();
        wimg = new Texture("wbox.png");

        world = new NBPWorld(0.09f);
        box1 = new BasicBox(480,40,20,400, NBPBoxType.STATIC);
        box1.setName("wall");

        // Create a cluster
        for(int i = 0; i < clusterSize; i++) {
            NBPBox cBox = new BasicBox(100,250,10,10,NBPBoxType.KINETIC);
            world.addBox(cBox);
            cBox.setName("bullet");
            float baseImpulse = ran.nextFloat(); // breaks: 0.39481282f
            // System.out.println("Base Impulse: " + baseImpulse);
            cBox.applyImpulse(16f, 0.39481282f * 3f);
            cBox.setRestitution(0.5f);
            cluster.add(cBox);
        }

        world.addBox(box1);
        cluster.add(box1);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Reset boxes
        for(NBPBox box : cluster) {
            if(box.getY() < 0) {
                box.setX(100);
                box.setY(250);
                box.setVelx(0);
                box.setVely(0);
                float baseImpulse = ran.nextFloat();
                System.out.println("Reset Impulse: " + baseImpulse);
                box.applyImpulse(16f, baseImpulse*3f);
            }
        }

        world.update();

        // Draw all boxes in cluster
        batch.begin();
        for(NBPBox box : cluster) {
            batch.draw( wimg, box.getX(), box.getY(), box.getWidth(), box.getHeight());
        }
        batch.end();
    }
}
