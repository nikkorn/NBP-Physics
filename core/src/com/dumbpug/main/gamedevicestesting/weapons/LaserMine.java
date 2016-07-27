package com.dumbpug.main.gamedevicestesting.weapons;

import com.dumbpug.main.gamedevicestesting.C;
import com.dumbpug.main.gamedevicestesting.player.Player;
import com.dumbpug.nbp.NBPBloom;
import com.dumbpug.nbp.NBPBox;
import com.dumbpug.nbp.NBPBoxType;
import com.dumbpug.nbp.NBPIntersectionPoint;
import com.dumbpug.nbp.NBPSensor;

public class LaserMine extends NBPBox implements Mine{
	// Defines whether this mine has hit a static box and has been set.
	private boolean isPrimed = false;
	// Facing direction of the mine, defaults up for when it hasn't stuck to anything yet,
	private MineDirection direction = MineDirection.UP;
	// Is there a player in range of the laser?
	private boolean isPlayerInLaserRange = false;

	/**
	 * Create a new instance of the ProximityMine class.
	 * @param owner
	 */
	public LaserMine(Player owner) {
		super(owner.getCurrentOriginPoint().getX(), owner.getCurrentOriginPoint().getY(), C.GRENADE_SIZE, C.GRENADE_SIZE, NBPBoxType.KINETIC);
		this.setName("LASER_MINE");
        // Set various properties for the grenade.
        setFriction(C.MINE_FRICTION);
        setRestitution(C.MINE_RESTITUTION);
        // Set max velocity. 
        setMaxVelocityX(C.MINE_MAX_VELOCITY);
        setMaxVelocityY(C.MINE_MAX_VELOCITY);
        // Apply initial impulse in the players facing direction
        this.applyImpulse((float) Math.cos(owner.getAngleOfFocus()) * C.MINE_INITIAL_VELOCITY, (float) Math.sin(owner.getAngleOfFocus()) * C.MINE_INITIAL_VELOCITY);
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
	 * Gets whether this mine needs to be triggered as a player has wandered into the lasers reach.
	 * @return isPlayerInLaserRange
	 */
	public boolean isPlayerInLaserRange() {
		return isPlayerInLaserRange;
	}
	
	/**
	 * Detonate this mine.
	 */
	public void detonate() {
		// This type of mine will not explode, it instead will fire a rocket along the path of the laser.
		LaserMineRocket laserMineRocket = new LaserMineRocket(this);
		this.getWrappingWorld().addBox(laserMineRocket);
		// Remove this mine box on next update.
		this.markForDeletion();
	}

	@Override
	protected void onCollisonWithStaticBox(NBPBox collidingBox, NBPIntersectionPoint originAtCollision) {
		// We have hit a static box, so prime this mine.
		// Firstly, kill this box so it remains stationary
		this.kill();
		// Create our sensor which will act as our laser beam.
		NBPSensor baseSensor = null;
		// Record the direction of the collision intersection, the facing direction
		// of the mine will be the opposite of this.
		switch(originAtCollision.getIntersectionDir()) {
		case BOTTOM:
			this.direction = MineDirection.DOWN;
			baseSensor = new NBPSensor(this.getX(), this.getY() - (C.LASER_MINE_LENGTH-this.getHeight()), this.getWidth(), C.LASER_MINE_LENGTH);
			break;
		case SIDE_LEFT:
			this.direction = MineDirection.LEFT;
			baseSensor = new NBPSensor(this.getX() - (C.LASER_MINE_LENGTH-this.getWidth()), this.getY(), C.LASER_MINE_LENGTH, this.getHeight());
			break;
		case SIDE_RIGHT:
			this.direction = MineDirection.RIGHT;
			baseSensor = new NBPSensor(this.getX(), this.getY(), C.LASER_MINE_LENGTH, this.getHeight());
			break;
		case TOP:
			this.direction = MineDirection.UP;
			baseSensor = new NBPSensor(this.getX(), this.getY(), this.getWidth(), C.LASER_MINE_LENGTH);
			break;
		case NONE:
			break;
		}
		// Add our mine laser sensor.
		baseSensor.setName("LASER_MINE_LASER_REACH");
		this.attachSensor(baseSensor);
		// Set this mine as being primed now, any player which passes into its reach will detonate it.
		this.setPrimed(true);
	}
	
	@Override
	protected void onCollisonWithKineticBox(NBPBox collidingBox, NBPIntersectionPoint kinematicBoxOriginAtCollision) {}

	@Override
	protected void onSensorEntry(NBPSensor sensor, NBPBox enteredBox) {
		// Is this our laser reach sensor?
		if(sensor.getName().equals("LASER_MINE_LASER_REACH")) {
			// We only care if the entering box is a player.
			if(enteredBox instanceof Player) {
				this.isPlayerInLaserRange = true;
			}
		}
	}

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
