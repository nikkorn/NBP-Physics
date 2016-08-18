package com.dumbpug.main.gamedevicestesting.stage.state;

import com.dumbpug.main.gamedevicestesting.round.Round;
import com.dumbpug.main.gamedevicestesting.round.RoundCountdownType;
import com.dumbpug.main.gamedevicestesting.stage.Countdown;
import com.dumbpug.main.gamedevicestesting.stage.Stage;
import com.dumbpug.main.gamedevicestesting.stage.StageState;

/**
 * @author nikolas.howard
 */
public class StateRoundStarting implements State {

	@Override
	public void update(Stage stage) {
		// Get the current round.
		Round currentRound = stage.getCurrentRound();
		// Check to make sure that the count-down for this round has started.
		// Start it if it has not already been started.
		Countdown roundIntroCountdown = currentRound.getRoundCountdown(RoundCountdownType.INTRO);
		if(!roundIntroCountdown.hasCountdownStarted()){
			roundIntroCountdown.startCountdown();
		}
		// Update our physics world.
		stage.getStagePhysicsWorld().update();
		// Get the count-down remainder. If 0 then the round should start!
		int countdownRemainder = roundIntroCountdown.getCountdownInSeconds();
		if(countdownRemainder == 0) {
			// The game should now be in progress.
			stage.setStageState(StageState.IN_PROGRESS);
		}
	}
}
