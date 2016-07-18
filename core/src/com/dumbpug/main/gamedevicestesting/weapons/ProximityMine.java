package com.dumbpug.main.gamedevicestesting.weapons;

import com.dumbpug.main.gamedevicestesting.C;
import com.dumbpug.nbp.NBPBloom;
import com.dumbpug.nbp.NBPBox;
import com.dumbpug.nbp.NBPBoxType;
import com.dumbpug.nbp.NBPIntersectionPoint;
import com.dumbpug.nbp.NBPSensor;

public class ProximityMine extends NBPBox {
	// Defines whether this mine has hit a static box and has been set.
	private boolean isPrimed = false;
	// Facing direction of the mine, defaults up for when it hasn't stuck to anything yet,
	private MineDirection direction = MineDirection.UP;

	public ProximityMine(float x, float y, float width, float height, NBPBoxType type) {
		super(x, y, width, height, type);
		this.setName("PROXIMITY_MINE");
	}
	
	/**
	 * Get the facing direction of this mine.
	 * @return facing direction
	 */
	public MineDirection getFacingDirection() {
		return this.direction;
	}
	
	/**
	 * Get whether this mine is primed.
	 * @return is primed
	 */
	public boolean isPrimed() {
		return isPrimed;
	}

	/**
	 * Set whether this mine is primed.
	 * @param isSet
	 */
	public void setPrimed(boolean isSet) {
		this.isPrimed = isSet;
	}
	
	/**
	 * Detonate this mine.
	 */
	public void detonate() {
		// Explode! Add a world bloom.
		NBPBloom mineExplosionBloom = new NBPBloom(this.getCurrentOriginPoint().getX(), 
				this.getCurrentOriginPoint().getY(), C.PROXIMITY_MINE_RADIUS, C.PROXIMITY_MINE_FORCE);
		this.getWrappingWorld().addBloom(mineExplosionBloom);
		// Remove this mine box on next update.
		this.markForDeletion();
	}

	@Override
	protected void onCollisonWithStaticBox(NBPBox collidingBox, NBPIntersectionPoint originAtCollision) {
		// We have hit a static box, so prime this mine.
		// Firstly, kill this box so it remains stationary
		this.kill();
		// Record the direction of the collision intersection, the facing direction
		// of the mine will be the opposite of this.
		switch(originAtCollision.getIntersectionDir()) {
		case BOTTOM:
			this.direction = MineDirection.DOWN;
			break;
		case SIDE_LEFT:
			this.direction = MineDirection.LEFT;
			break;
		case SIDE_RIGHT:
			this.direction = MineDirection.RIGHT;
			break;
		case TOP:
			this.direction = MineDirection.UP;
			break;
		case NONE:
			break;
		}
		// Set this mine as being primed now, any player which passes into its reach will detonate it.
		this.setPrimed(true);
	}
	
	@Override
	protected void onCollisonWithKineticBox(NBPBox collidingBox, NBPIntersectionPoint kinematicBoxOriginAtCollision) {}

	@Override
	protected void onSensorEntry(NBPSensor sensor, NBPBox enteredBox) {}

	@Override
	protected void onSensorExit(NBPSensor sensor, NBPBox exitedBox) {}

	@Override
	protected boolean onBloomPush(NBPBloom bloom, float angleOfForce, float force, float distance) { return true; }

	@Override
	protected void onBeforeUpdate() {}

	@Override
	protected void onAfterUpdate() {}

	@Override
	protected void onDeletion() {}
}
