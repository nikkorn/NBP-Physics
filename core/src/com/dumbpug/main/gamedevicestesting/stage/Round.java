package com.dumbpug.main.gamedevicestesting.stage;

import java.util.ArrayList;
import java.util.HashMap;
import com.dumbpug.main.gamedevicestesting.C;
import com.dumbpug.main.gamedevicestesting.player.Player;

/**
 * Represents a game round.
 * @author nikolas.howard
 */
public class Round {
	private ArrayList<Player> roundDeaths                      = new ArrayList<Player>();
	private ArrayList<Player> roundWinners                     = new ArrayList<Player>();
	private HashMap<RoundCountdownType, Countdown> roundCountdowns = new HashMap<RoundCountdownType, Countdown>();
	
	/**
	 * Create a new instance of Round.
	 */
	public Round() {
		// Set up our round count-downs.
		roundCountdowns.put(RoundCountdownType.INTRO, new Countdown(C.STAGE_DEFAULT_ROUND_INTRO_TIME));
		roundCountdowns.put(RoundCountdownType.IN_GAME, new Countdown(C.STAGE_DEFAULT_ROUND_TIME));
		roundCountdowns.put(RoundCountdownType.ROUND_RESULT, new Countdown(C.STAGE_DEFAULT_ROUND_RESULT_TIME));
	}

	/**
	 * Get players that have died during this round.
	 * @return list of dead players.
	 */
	public ArrayList<Player> getDeadPlayers() {
		return roundDeaths;
	}

	/**
	 * Record a player death against this round.
	 * @param deadPlayer
	 */
	public void recordPlayerDeath(Player deadPlayer) {
		this.roundDeaths.add(deadPlayer);
	}

	/**
	 * Get the winning players of this round.
	 * @return winning players.
	 */
	public ArrayList<Player> getRoundWinners() {
		return roundWinners;
	}

	/**
	 * Add a round winner. (can be draws)
	 * @param round Winner
	 */
	public void addRoundWinner(Player roundWinner) {
		this.roundWinners.add(roundWinner);
	}

	/**
	 * Get a count-down used by this round.
	 * @param roundCountdown
	 * @return count-down
	 */
	public Countdown getRoundCountdown(RoundCountdownType roundCountdown) {
		return roundCountdowns.get(roundCountdown);
	}
}
