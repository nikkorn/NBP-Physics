package com.dumbpug.main.gamedevicestesting.stage;

import java.util.ArrayList;
import com.dumbpug.main.gamedevicestesting.C;
import com.dumbpug.main.gamedevicestesting.maps.Map;
import com.dumbpug.main.gamedevicestesting.maps.SpawnPoint;
import com.dumbpug.main.gamedevicestesting.player.Player;
import com.dumbpug.main.gamedevicestesting.player.PlayerWeaponInventory;
import com.dumbpug.main.gamedevicestesting.round.Round;
import com.dumbpug.main.gamedevicestesting.round.RoundCountdownType;
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
			// Get the current round.
			Round currentRound = this.getCurrentRound();
			// Check to make sure that the count-down for this round has started.
			// Start it if it has not already been started.
			Countdown roundIntroCountdown = currentRound.getRoundCountdown(RoundCountdownType.INTRO);
			if(!roundIntroCountdown.hasCountdownStarted()){
				roundIntroCountdown.startCountdown();
			}
			// Update our physics world.
			world.update();
			// Get the count-down remainder. If 0 then the round should start!
			int countdownRemainder = roundIntroCountdown.getCountdownInSeconds();
			if(countdownRemainder == 0) {
				// The game should now be in progress.
				this.stageState = StageState.IN_PROGRESS;
			}
			break;
		case IN_PROGRESS:
			// Start the round count-down.
			Countdown roundCountdown = this.getCurrentRound().getRoundCountdown(RoundCountdownType.IN_GAME);
			if(!roundCountdown.hasCountdownStarted()){
				roundCountdown.startCountdown();
			}
			// Update our physics world.
			world.update();
			// Process player input
			for(Player player : this.getPlayers()) {
				player.processInput(this);
			}
			// Check for whether the round has been won.
			if(hasRoundBeenWon()) {
				this.stageState = StageState.ROUND_WON;
			}
			// Check to see if the round timer is finished.
			// Get the count-down remainder. If 0 then the round finished with no winners!
			int roundCountdownRemainder = roundCountdown.getCountdownInSeconds();
			if(roundCountdownRemainder == 0) {
				// The game should now show that time ran out for this round.
				this.stageState = StageState.ROUND_TIME_UP;
			}
			break;
		case ROUND_WON:
			// TODO Show the winner.
			// TODO Go to next round if there is one, or go to results.
			break;
		case ROUND_TIME_UP:
			// TODO Show that time is up.
			// TODO Go to next round if there is one, or go to results.
			break;
		case PENDING_RESULTS:
			// TODO Show the results.
			break;
		case PENDING_EXIT:
			break;
		default:
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
	 * Check whether this round has been one, also records player deaths against the current round.
	 * @return hasRoundBeenWon
	 */
	private boolean hasRoundBeenWon() {
		ArrayList<Player> winningPlayers = new ArrayList<Player>();
		// Get the living players.
		for(Player player : this.getPlayers()) {
			if(player.isAlive()) {
				winningPlayers.add(player);
			}
		}
		// If there is one player alive then they are the winner or this round.
		if(winningPlayers.size() == 1) {
			// Record this last living player as the round winner.
			this.getCurrentRound().addRoundWinner(winningPlayers.get(0));
			return true;
		}
		// If there are no living players then we need to check the round deaths to see who drew.
		if(winningPlayers.size() == 0) {
			// All players that have died but have not had their deaths recorded against the round have drawn.
			for(Player player : this.getPlayers()) {
				if(!this.getCurrentRound().getDeadPlayers().contains(player)) {
					// Record this player as a drawing round winner.
					this.getCurrentRound().addRoundWinner(player);
				}
			}
			return true;
		}
		// Record player deaths against the current round.
		for(Player player : this.getPlayers()) {
			if(!player.isAlive() && !this.getCurrentRound().getDeadPlayers().contains(player)) {
				this.getCurrentRound().recordPlayerDeath(player);
			}
		}
		// No winners yet.
		return false;
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
