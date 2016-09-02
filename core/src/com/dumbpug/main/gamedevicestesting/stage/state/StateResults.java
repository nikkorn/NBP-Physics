package com.dumbpug.main.gamedevicestesting.stage.state;

import com.dumbpug.main.gamedevicestesting.C;
import com.dumbpug.main.gamedevicestesting.player.Player;
import com.dumbpug.main.gamedevicestesting.round.Round;
import com.dumbpug.main.gamedevicestesting.stage.Countdown;
import com.dumbpug.main.gamedevicestesting.stage.Stage;
import com.dumbpug.main.gamedevicestesting.stage.StageState;

/**
 * @author nikolas.howard
 */
public class StateResults implements State {
	// Count-down for viewing our game results.
	Countdown gameResultsCountdown;
	
	/**
	 * Create a new instance of the StateResults class.
	 */
	public StateResults() {
		gameResultsCountdown = new Countdown(C.STAGE_DEFAULT_GAME_RESULT_TIME);
	}

	@Override
	public void update(Stage stage) {
		// Start the count-down for the end of game results.
		if(!gameResultsCountdown.hasCountdownStarted()){
			// Update players of overall results.
			broadcastOverallResults(stage);
			// Start the count-down until next round or end of stage.
			gameResultsCountdown.startCountdown();
		}
		// Update our physics world.
		stage.getStagePhysicsWorld().update();
		// Get the count-down remainder. If 0 then we need to move on.
		int gameResultCountdownRemainder = gameResultsCountdown.getCountdownInSeconds();
		// Exit the stage after celebrations.
		if(gameResultCountdownRemainder == 0) {
			stage.setStageState(StageState.PENDING_EXIT);
		}
	}

	/**
	 * Broadcast overall results to all players.
	 * @param stage The stage.
	 */
	private void broadcastOverallResults(Stage stage) {
		// TODO Broadcast game results.
		System.out.println("Scores : ");
		for(Player player : stage.getCurrentRound().getRoundWinners()){
			int playerScore = 0;
			// Sum score from each round.
			for(Round round : stage.getStageRounds()) {
				for(Player winner : round.getRoundWinners()) {
					if(winner == player) {
						playerScore++;
					}
				}
			}
			System.out.println(player.getPlayerPhysicsBox().getName() + " : " + playerScore);
		}
	}
}
