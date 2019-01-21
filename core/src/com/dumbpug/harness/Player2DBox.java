package com.dumbpug.harness;

import com.dumbpug.nbp.Axis;
import com.dumbpug.nbp.Bloom;
import com.dumbpug.nbp.Box;
import com.dumbpug.nbp.BoxType;
import com.dumbpug.nbp.Sensor;

/**
 * Represents a movable player.
 */
public class Player2DBox extends Box {
	private final float PLAYER_MAX_VELOCITY    = 5f;
	private final float PLAYER_FRICTION        = 0.92f;
	private final float PLAYER_RESTITUTION     = 0.5f;
	private final float PLAYER_JUMPING_IMPULSE = 3.5f;
	private final float PLAYER_WALKING_IMPULSE = 0.2f;

	/**
	 * Create a new instance of the PlayerBox class.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param playerNumber
	 */
    public Player2DBox(float x, float y, float width, float height) {
        super(x, y, width, height, BoxType.DYNAMIC);
        setFriction(PLAYER_FRICTION);
        setRestitution(PLAYER_RESTITUTION);
        setMaxVelocity(Axis.X, PLAYER_MAX_VELOCITY);
        setMaxVelocity(Axis.Y, PLAYER_MAX_VELOCITY);
        // Create a sensor and place it at the base of our player. This sensor will
        // be used to detect when we are standing on something static, thus allowing
        // the player to jump.
        float sensorHeight = 1;
        float sensorWidth  = width/2;
        float sensorPosX   = x;
        float sensorPosY   = y - sensorHeight;
        // Create the sensor.
        Sensor baseSensor = new Sensor(sensorPosX + (sensorWidth/2), sensorPosY, sensorWidth, sensorHeight);
        // Give the sensor a name, this will be checked when notified by th sensor.
        baseSensor.setName("player_base_sensor");
        // Attach the sensor to the player box.
        attachSensor(baseSensor);
    }

    /**
     * Make the player jump if he can.
     */
    public void jump() {
    	// Apply a vertical impulse.
        applyImpulse(Axis.Y, PLAYER_JUMPING_IMPULSE);
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