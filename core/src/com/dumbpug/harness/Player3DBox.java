package com.dumbpug.harness;

import com.dumbpug.nbp.Axis;
import com.dumbpug.nbp.Bloom;
import com.dumbpug.nbp.Box;
import com.dumbpug.nbp.BoxType;
import com.dumbpug.nbp.Sensor;

/**
 * Represents a 3D movable player.
 */
public class Player3DBox extends Box {
	private final float PLAYER_MAX_VELOCITY    = 0.5f;
	private final float PLAYER_FRICTION        = 0.5f;
	private final float PLAYER_RESTITUTION     = 0.5f;
	private final float PLAYER_JUMPING_IMPULSE = 0.5f;
	private final float PLAYER_WALKING_IMPULSE = 0.2f;

	/**
	 * Create a new instance of the Player3DBox class.
	 * @param x
	 * @param y
	 * @param z
	 * @param width
	 * @param height
	 * @param depth
	 * @param playerNumber
	 */
    public Player3DBox(float x, float y, float z, float width, float height, float depth) {
        super(x, y, z, width, height, depth, BoxType.DYNAMIC);
        setFriction(PLAYER_FRICTION);
        setRestitution(PLAYER_RESTITUTION);
        setMaxVelocity(Axis.X, PLAYER_MAX_VELOCITY);
        setMaxVelocity(Axis.Z, PLAYER_MAX_VELOCITY);
        setMaxVelocity(Axis.Y, PLAYER_MAX_VELOCITY);
    }

    /**
     * Make the player jump if he can.
     */
    public void jump() {
    	// Apply a vertical impulse.
        applyImpulse(Axis.Y, PLAYER_JUMPING_IMPULSE);
    }
    
    /**
     * Move the player up.
     */
    public void moveUp() {
    	applyImpulse(Axis.Z, -PLAYER_WALKING_IMPULSE);
    }

    /**
     * Move the player down.
     */
    public void moveDown() {
    	applyImpulse(Axis.Z, PLAYER_WALKING_IMPULSE);
    }
    
    /**
     * Move the player to the left.
     */
    public void moveLeft() {
    	applyImpulse(Axis.X, -PLAYER_WALKING_IMPULSE);
    }

    /**
     * Move the player to the right.
     */
    public void moveRight() {
    	applyImpulse(Axis.X, PLAYER_WALKING_IMPULSE);
    }

	@Override
	protected void onBeforeUpdate() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onAfterUpdate() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onDeletion() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onCollisionWithDynamicBox(Box collidingBox) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onCollisionWithStaticBox(Box collidingBox) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onSensorEntry(Sensor sensor, Box enteredBox) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onSensorExit(Sensor sensor, Box exitedBox) {
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean onBloomPush(Bloom bloom, float angleOfForce, float force, float distance) {
		// TODO Auto-generated method stub
		return true;
	}
}