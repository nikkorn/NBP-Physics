package com.dumbpug.main.gamedevicestesting.stage.state;

import com.dumbpug.main.gamedevicestesting.stage.Stage;

/**
 * Represents an actual state in a stage.
 * @author nikolas.howard
 */
public interface State {
	/**
	 * Called by the stage when the stage is in this current state.
	 * @param stage current stage.
	 */
	public void update(Stage stage);
}
