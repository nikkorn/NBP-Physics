package com.dumbpug.main.gamedevicestesting.stage.state;

import java.util.ArrayList;
import com.dumbpug.main.gamedevicestesting.player.Player;
import com.dumbpug.main.gamedevicestesting.round.RoundCountdownType;
import com.dumbpug.main.gamedevicestesting.stage.Countdown;
import com.dumbpug.main.gamedevicestesting.stage.Stage;
import com.dumbpug.main.gamedevicestesting.stage.StageState;

/**
 * @author nikolas.howard
 */
public class StateRoundInProgress implements State {

	@Override
	public void update(Stage stage) {
		// Start the round count-down.
		Countdown roundCountdown = stage.getCurrentRound().getRoundCountdown(RoundCountdownType.IN_GAME);
		if(!roundCountdown.hasCountdownStarted()){
			roundCountdown.startCountdown();
		}
		// Update our physics world.
		stage.getStagePhysicsWorld().update();
		// Process player input
		for(Player player : stage.getPlayers()) {
			player.processInput(stage);
		}
		// Check for whether the round has been won.
		if(hasRoundBeenWon(stage)) {
			stage.setStageState(StageState.ROUND_WON);
		}
		// Check to see if the round timer is finished.
		// Get the count-down remainder. If 0 then the round finished with no winners!
		int roundCountdownRemainder = roundCountdown.getCountdownInSeconds();
		if(roundCountdownRemainder == 0) {
			// The game should now show that time ran out for this round.
			stage.setStageState(StageState.ROUND_TIME_UP);
		}
	}
	
	/**
	 * Check whether this round has been won, also records player deaths against the current round.
	 * @param stage
	 * @return hasRoundBeenWon
	 */
	private boolean hasRoundBeenWon(Stage stage) {
		ArrayList<Player> winningPlayers = new ArrayList<Player>();
		// Get the living players.
		for(Player player : stage.getPlayers()) {
			if(player.isAlive()) {
				winningPlayers.add(player);
			}
		}
		// If there is one player alive then they are the winner or this round.
		if(winningPlayers.size() == 1) {
			// Record this last living player as the round winner.
			stage.getCurrentRound().addRoundWinner(winningPlayers.get(0));
			return true;
		}
		// If there are no living players then we need to check the round deaths to see who drew.
		if(winningPlayers.size() == 0) {
			// All players that have died but have not had their deaths recorded against the round have drawn.
			for(Player player : stage.getPlayers()) {
				if(!stage.getCurrentRound().getDeadPlayers().contains(player)) {
					// Record this player as a drawing round winner.
					stage.getCurrentRound().addRoundWinner(player);
				}
			}
			return true;
		}
		// Record player deaths against the current round.
		for(Player player : stage.getPlayers()) {
			if(!player.isAlive() && !stage.getCurrentRound().getDeadPlayers().contains(player)) {
				stage.getCurrentRound().recordPlayerDeath(player);
			}
		}
		// No winners yet.
		return false;
	}
}
