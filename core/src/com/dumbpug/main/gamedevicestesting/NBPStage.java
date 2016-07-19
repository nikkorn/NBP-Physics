package com.dumbpug.main.gamedevicestesting;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.main.gamedevicestesting.weapons.ClusterGrenade;
import com.dumbpug.main.gamedevicestesting.weapons.Grenade;
import com.dumbpug.main.gamedevicestesting.weapons.ProximityMine;
import com.dumbpug.main.gamedevicestesting.weapons.Rocket;
import com.dumbpug.main.gamedevicestesting.weapons.RubberGrenade;
import com.dumbpug.main.gamedevicestesting.weapons.StickyGrenade;
import com.dumbpug.nbp.*;

/**
 * Testing for the physics engine.
 * @author Nikolas Howard
 *
 */
public class NBPStage extends ApplicationAdapter {
    SpriteBatch batch;
    Texture wimg;
    Texture rimg;
    Texture simg;
    Texture oimg;
    Texture gimg;
    Texture pimg;
    Texture piimg;

    NBPWorld world;
    PlayerBox player;

    private int[][] gridLayout = {
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,1,0,1,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    @Override
    public void create() {
        batch = new SpriteBatch();
        wimg = new Texture("wbox.png");
        rimg = new Texture("rbox.png");
        simg = new Texture("sbox.png");
        oimg = new Texture("obox.png");
        gimg = new Texture("gbox.png");
        pimg = new Texture("pbox.png");
        piimg = new Texture("pibox.png");

        world = new NBPWorld(C.WORLD_GRAVITY);

        // Create our grid.
        float gridStartX = 0;
        float gridStartY = gridLayout.length*C.WORLD_TILE_SIZE;
        for(int gridX = 0; gridX < gridLayout[0].length; gridX++) {
            for(int gridY = 0; gridY < gridLayout.length; gridY++) {
                if(gridLayout[gridY][gridX] == 1) {
                    NBPBox gridBlock = new Tile(gridStartX + (gridX*C.WORLD_TILE_SIZE),
                            gridStartY-(gridY*C.WORLD_TILE_SIZE), C.WORLD_TILE_SIZE, C.WORLD_TILE_SIZE, NBPBoxType.STATIC);
                    gridBlock.setName((gridX%2 == 0) ? "GREEN_TILE" : "RED_TILE");
                    world.addBox(gridBlock);
                }
            }
        }

        // Make our player box.
        player = new PlayerBox(65, 200, C.PLAYER_SIZE_WIDTH, C.PLAYER_SIZE_HEIGHT, 1);
        world.addBox(player);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());

        world.update();
        
        // Only allow player to do stuff while he is alive
        if(player.isAlive()) {
        	// Test player horizontal movement.
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                player.moveLeft();
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                player.moveRight();
            }

            // Test player jump
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                player.jump();
            }
            
            // Throw a grenade.
            if (Gdx.input.isKeyPressed(Input.Keys.G)) {
            	world.addBox(new Grenade(player, C.GRENADE_FUSE_MAX));
            }
            
            // Throw a rubber grenade.
            if (Gdx.input.isKeyPressed(Input.Keys.B)) {
            	world.addBox(new RubberGrenade(player, C.GRENADE_FUSE_MAX));
            }
            
            // Throw a sticky grenade.
            if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            	world.addBox(new StickyGrenade(player, C.GRENADE_FUSE_MAX));
            }
            
            // Throw a cluster grenade.
            if (Gdx.input.isKeyPressed(Input.Keys.C)) {
            	world.addBox(new ClusterGrenade(player, C.GRENADE_FUSE_MAX));
            }
            
            // Throw a proximity mine.
            if (Gdx.input.isKeyPressed(Input.Keys.M)) {
            	world.addBox(new ProximityMine(player));
            }
            
            // Fire a Rocket.
            if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            	world.addBox(new Rocket(player));
            }
        }
        
        batch.begin();
        // Draw Grid
        for(NBPBox box : world.getWorldBoxes()) {
        	if (box.getName().equals("GRENADE")) {
        		batch.draw(simg, box.getX(), box.getY(), box.getWidth(), box.getHeight());
        	} else if (box.getName().equals("RUBBER_GRENADE")) {
        		batch.draw(rimg, box.getX(), box.getY(), box.getWidth(), box.getHeight());
        	} else if (box.getName().equals("STICKY_GRENADE")) {
        		batch.draw(oimg, box.getX(), box.getY(), box.getWidth(), box.getHeight());
        	} else if (box.getName().equals("ROCKET")) {
        		batch.draw(pimg, box.getX(), box.getY(), box.getWidth(), box.getHeight());
        	} else if (box.getName().equals("PROXIMITY_MINE")) {
        		float directionAngle = 0f;
        		switch(((ProximityMine) box).getFacingDirection() ) {
				case DOWN:
					directionAngle = 180f;
					break;
				case LEFT:
					directionAngle = 270f;
					break;
				case RIGHT:
					directionAngle = 90f;
					break;
				case UP:
					directionAngle = 0f;
					break;
				default:
					break;
        		}
        		batch.draw(piimg, box.getX(), box.getY(), box.getWidth()/2f, box.getHeight()/2f, box.getWidth(), box.getHeight(), 1f, 1f, directionAngle, 1, 1, 1, 1, false, false);
        	} else {
        		batch.draw(wimg, box.getX(), box.getY(), box.getWidth(), box.getHeight());
        	}
        }
        // Draw the sensors for player
        for (NBPSensor sensor : player.getAttachedSensors()) {
            batch.draw(simg, sensor.getX(), sensor.getY(), sensor.getWidth(), sensor.getHeight());
        }
        batch.end();
    }
}