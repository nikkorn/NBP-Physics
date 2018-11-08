package com.dumbpug.harness;

import com.dumbpug.nbp.Bloom;
import com.dumbpug.nbp.Box;
import com.dumbpug.nbp.BoxType;
import com.dumbpug.nbp.Sensor;
import com.dumbpug.nbp.point.IntersectionPoint;

/**
 * Represents a movable player.
 */
public class PlayerBox extends Box {
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
    public PlayerBox(float x, float y, float width, float height) {
        super(x, y, width, height, BoxType.DYNAMIC);
        setFriction(PLAYER_FRICTION);
        setRestitution(PLAYER_RESTITUTION);
        setMaxVelocityX(PLAYER_MAX_VELOCITY);
        setMaxVelocityY(PLAYER_MAX_VELOCITY);
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
        applyImpulse(0f, PLAYER_JUMPING_IMPULSE);
    }
    
    /**
     * Move the player to the left.
     */
    public void moveLeft() {
    	applyImpulse(-PLAYER_WALKING_IMPULSE, 0f);
    }

    /**
     * Move the player to the right.
     */
    public void moveRight() {
    	applyImpulse(PLAYER_WALKING_IMPULSE, 0f);
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
	protected void onCollisionWithDynamicBox(Box collidingBox, IntersectionPoint dynamicBoxOriginAtCollision) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onCollisionWithStaticBox(Box collidingBox, IntersectionPoint originAtCollision) {
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