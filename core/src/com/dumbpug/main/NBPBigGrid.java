package com.dumbpug.main;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.nbp.*;

/**
 * Testing for the physics engine.
 * @author Nikolas Howard
 *
 */
public class NBPBigGrid extends ApplicationAdapter {
    SpriteBatch batch;
    Texture wimg;
    Texture rimg;
    Texture simg;

    NBPWorld world;
    PlayerBox player;
    
    ArrayList<PlayerProjectile> projectiles = new ArrayList<PlayerProjectile>();

    private int gridBlockSize = 20;
    private int[][] gridLayout = {
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,1},
        {1,0,0,1,1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    @Override
    public void create() {
        batch = new SpriteBatch();
        wimg = new Texture("wbox.png");
        rimg = new Texture("rbox.png");
        simg = new Texture("sbox.png");

        world = new NBPWorld(0.09f);

        // Create our grid.
        float gridStartX = 0;
        float gridStartY = gridLayout.length*gridBlockSize;
        for(int gridX = 0; gridX < gridLayout[0].length; gridX++) {
            for(int gridY = 0; gridY < gridLayout.length; gridY++) {
                if(gridLayout[gridY][gridX] == 1) {
                    NBPBox gridBlock = new BasicBox(gridStartX + (gridX*gridBlockSize),
                            gridStartY-(gridY*gridBlockSize), gridBlockSize, gridBlockSize, NBPBoxType.STATIC);
                    gridBlock.setName("grid_piece");
                    world.addBox(gridBlock);
                }
            }
        }

        // Make our player box.
        player = new PlayerBox(65, 200, 16, 38);
        player.setMaxVelocityX(2f);
        world.addBox(player);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());

        world.update();

        // Test moving box1
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.moveLeft();
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.moveRight();
        }

        // Test jumping box1
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.jump();
        }
        
        // Fire projectile on space.
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
        	PlayerProjectile p = new PlayerProjectile(player.getCurrentOriginPoint().getX(),
        			player.getCurrentOriginPoint().getY(),
        			player.facingRight);
        	world.addBox(p);
        }
        
        // test bloom
        if (Gdx.input.isKeyPressed(Input.Keys.B)) {
        	NBPBloom bloom = new NBPBloom(400, 40, 40, 3);
        	world.addBloom(bloom);
        }
        
        batch.begin();
        // Draw Grid
        for(NBPBox box : world.getWorldBoxes()) {
            batch.draw(wimg, box.getX(), box.getY(), box.getWidth(), box.getHeight());
        }
        // Draw the sensors for player
        for (NBPSensor sensor : player.getAttachedSensors()) {
            batch.draw(simg, sensor.getX(), sensor.getY(), sensor.getWidth(), sensor.getHeight());
        }
        batch.end();
    }
}