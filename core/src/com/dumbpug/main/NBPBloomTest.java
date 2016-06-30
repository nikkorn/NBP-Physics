package com.dumbpug.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.nbp.*;

/**
 * Testing for the physics engine.
 * @author Nikolas Howard
 *
 */
public class NBPBloomTest extends ApplicationAdapter {
    SpriteBatch batch;
    Texture wimg;
    Texture rimg;
    Texture simg;

    NBPWorld world;

    private int gridBlockSize = 20;
    private int gridSize = 40;

    @Override
    public void create() {
        batch = new SpriteBatch();
        wimg = new Texture("wbox.png");
        rimg = new Texture("rbox.png");
        simg = new Texture("sbox.png");

        // We want a world with NO gravity.
        world = new NBPWorld(0.0f);

        // Create our grid.
        float gridStartY = gridSize*gridBlockSize;
        for(int gridX = 0; gridX < gridSize; gridX++) {
            for(int gridY = 0; gridY < gridSize; gridY++) {
            	NBPBox gridBlock = new NBPBox((gridX*gridBlockSize),
                        gridStartY-(gridY*gridBlockSize), gridBlockSize, gridBlockSize, NBPBoxType.KINETIC);
                gridBlock.setName("grid_piece");
                gridBlock.setMaxVelocityX(5);
                world.addBox(gridBlock);
            }
        }
        
        // Add our test bloom
        NBPBloom bloom = new NBPBloom(340, 260, 300, 1);
    	world.addBloom(bloom);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());

        world.update();
        
        batch.begin();
        // Draw Grid
        for(NBPBox box : world.getWorldBoxes()) {
            batch.draw(wimg, box.getX(), box.getY(), box.getWidth(), box.getHeight());
        }
        batch.end();
    }
}