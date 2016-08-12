package com.dumbpug.main.gamedevicestesting;

import java.util.ArrayList;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.main.gamedevicestesting.input.IPlayerInput;
import com.dumbpug.main.gamedevicestesting.input.LocalPlayerInput;
import com.dumbpug.main.gamedevicestesting.maps.LocalMaps;
import com.dumbpug.main.gamedevicestesting.maps.Map;
import com.dumbpug.main.gamedevicestesting.player.Player;
import com.dumbpug.main.gamedevicestesting.stage.StagePhysicsWorld;
import com.dumbpug.main.gamedevicestesting.weapons.ClusterGrenade;
import com.dumbpug.main.gamedevicestesting.weapons.Grenade;
import com.dumbpug.main.gamedevicestesting.weapons.LaserMine;
import com.dumbpug.main.gamedevicestesting.weapons.ProximityMine;
import com.dumbpug.main.gamedevicestesting.weapons.Rocket;
import com.dumbpug.main.gamedevicestesting.weapons.RubberGrenade;
import com.dumbpug.main.gamedevicestesting.weapons.StickyGrenade;
import com.dumbpug.main.gamedevicestesting.weapons.WeaponType;
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
    Map stageMap;

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

        // Create a blank physics world.
        world = new StagePhysicsWorld(C.WORLD_GRAVITY);
        
        // Get the test local map, a map would usually be provided.
        ArrayList<Map> localMaps = LocalMaps.getLocalMaps();
        // We need our test map.
        if(localMaps.size() > 0) {
        	// Set the stage map.
        	this.stageMap = localMaps.get(0);
        	// Populate our physics world with world tiles.
        	this.stageMap.populatePhysicsWorldWithTiles(world);
        } else {
        	System.out.println("Error: cannot load test map.");
        }
   
        // Make our player box.
        player = new Player(65, 200, C.PLAYER_SIZE_WIDTH, C.PLAYER_SIZE_HEIGHT, 1);
        
        // Give our player some grenades to play with.
        player.getPlayerWeaponInventory().setWeaponAmmunition(WeaponType.GRENADE, 10);
        world.addBox(player.getPlayerPhysicsBox());
        
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
		// Set the players angle of focus.
 		player.setAngleOfFocus(playerInput.getAngleOfFocus());
 		
 		// Check for presses of number 1-7, this represents a change in active weapon.
 		if(playerInput.isNum1Pressed()) {
 			player.getPlayerWeaponInventory().setActiveWeaponType(WeaponType.GRENADE);
 		}
 		if(playerInput.isNum2Pressed()) {
 			player.getPlayerWeaponInventory().setActiveWeaponType(WeaponType.STICKY_GRENADE);
 		}
 		if(playerInput.isNum3Pressed()) {
 			player.getPlayerWeaponInventory().setActiveWeaponType(WeaponType.CLUSTER_GRENADE);
 		}
 		if(playerInput.isNum4Pressed()) {
 			player.getPlayerWeaponInventory().setActiveWeaponType(WeaponType.RUBBER_GRENADE);
 		}
 		if(playerInput.isNum5Pressed()) {
 			player.getPlayerWeaponInventory().setActiveWeaponType(WeaponType.PROXIMITY_MINE);
 		}
 		if(playerInput.isNum6Pressed()) {
 			player.getPlayerWeaponInventory().setActiveWeaponType(WeaponType.LASER_MINE);
 		}
 		if(playerInput.isNum7Pressed()) {
 			player.getPlayerWeaponInventory().setActiveWeaponType(WeaponType.ROCKET);
 		}
        
        // Only allow player to do stuff while he is alive.
        if(!player.isAlive()) {
        	return;
        } 
        	
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
        
        // Handle weapon fire.
        if(playerInput.isLeftMouseButtonDown()) {
        	// Can the player even fire his weapon? (cool-down over)
        	if(player.canFireWeapon()) {
        		// Get the players active weapon.
        		WeaponType activeWeaponType = player.getPlayerWeaponInventory().getActiveWeaponType();
        		// Does the player have the required ammo?
        		int ammoCount = player.getPlayerWeaponInventory().getWeaponAmmunition(activeWeaponType);
        		if(ammoCount != 0) {
        			// An ammo count of -1 represents infinite ammo, if we don't have infinite ammo 
        			// then we need to remove some ammo from the inventory.
        			if(ammoCount != -1) {
        				player.getPlayerWeaponInventory().setWeaponAmmunition(activeWeaponType, ammoCount - 1);
        			}
        			// We can fire now!
        			switch(activeWeaponType) {
					case CLUSTER_GRENADE:
						world.addBox(new ClusterGrenade(player, C.GRENADE_FUSE_MAX));
						break;
					case GRENADE:
						world.addBox(new Grenade(player, C.GRENADE_FUSE_MAX));
						break;
					case LASER_MINE:
						world.addBox(new LaserMine(player));
						break;
					case PROXIMITY_MINE:
						world.addBox(new ProximityMine(player));
						break;
					case ROCKET:
						world.addBox(new Rocket(player));
						break;
					case RUBBER_GRENADE:
						world.addBox(new RubberGrenade(player, C.GRENADE_FUSE_MAX));
						break;
					case STICKY_GRENADE:
						world.addBox(new StickyGrenade(player, C.GRENADE_FUSE_MAX));
						break;
        			}
            		// We have fired our weapon, restart the weapon cool-down.
            		player.restartWeaponCooldown();
        		} 
        	}
        }
	}
}