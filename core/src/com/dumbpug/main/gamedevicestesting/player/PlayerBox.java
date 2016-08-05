package com.dumbpug.main.gamedevicestesting.player;

import com.dumbpug.main.gamedevicestesting.C;
import com.dumbpug.nbp.NBPBloom;
import com.dumbpug.nbp.NBPBox;
import com.dumbpug.nbp.NBPBoxType;
import com.dumbpug.nbp.NBPIntersectionPoint;
import com.dumbpug.nbp.NBPSensor;

/**
 * Physics box for a player.
 * @author nikolas.howard
 *
 */
public class PlayerBox extends NBPBox {
	// Reference to our player object.
	private Player player;
	// Can the player jump?
    private boolean canJump = false;

	public PlayerBox(Player player, float x, float y, float width, float height, NBPBoxType type) {
		super(x, y, width, height, type);
		setFriction(C.PLAYER_FRICTION);
        setRestitution(C.PLAYER_RESTITUTION);
        // Set max velocity for this player.
        setMaxVelocityX(C.PLAYER_MAX_VELOCITY);
        setMaxVelocityY(C.PLAYER_MAX_VELOCITY);
        // Create a sensor and place it at the base of our player. This sensor will
        // be used to detect when we are standing on something static, thus allowing
        // the player to jump.
        float sensorHeight = 1;
        float sensorWidth  = width/2;
        float sensorPosX   = x;
        float sensorPosY   = y - sensorHeight;
        // Create the sensor.
        NBPSensor baseSensor = new NBPSensor(sensorPosX + (sensorWidth/2), sensorPosY, sensorWidth, sensorHeight);
        // Give the sensor a name, this will be checked when notified by the sensor.
        baseSensor.setName("player_base_sensor");
        // Attach the sensor to the player box.
        attachSensor(baseSensor);
        // Grab reference to our player.
        this.player = player;
	}
	
	/**
     * Move the player to the left.
     */
    public void moveLeft() {
    	// Calculate how to apply an impulse to this player so that its moving speed is defined 
    	// by a value lower that its max velocity. In this case, a walking speed.
    	if(this.getVelx() > -C.PLAYER_MAX_WALKING_VELOCITY) {
    		if((-C.PLAYER_MAX_WALKING_VELOCITY - this.getVelx()) > C.PLAYER_WALKING_IMPULSE_VALUE) {
    			applyImpulse(-C.PLAYER_MAX_WALKING_VELOCITY - this.getVelx(), 0f);
    		} else {
    			applyImpulse(-C.PLAYER_WALKING_IMPULSE_VALUE, 0f);
    		}
    	} 
    }

    /**
     * Move the player to the right.
     */
    public void moveRight() {
    	// Calculate how to apply an impulse to this player so that its moving speed is defined 
    	// by a value lower that its max velocity. In this case, a walking speed.
    	if(this.getVelx() < C.PLAYER_MAX_WALKING_VELOCITY) {
    		if((C.PLAYER_MAX_WALKING_VELOCITY - this.getVelx()) < C.PLAYER_WALKING_IMPULSE_VALUE) {
    			applyImpulse(C.PLAYER_MAX_WALKING_VELOCITY - this.getVelx(), 0f);
    		} else {
    			applyImpulse(C.PLAYER_WALKING_IMPULSE_VALUE, 0f);
    		}
    	} 
    }

    /**
     * Make the player jump if he can.
     */
    public void jump() {
        // Can we jump? (Are we on a static block?)
        if(canJump) {
            // Apply a vertical impulse.
            applyImpulse(0f, C.PLAYER_JUMPING_IMPULSE);
        }
    }
    
    @Override
    public void onSensorEntry(NBPSensor sensor, NBPBox enteredBox) {
        // Check that this sensor is the one we have placed at the bottom of the box.
        if(sensor.getName().equals("player_base_sensor")) {
            // If we are on any static block then we can jump off of it.
            if(enteredBox.getType() == NBPBoxType.STATIC) {
                // Set a flag to show that the player can now jump.
                this.canJump = true;
            }
        }
    }

    @Override
    public void onSensorExit(NBPSensor sensor, NBPBox exitedBox) {
        // We have lifted off of a box, if this is a static box then check whether the sensor is
        // now not intersecting with any static boxes. if not then the player can no longer jump.
        // Firstly, make sure that this is the sensor that we placed at the base of the player.
        if(sensor.getName().equals("player_base_sensor")) {
            // Check that the sensor left a static box.
            if(exitedBox.getType() == NBPBoxType.STATIC) {
                // Get all other intersecting boxes for this sensor, if none are static
                // then we can no longer jump as we are not resting on anything.
                boolean isRestingOnStaticBox = false;
                for(NBPBox box : sensor.getIntersectingBoxes()) {
                    // Is this intersecting box static?
                    if(box.getType() == NBPBoxType.STATIC) {
                        // We are still standing on a static box so we can still jump.
                        isRestingOnStaticBox = true;
                        break;
                    }
                }
                // Set the flag to show whether we can still jump.
                this.canJump = isRestingOnStaticBox;
            }
        }
    }

	@Override
	protected boolean onBloomPush(NBPBloom bloom, float angleOfForce, float force, float distance) {
		// While NBPBloom does not necessarily mean an explosion, it does in the context of this game.
		// Reduce our players health, using the force.
		player.dealDamage((int) force);
		// Return true as we want the force to affect this player box.
		return true;
	}

	@Override
	protected void onCollisonWithKineticBox(NBPBox collidingBox, NBPIntersectionPoint kinematicBoxOriginAtCollision) {}

	@Override
	protected void onCollisonWithStaticBox(NBPBox collidingBox, NBPIntersectionPoint originAtCollision) {}
	
	@Override
	protected void onBeforeUpdate() {}

	@Override
	protected void onAfterUpdate() {}

	@Override
	protected void onDeletion() {}
}
