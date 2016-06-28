package com.dumbpug.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.nbp.*;
import java.util.ArrayList;

/**
 * Created by nik on 25/03/16.
 */
public class NBPSimple extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture wimg;

    private NBPWorld world;
    private NBPBox box1;
    private NBPBox movingBox;
    
    private ArrayList<NBPBox> cluster = new ArrayList<NBPBox>();

    @Override
    public void create () {
        batch = new SpriteBatch();
        wimg = new Texture("wbox.png");

        world = new NBPWorld(0.09f);
        
        box1 = new NBPBox(200,100,100,100, NBPBoxType.STATIC);
        box1.setName("wall");

        movingBox = new NBPBox(200,200,100,100,NBPBoxType.KINETIC);
        movingBox.setName("bullet");

        world.addBox(movingBox);
        world.addBox(box1);
        cluster.add(box1);
        cluster.add(movingBox);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
        	movingBox.applyImpulse(0f, 0.8f);
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
        	movingBox.applyImpulse(0f, -0.8f);
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
