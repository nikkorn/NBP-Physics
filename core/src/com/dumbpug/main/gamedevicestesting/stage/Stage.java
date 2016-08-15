package com.dumbpug.main.gamedevicestesting.stage;

import java.util.ArrayList;
import com.dumbpug.main.gamedevicestesting.C;
import com.dumbpug.main.gamedevicestesting.maps.Map;
import com.dumbpug.main.gamedevicestesting.maps.SpawnPoint;
import com.dumbpug.main.gamedevicestesting.player.Player;
import com.dumbpug.main.gamedevicestesting.player.PlayerWeaponInventory;
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
		// Set our initial stage state.
    	stageState = StageState.ROUND_STARTING;
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
			// TODO Wait for count down.
			// The game should now be in progress.
			this.stageState = StageState.IN_PROGRESS;
			break;
		case IN_PROGRESS:
			// Update our physics world.
			world.update();
			// Process player input
			for(Player player : this.getPlayers()) {
				player.processInput(this);
			}
			// Check to see if we have a winner (last guy who is not dead)
			// If so then the round is over. 
			// -----------------------------------------------------------
			// TODO Find a way to check for a draw as this will be common.
			// -----------------------------------------------------------
			if(getWinningPlayer() != null) {
				this.stageState = StageState.ROUND_WON;
			}
			break;
		case ROUND_WON:
			// TODO Show the winner.
			// TODO Go to next round if there is one, or go to results.
			break;
		case PENDING_RESULTS:
			// TODO Show the results.
			break;
		case PENDING_EXIT:
			break;
		}
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
	 * Try to get the last living player. If there are multiple living players then 
	 * we simply have no winner, return null.
	 * @return winning player.
	 */
	private Player getWinningPlayer() {
		Player winningPlayer  = null;
		int livingPlayerCount = 0;
		for(Player player : this.getPlayers()) {
			if(player.isAlive()) {
				livingPlayerCount++;
				winningPlayer = player;
			}
		}
		// If there is only one living player then we have a winner! else return null.
		return (livingPlayerCount == 1) ? winningPlayer : null;
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
	
	/**
	 * Get the stages physics world.
	 * @return stage physics world.
	 */
	public StagePhysicsWorld getStagePhysicsWorld() {
		return world;
	}
}
