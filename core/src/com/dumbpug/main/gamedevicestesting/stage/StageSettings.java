package com.dumbpug.main.gamedevicestesting.stage;

import com.dumbpug.main.gamedevicestesting.C;

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
}
