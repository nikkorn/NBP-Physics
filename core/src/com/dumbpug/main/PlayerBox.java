package com.dumbpug.main;

import com.dumbpug.nbp.NBPBox;
import com.dumbpug.nbp.NBPBoxType;
import com.dumbpug.nbp.NBPSensor;

/**
 * Represents a movable player.
 * Created by nik on 28/02/16.
 */
public class PlayerBox extends NBPBox {
    // Can the player jump?
    private boolean canJump = false;

    public PlayerBox(float x, float y, float width, float height) {
        super(x, y, width, height, NBPBoxType.KINETIC);
        // Set various properties for the player.
        setName("player");
        setFriction(0.92f);
        setRestitution(0f);
        // Create a sensor and place it at the base of our player. This sensor will
        // be used to detect when we are standing on something static, thus allowing
        // the player to jump.
        float sensorHeight = 2;
        float sensorWidth  = width/2;
        float sensorPosX   = x;
        float sensorPosY   = y - sensorHeight;
        // Create the sensor.
        NBPSensor baseSensor = new NBPSensor(sensorPosX + (sensorWidth/2), sensorPosY, sensorWidth, sensorHeight);
        // Give the sensor a name, this will be checked when notified by th sensor.
        baseSensor.setName("player_base_sensor");
        // Attach the sensor to the player box.
        attachSensor(baseSensor);
    }

    /**
     * Move the player to the left.
     */
    public void moveLeft() {
        applyImpulse(-0.2f,0f);
    }

    /**
     * Move the player to the right.
     */
    public void moveRight() {
        applyImpulse(0.2f,0f);
    }

    /**
     * Make the player jump if he can.
     */
    public void jump() {
        // Can we jump? (Are we on a static block?)
        if(canJump) {
            // Apply a vertical impulse.
            applyImpulse(0f,3f);
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
}
