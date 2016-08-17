package com.dumbpug.main.gamedevicestesting.stage;

/**
 * A Game Count-down.
 * @author nikolas.howard
 */
public class Countdown {
	private boolean hasCountdownStarted    = false;
	private long countdownStart            = 0;
	private int countdownInSeconds         = 0;
	
	/**
	 * Create a new instance of Countdown.
	 * @param countdownInSeconds
	 */
	public Countdown(int countdownInSeconds) {
		this.countdownInSeconds = countdownInSeconds;
	}
	
	/**
	 * Get the remainder of the count-down in seconds.
	 * @return remainder
	 */
	public int getCountdownInSeconds() {
		if(!hasCountdownStarted) {
			return countdownInSeconds;
		}
		long countdownTarget = countdownStart + (countdownInSeconds*1000);
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
