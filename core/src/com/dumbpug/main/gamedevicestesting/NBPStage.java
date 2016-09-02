package com.dumbpug.main.gamedevicestesting;

import java.util.ArrayList;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.main.gamedevicestesting.input.LocalPlayerInput;
import com.dumbpug.main.gamedevicestesting.maps.LocalMaps;
import com.dumbpug.main.gamedevicestesting.maps.Map;
import com.dumbpug.main.gamedevicestesting.player.Player;
import com.dumbpug.main.gamedevicestesting.player.PlayerWeaponInventory;
import com.dumbpug.main.gamedevicestesting.stage.Stage;
import com.dumbpug.main.gamedevicestesting.stage.StageSettings;
import com.dumbpug.main.gamedevicestesting.weapons.ProximityMine;
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

    Stage stage;

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
        
        // Get the test local map, a map would usually be provided.
        Map stageMap = null;
        ArrayList<Map> localMaps = LocalMaps.getLocalMaps();
        // We need our test map.
        if(localMaps.size() > 0) {
        	// Set the stage map.
        	stageMap = localMaps.get(0);
        } else {
        	System.out.println("Error: cannot load test map.");
        }
   
        // Make our player.
        Player player = new Player(65, 200, 1);
        // Create our (local) player input interface.
        LocalPlayerInput localPlayerInput = new LocalPlayerInput(player);
        player.setPlayerInputProvider(localPlayerInput);
        // Add this player to a list of players.
        ArrayList<Player> playersList = new ArrayList<Player>();
        playersList.add(player);
        // Add a dummy player.
        playersList.add(new Player(0, 0, 2));
        
        // Create a shared weapon inventory for all of our players.
        PlayerWeaponInventory inventory = new PlayerWeaponInventory();
        inventory.setWeaponAmmunition(WeaponType.GRENADE, 1000);
        inventory.setWeaponAmmunition(WeaponType.ROCKET, 5);
        
        // Create stage settings.
        StageSettings settings = new StageSettings();
        settings.setSecondsPerRound(1000);
        settings.setSharedWeaponInventory(inventory);
        settings.setNumberOfRounds(3);
        
        // Make our actual stage.
        stage = new Stage(stageMap, playersList, settings);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
        
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
 		
 		// Is this stage finished with? (game over)
 		if(stage.isStageFinished()) {
 			// Usually return to a lobby, but this will do
 			Gdx.app.exit();
 		} else {
 			// Update our stage.
 	 		stage.update();
 		}
        
        batch.begin();
        // Draw Grid
        for(NBPBox box : stage.getStagePhysicsWorld().getWorldBoxes()) {
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
}