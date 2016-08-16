package com.dumbpug.main.gamedevicestesting.stage;

import com.dumbpug.main.gamedevicestesting.C;

/**
 * Represents a game round.
 * @author nikolas.howard
 */
public class Round {
	private boolean hasCountdownStarted = false;
	private long countdownStart = 0;
	
	/**
	 * Get the remainder of the count-down in seconds.
	 * @return remainder
	 */
	public int getCountdownInSeconds() {
		if(!hasCountdownStarted) {
			return C.STAGE_DEFAULT_ROUND_COUNT;
		}
		long countdownTarget = countdownStart + (C.STAGE_DEFAULT_ROUND_COUNT*1000);
		long currentTime     = System.currentTimeMillis(); 
		if(currentTime >= countdownTarget) {
			return 0;
		} else {
			return (int) Math.ceil((double) (countdownTarget-currentTime) / 1000);
		}
	}

	/**
	 * Get whether the count-down has started for this round.
	 * @return hasCountdownStarted
	 */
	public boolean hasCountdownStarted() {
		return hasCountdownStarted;
	}

	/**
	 * Start the count-down for this round.
	 */
	public void startCountdown() {
		this.hasCountdownStarted = true;
		countdownStart           = System.currentTimeMillis();
	}
}
