package com.dumbpug.main.gamedevicestesting.stage;

import com.dumbpug.main.gamedevicestesting.C;
import com.dumbpug.main.gamedevicestesting.player.PlayerWeaponInventory;

/**
 * Settings object to be passed when setting up a stage.
 * @author nikolas.howard
 *
 */
public class StageSettings {
	// The number of rounds to be had per game.
	private int numberOfRounds = C.STAGE_DEFAULT_ROUND_COUNT;
	// Time allowed for each round (seconds). (0 means no timer)
	private int secondsPerRound = C.STAGE_DEFAULT_ROUND_TIME;
	// Shared player inventory. If this is provided then it is 
	// copied for ever player that joins the stage.
	private PlayerWeaponInventory sharedWeaponInventory = null;
	
	public int getNumberOfRounds() {
		return numberOfRounds;
	}
	public void setNumberOfRounds(int numberOfRounds) {
		this.numberOfRounds = numberOfRounds;
	}
	public int getSecondsPerRound() {
		return secondsPerRound;
	}
	public void setSecondsPerRound(int secondsPerRound) {
		this.secondsPerRound = secondsPerRound;
	}
	public PlayerWeaponInventory getSharedWeaponInventory() {
		return sharedWeaponInventory;
	}
	public void setSharedWeaponInventory(PlayerWeaponInventory sharedWeaponInventory) {
		this.sharedWeaponInventory = sharedWeaponInventory;
	}
}
