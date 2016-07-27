package com.dumbpug.main.gamedevicestesting;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.main.gamedevicestesting.input.IPlayerInput;
import com.dumbpug.main.gamedevicestesting.input.LocalPlayerInput;
import com.dumbpug.main.gamedevicestesting.player.Player;
import com.dumbpug.main.gamedevicestesting.weapons.ClusterGrenade;
import com.dumbpug.main.gamedevicestesting.weapons.Grenade;
import com.dumbpug.main.gamedevicestesting.weapons.LaserMine;
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
    Texture piimgUp;
    Texture piimgDown;
    Texture piimgLeft;
    Texture piimgRight;
    Texture pointer;

    StagePhysicsWorld world;
    Player player;
    
    LocalPlayerInput localPlayerInput;
    
    private int[][] gridLayout = {
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1},
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
        {1,1,0,0,0,0,0,0,1,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    @Override
    public void create() {
        batch      = new SpriteBatch();
        wimg       = new Texture("wbox.png");
        rimg       = new Texture("rbox.png");
        simg       = new Texture("sbox.png");
        oimg       = new Texture("obox.png");
        gimg       = new Texture("gbox.png");
        pimg       = new Texture("pbox.png");
        piimgUp    = new Texture("pibox_up.png");
        piimgDown  = new Texture("pibox_down.png");
        piimgLeft  = new Texture("pibox_left.png");
        piimgRight = new Texture("pibox_right.png");
        pointer    = new Texture("pointer.png");

        world = new StagePhysicsWorld(C.WORLD_GRAVITY);

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
        player = new Player(65, 200, C.PLAYER_SIZE_WIDTH, C.PLAYER_SIZE_HEIGHT, 1);
        world.addBox(player);
        
        // Create our (local) player input interface.
        localPlayerInput = new LocalPlayerInput(player);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
        
        // Update our physics world.
        world.update();
        
        // Mouse capture
 		if(!Gdx.input.isCursorCatched()){
 			if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
 				Gdx.input.setCursorCatched(true);
 			}
 		} else {
 			if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
 				Gdx.input.setCursorCatched(false);
 			}
 		}
 		
 		// Process player input. TODO will eventually have to be done for ever player, not just test player.
 		processPlayerInput(player, localPlayerInput);
        
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
        		switch(((ProximityMine) box).getFacingDirection() ) {
				case DOWN:
	        		batch.draw(piimgDown, box.getX(), box.getY(), box.getWidth(), box.getHeight());
					break;
				case LEFT:
	        		batch.draw(piimgLeft, box.getX(), box.getY(), box.getWidth(), box.getHeight());
					break;
				case RIGHT:
	        		batch.draw(piimgRight, box.getX(), box.getY(), box.getWidth(), box.getHeight());
					break;
				case UP:
	        		batch.draw(piimgUp, box.getX(), box.getY(), box.getWidth(), box.getHeight());
					break;
        		}
        	} else {
        		batch.draw(wimg, box.getX(), box.getY(), box.getWidth(), box.getHeight());
        	}
        	// Draw any sensors.
        	for (NBPSensor sensor : box.getAttachedSensors()) {
                batch.draw(simg, sensor.getX(), sensor.getY(), sensor.getWidth(), sensor.getHeight());
            }
        	// Draw our pointer.
        	batch.draw(pointer, Gdx.input.getX() - (C.MISC_POINTER_SIZE/2f), 
        			Gdx.graphics.getHeight() - (Gdx.input.getY() - (C.MISC_POINTER_SIZE/2f)), C.MISC_POINTER_SIZE, C.MISC_POINTER_SIZE);
        }
        batch.end();
    }

    /**
     * Process a players input.
     * @param playerEntity
     * @param playerInput
     */
	private void processPlayerInput(Player playerEntity, IPlayerInput playerInput) {
		// Set the players angle of focus based on the mouse position.
 		float angleOfFocus = NBPMath.getAngleBetweenPoints(player.getCurrentOriginPoint(), new NBPPoint(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()));
 		player.setAngleOfFocus(angleOfFocus);
 		
 		// Check for presses of number 1-6, this represents a change in active weapon.
 		if(playerInput.isNum1Pressed())
        
        // Only allow player to do stuff while he is alive
        if(player.isAlive()) {
        	// Test player horizontal movement.
            if (localPlayerInput.isLeftPressed()) {
                player.moveLeft();
            } else if (localPlayerInput.isRightPressed()) {
                player.moveRight();
            }

            // Test player jump
            if (localPlayerInput.isUpPressed()) {
                player.jump();
            }
            
            // Throw a grenade.
            if (Gdx.input.isKeyPressed(Input.Keys.G)) {
            	if(player.canFireWeapon()) {
            		world.addBox(new Grenade(player, C.GRENADE_FUSE_MAX));
            		player.restartWeaponCooldown();
            	}
            }
            
            // Throw a rubber grenade.
            if (Gdx.input.isKeyPressed(Input.Keys.B)) {
            	if(player.canFireWeapon()) {
            		world.addBox(new RubberGrenade(player, C.GRENADE_FUSE_MAX));
            		player.restartWeaponCooldown();
            	}
            }
            
            // Throw a sticky grenade.
            if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            	if(player.canFireWeapon()) {
            		world.addBox(new StickyGrenade(player, C.GRENADE_FUSE_MAX));
            		player.restartWeaponCooldown();
            	}
            }
            
            // Throw a cluster grenade.
            if (Gdx.input.isKeyPressed(Input.Keys.C)) {
            	if(player.canFireWeapon()) {
            		world.addBox(new ClusterGrenade(player, C.GRENADE_FUSE_MAX));
            		player.restartWeaponCooldown();
            	}
            }
            
            // Throw a proximity mine.
            if (Gdx.input.isKeyPressed(Input.Keys.M)) {
            	if(player.canFireWeapon()) {
            		world.addBox(new ProximityMine(player));
            		player.restartWeaponCooldown();
            	}
            }
            
            // Throw a laser mine.
            if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            	if(player.canFireWeapon()) {
            		world.addBox(new LaserMine(player));
            		player.restartWeaponCooldown();
            	}
            }
            
            // Fire a Rocket.
            if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            	if(player.canFireWeapon()) {
            		world.addBox(new Rocket(player));
            		player.restartWeaponCooldown();
            	}
            }
        }
	}
}