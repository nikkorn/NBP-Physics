package com.dumbpug.main.gamedevicestesting.stage;

import java.util.ArrayList;
import com.dumbpug.main.gamedevicestesting.C;
import com.dumbpug.main.gamedevicestesting.maps.Map;
import com.dumbpug.main.gamedevicestesting.maps.SpawnPoint;
import com.dumbpug.main.gamedevicestesting.player.Player;
import com.dumbpug.main.gamedevicestesting.player.PlayerWeaponInventory;
import com.dumbpug.main.gamedevicestesting.round.Round;
import com.dumbpug.main.gamedevicestesting.stage.state.StatePendingExit;
import com.dumbpug.main.gamedevicestesting.stage.state.StateResults;
import com.dumbpug.main.gamedevicestesting.stage.state.StateRoundInProgress;
import com.dumbpug.main.gamedevicestesting.stage.state.StateRoundStarting;
import com.dumbpug.main.gamedevicestesting.stage.state.StateRoundTimeUp;
import com.dumbpug.main.gamedevicestesting.stage.state.StateRoundWon;
import com.dumbpug.main.gamedevicestesting.weapons.ClusterGrenade;
import com.dumbpug.main.gamedevicestesting.weapons.Grenade;
import com.dumbpug.main.gamedevicestesting.weapons.LaserMine;
import com.dumbpug.main.gamedevicestesting.weapons.ProximityMine;
import com.dumbpug.main.gamedevicestesting.weapons.Rocket;
import com.dumbpug.main.gamedevicestesting.weapons.RubberGrenade;
import com.dumbpug.main.gamedevicestesting.weapons.StickyGrenade;
import com.dumbpug.main.gamedevicestesting.weapons.WeaponType;

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
    private ArrayList<Round> rounds;
    
    /**
     * State handlers. State specific logic is handled by these to keep this class clean.
     */
    private StateRoundStarting stateRoundStarting;
    private StateRoundInProgress stateRoundInProgress;
    private StateRoundWon stateRoundWon;
    private StateRoundTimeUp stateRoundTimeUp;
    private StateResults stateResults;
    private StatePendingExit statePendingExit;
    
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
		setupWorld();
		setupPlayers();
		// Add our initial round.
		rounds = new ArrayList<Round>();
		rounds.add(new Round());
		// Set our initial stage state.
    	stageState = StageState.ROUND_STARTING;
    	// Create our state handlers.
    	stateRoundStarting   = new StateRoundStarting();
        stateRoundInProgress = new StateRoundInProgress();
        stateRoundWon        = new StateRoundWon();
        stateRoundTimeUp     = new StateRoundTimeUp();
        stateResults         = new StateResults();
        statePendingExit     = new StatePendingExit();
	}

	/**
	 * Setup this stage world.
	 */
	private void setupWorld() {
		// Create a blank physics world.
        world = new StagePhysicsWorld(C.WORLD_GRAVITY);
        // Populate our physics world with world tiles.
    	stageMap.populatePhysicsWorldWithTiles(world);
	}
	
	/**
	 * Setup the stages players.
	 */
	private void setupPlayers() {
		// Get the spawn points from the map.
		ArrayList<SpawnPoint> spawnPoints = stageMap.getPlayerSpawnPoints();
		// Get the shared weapon inventory from the stage settings.
		PlayerWeaponInventory sharedInventory = stageSettings.getSharedWeaponInventory();
		// Set up each player for this stage.
		for(int playerIndex = 0; playerIndex < players.size(); playerIndex++) {
			// Get the current player.
			Player player = players.get(playerIndex);
			// Firstly, we need to add the players physics box to the stage physics world.
			world.addBox(player.getPlayerPhysicsBox());
			// Get a spawn point for this player.
			SpawnPoint availableSpawnPoint = (playerIndex < spawnPoints.size()) ? spawnPoints.get(playerIndex): spawnPoints.get(0); 
			player.moveToSpawnPoint(availableSpawnPoint);
			// Give this player a weapon inventory (if a shared one was defined in settings).
			if(sharedInventory != null) {
				// Create a brand-spanking new weapon inventory for our player ...
				PlayerWeaponInventory newWeaponInventory = new PlayerWeaponInventory();
				// ... and fill it with the same levels of ammo as found in the shared inventory.
				for(WeaponType weaponType : WeaponType.values()) {
					newWeaponInventory.setWeaponAmmunition(weaponType, sharedInventory.getWeaponAmmunition(weaponType));
				}
				// Give the new weapon inventory to the player.
				player.setPlayerWeaponInventory(newWeaponInventory);
			}
		}
	}
	
	/**
	 * Our game tick.
	 */
	public void update() {
		// What we do in an update completely depends on the state of the stage.
		switch(stageState) {
		case ROUND_STARTING:
			stateRoundStarting.update(this);
			break;
		case IN_PROGRESS:
			stateRoundInProgress.update(this);
			break;
		case ROUND_WON:
			stateRoundWon.update(this);
			break;
		case ROUND_TIME_UP:
			stateRoundTimeUp.update(this);
			break;
		case PENDING_RESULTS:
			stateResults.update(this);
			break;
		case PENDING_EXIT:
			statePendingExit.update(this);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Start the next round.
	 */
	public void startNextRound() {
		// TODO Add fresh round.
		// TODO Reset EVERYTHING!.
		// TODO Set stage state to be ROUND_STARTING.
	}
	
	/**
	 * Called by a player when they fire their weapon.
	 * @param player
	 */
	public void weaponFiredByPlayer(Player player) {
		// Create a projectile element based on the players active weapon.
		switch(player.getPlayerWeaponInventory().getActiveWeaponType()) {
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
	 * Set the state of the stage.
	 * @param stage state.
	 */
	public void setStageState(StageState state) {
		this.stageState = state;
	}

	/**
	 * Get the players of this stage.
	 * @return players.
	 */
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Get the stages physics world.
	 * @return stage physics world.
	 */
	public StagePhysicsWorld getStagePhysicsWorld() {
		return world;
	}
	
	/**
	 * Get the stages rounds.
	 * @return stage rounds.
	 */
	public ArrayList<Round> getStageRounds() {
		return this.rounds;
	}
	
	/**
	 * Get the current round.
	 * @return current round.
	 */
	public Round getCurrentRound() {
		return this.rounds.get(rounds.size() - 1);
	}
}
