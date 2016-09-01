package com.dumbpug.main.gamedevicestesting.stage.state;

import com.dumbpug.main.gamedevicestesting.round.RoundCountdownType;
import com.dumbpug.main.gamedevicestesting.stage.Countdown;
import com.dumbpug.main.gamedevicestesting.stage.Stage;
import com.dumbpug.main.gamedevicestesting.stage.StageState;

/**
 * @author nikolas.howard
 */
public class StateRoundWon implements State {

	@Override
	public void update(Stage stage) {
		// Start the count-down for the end of round results.
		Countdown roundResultCountdown = stage.getCurrentRound().getRoundCountdown(RoundCountdownType.ROUND_RESULT);
		if(!roundResultCountdown.hasCountdownStarted()){
			roundResultCountdown.startCountdown();
		}
		// Update players of round results.
		broadcastRoundResults(stage);
		// Update our physics world.
		stage.getStagePhysicsWorld().update();
		// Get the count-down remainder. If 0 then we need to move on.
		int roundResultCountdownRemainder = roundResultCountdown.getCountdownInSeconds();
		if(roundResultCountdownRemainder == 0) {
			// Do we go to the next round or all rounds finished?
			if(stage.getStageSettings().getNumberOfRounds() > stage.getStageRounds().size()) {
				// Ask the stage to start the next round.
				stage.startNextRound();
			} else {
				// There are no more rounds to be had. Show the stage results.
				stage.setStageState(StageState.PENDING_RESULTS);
			}
		}
	}

	/**
	 * Broadcast stage results to all players.
	 * @param stage
	 */
	private void broadcastRoundResults(Stage stage) {
		// TODO Broadcast round results.
	}
}
