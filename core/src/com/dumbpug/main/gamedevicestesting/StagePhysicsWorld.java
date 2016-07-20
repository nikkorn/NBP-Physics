package com.dumbpug.main.gamedevicestesting;

import java.util.ArrayList;
import com.dumbpug.main.gamedevicestesting.weapons.ProximityMine;
import com.dumbpug.nbp.NBPBox;
import com.dumbpug.nbp.NBPMath;
import com.dumbpug.nbp.NBPWorld;

/**
 * Physics world for our stage.
 * @author nikolas.howard
 *
 */
public class StagePhysicsWorld extends NBPWorld {
	// Player instances that have been added to the world.
	ArrayList<PlayerBox> players = new ArrayList<PlayerBox>();
	
	public StagePhysicsWorld(float gravity) {
		super(gravity);
	}
	
	@Override
	public void addBox(NBPBox box) {
		super.addBox(box);
		// Check to see if this box is a player, if it is grab a reference to him.
		if(box instanceof PlayerBox) {
			players.add((PlayerBox) box);
		}
	}
	
	@Override
	public void onAfterWorldUpdate() {
		// Check all of our mines.
		checkMines();
	}

	/**
	 * Go over all proximity/laser mines and detonate them if certain conditions are met.
	 */
	private void checkMines() {
		for(NBPBox box : this.getWorldBoxes()) {
			// Is this box a proximity mine?
			if(box.getName().equals("PROXIMITY_MINE")) {
				ProximityMine mine = (ProximityMine) box;
				// Check that this mine is primed (ready for detonation).
				if(mine.isPrimed()) {
					// Check to see if any players are in the range of this mine.
					for(PlayerBox player : players) {
						float playerDistance = NBPMath.getDistanceBetweenPoints(player.getCurrentOriginPoint(), mine.getCurrentOriginPoint());
						if(playerDistance <= C.PROXIMITY_MINE_REACH) {
							// Detonate!
							mine.detonate();
							// We don't care if any other players are within range anymore.
							break;
						}
					}
				}
			}
			// Is this box a laser mine?
			if(box.getName().equals("LASER_MINE")) {
				// TODO handle detonation if need be.
			}
		}
	}
}
