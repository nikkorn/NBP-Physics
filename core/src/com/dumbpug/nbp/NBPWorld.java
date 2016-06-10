package com.dumbpug.nbp;

import java.util.ArrayList;

/**
 * Represents our physical world.
 * @author Nikolas Howard
 *
 */
public class NBPWorld {
    private float worldGravity;
    private float worldMaxBoxVelocity;
    private ArrayList<NBPBox> boxEntities;

    public NBPWorld(float gravity, float maxVelocity) {
        boxEntities = new ArrayList<NBPBox>();
        this.worldGravity = gravity;
        this.worldMaxBoxVelocity = maxVelocity;
    }

    /**
     * Update the box entities in our world.
     */
    public void update() {
        // Process box movement
        for(NBPBox box : boxEntities) {
            box.update(this);
        }
        // Do collision detection and try to handle it.
        for(NBPBox cbox : boxEntities) {
            // Process the sensors attached to the current box.
            for(NBPSensor sensor: cbox.getAttachedSensors()) {
                sensor.reviewIntersections(boxEntities);
            }
            for(NBPBox tbox : boxEntities) {
                // Are these boxes different and do they collide?
                if((cbox != tbox) && NBPMath.doBoxesCollide(cbox, tbox)) {
                    // We found a collision.
                    NBPMath.handleCollision(tbox, cbox);
                }
            }
        }
    }

    /**
     * Add a Static/Kinetic box to the world.
     * @param box
     */
    public void addBox(NBPBox box) {
        if(!boxEntities.contains(box)) {
            boxEntities.add(box);
            box.setWrappingWorld(this);
        }
    }

    /**
     * Remove a Static/Kinetic box from the world.
     * @param box
     */
    public void removeBox(NBPBox box) {
        if(boxEntities.contains(box)) {
            boxEntities.remove(box);
            box.setWrappingWorld(null);
        }
    }

    /**
     * Get all boxes in the world.
     * @return world boxes
     */
    public ArrayList<NBPBox> getWorldBoxes() {
        return boxEntities;
    }

    public float getWorldGravity() {
        return worldGravity;
    }

    public void setWorldGravity(float worldGravity) {
        this.worldGravity = worldGravity;
    }

    public float getWorldMaxBoxVelocity() {
        return worldMaxBoxVelocity;
    }

    public void setWorldMaxBoxVelocity(float worldMaxBoxVelocity) {
        this.worldMaxBoxVelocity = worldMaxBoxVelocity;
    }
}
