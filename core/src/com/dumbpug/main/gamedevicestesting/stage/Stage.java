package com.dumbpug.main.gamedevicestesting.stage;

import java.util.ArrayList;
import com.dumbpug.main.gamedevicestesting.C;
import com.dumbpug.main.gamedevicestesting.maps.Map;
import com.dumbpug.main.gamedevicestesting.player.Player;

/**
 * Represents a stage.
 * @author nikolas.howard
 */
public class Stage {
	private StagePhysicsWorld world;
	private StageState stageState;
	private StageSettings stageSettings;
    private Map stageMap;
    private ArrayList<Player> players;
	
    /**
     * Initialise a new instance of the Stage class.
     * @param stageMap The map to be used for this stage.
     * @param players The list of participating players.
     * @param settings Stage settings.
     */
	public Stage(Map stageMap, ArrayList<Player> players, StageSettings settings) {
		this.stageMap      = stageMap;
		this.players       = players;
		this.stageSettings = settings;
		// Set up our stage.
		setup();
	}

	/**
	 * Setup this stage.
	 */
	private void setup() {
		// Create a blank physics world.
        world = new StagePhysicsWorld(C.WORLD_GRAVITY);
        // Populate our physics world with world tiles.
    	stageMap.populatePhysicsWorldWithTiles(world);
    	// Set our initial stage state.
    	stageState = StageState.ROUND_STARTING;
	}
	
	/**
	 * Our game tick.
	 */
	public void update() {
		// What we do in an update completely depends on the state of the stage.
		switch(stageState) {
		case ROUND_STARTING:
			break;
		case IN_PROCESS:
			break;
		case ROUND_WON:
			break;
		case PENDING_RESULTS:
			break;
		case PENDING_EXIT:
			break;
		}
	}

	/**
	 * Get the stage settings.
	 * @return stage settings.
	 */
	public StageSettings getStageSettings() {
		return stageSettings;
	}

	/**
	 * Get the state of the stage.
	 * @return stage state.
	 */
	public StageState getStageState() {
		return stageState;
	}

	/**
	 * Get the players of this stage.
	 * @return players.
	 */
	public ArrayList<Player> getPlayers() {
		return players;
	}
}
