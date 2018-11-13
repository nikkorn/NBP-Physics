package com.dumbpug.nbp;

import java.util.ArrayList;
import com.dumbpug.nbp.zone.Zone;

/**
 * Represents a 2D or 3D physics environment.
 */
public class Environment {
    /**
     * The environment gravity.
     */
    private Gravity gravity = null;
    /**
     * The dimension of the environment that everything within it must conform to.
     */
    private Dimension dimension;
    /**
     * The box entities that are in this environment.
     */
    private Boxes boxes = new Boxes();
    /**
     * The box entities waiting to be added to this environment (added during physics update).
     */
    private ArrayList<Box> pendingBoxes = new ArrayList<Box>();
    /**
     * List holding any pending Bloom instances to be processed.
     */
    private ArrayList<Bloom> blooms = new ArrayList<Bloom>();
    /**
     * List holding any zones of force in the environment.
     */
    private ArrayList<Zone> zones = new ArrayList<Zone>();
    /**
     * Are we currently processing a physics step.
     */
    private boolean inPhysicsStep = false;

    /**
     * Create a new instance of the Environment class.
     * @param dimension The dimension of the environment.
     */
    public Environment(Dimension dimension) {
        // Set the environment dimension.
        this.dimension = dimension;
    }

    /**
     * Create a new instance of the Environment class with gravity.
     * @param dimension The dimension of the environment.
     * @param gravity The environment gravity.
     */
    public Environment(Dimension dimension, Gravity gravity) {
        this(dimension);
        // Set the environment gravity.
        this.gravity = gravity;
    }

    /**
     * Update the box entities in this environment.
     */
    public void update() {
        // Call any user-defined pre-update logic.
        this.onBeforeUpdate();
        // Mark the start of the physics step.
        inPhysicsStep = true;
        // Remove any boxes which were marked for deletion.
        boxes.removeDeletedBoxes();
        // Apply any environment blooms to the dynamic boxes.
        for (Bloom bloom : blooms) {
            for (Box box : boxes.getDynamicBoxes()) {
            	// Apply the bloom to this box
                box.applyBloom(bloom);
            }
        }
        // Remove processed environment blooms.
        blooms.clear();
        // Apply zone forces to any intersecting boxes.
        for (Zone zone : zones) {
            // Go over all boxes.
            for (Box box : boxes.getDynamicBoxes()) {
                // Make sure this box actually intersects the zone.
                if (zone.intersects(box)) {
                    // Allow the zone of force to influence the intersecting box.
                    zone.influence(box);
                }
            }
        }
        // Update all of the dynamic boxes in the world.
        for (Box currentBox : boxes.getDynamicBoxes()) {
            // Call any user-defined box pre-update logic.
            currentBox.onBeforeUpdate();
            // Update the current box.
        	updateDynamicBox(currentBox);
            // Process the sensors attached to the current box.
            for (Sensor sensor : currentBox.getAttachedSensors()) {
                // TODO sensor.reviewIntersections(boxes);
            }
            // Call any user-defined box post-update logic.
            currentBox.onAfterUpdate();
        }
        // Mark the end of the physics step.
        inPhysicsStep = false;
        // Any boxes that were added as part of this physics step should be added to our actual entity list now.
        while (!pendingBoxes.isEmpty()) {
            // Get the next pending box to be added.
        	Box pendingBox = pendingBoxes.remove(0);
        	// Add the box to our boxes collection if it does not already contain it.
            if (!boxes.contains(pendingBox)) {
                boxes.add(pendingBox);
            }
        }
        // Call any user-defined post-update logic.
        this.onAfterUpdate();
    }
    
    /**
     * Update the specified box on every axis and handle and resulting collisions.
     * @param current The dynamic box to update.
     */
    private void updateDynamicBox(Box current) {
    	// Get all static boxes that the current box could collide with.
    	// For now, that will be all static boxes within a range, but eventually it would 
    	// be better to find those intersecting the box made by updating evey axis and maxing
    	// a box between the pre and post origins.
    	// TODO Find nearby!
    	ArrayList<Box> nearbyBoxes = boxes.getStaticBoxes();
    	
    	// There is nothing more to do if there are no boxes that we could be intersecting with.
    	if (nearbyBoxes.isEmpty()) {
    		return;
    	}
    	
    	// Update the dynamic box on the X axis and resolve any collisions.
    	current.updateOnAxis(Axis.X, this.gravity);
    	for (Box nearbyBox : nearbyBoxes) {
    		if (current.intersects(nearbyBox)) {
    			Utilities.handleCollision(current, nearbyBox, Axis.X);
            }
    	}
    	
    	// Update the dynamic box on the Y axis and resolve any collisions.
    	current.updateOnAxis(Axis.Y, this.gravity);
    	for (Box nearbyBox : nearbyBoxes) {
    		if (current.intersects(nearbyBox)) {
    			Utilities.handleCollision(current, nearbyBox, Axis.Y);
            }
    	}
    	
    	// Update the dynamic box on the Z axis and resolve any collisions if we are in 3D space.
    	if (this.dimension == Dimension.THREE_DIMENSIONS) {
        	current.updateOnAxis(Axis.Z, this.gravity);
        	for (Box nearbyBox : nearbyBoxes) {
        		if (current.intersects(nearbyBox)) {
        			Utilities.handleCollision(current, nearbyBox, Axis.Z);
                }
        	}
    	}
    }
 
    /**
     * Add a Static/Dynamic box to the environment.
     * @param box The box to add.
     */
    public void addBox(Box box) {
        // If this addition is taking place during a physics update, then it should be queued for later addition.
        if (inPhysicsStep) {
            pendingBoxes.add(box);
        } else {
            if (!boxes.contains(box)) {
                boxes.add(box);
            }
        }
    }

    /**
     * Remove a Static/Dynamic box from the environment.
     * @param box The box to remove.
     */
    public void removeBox(Box box) {
        if (boxes.contains(box)) {
            boxes.remove(box);
        }
    }

    /**
     * Get all boxes in the environment.
     * @return The list of boxes in the environment.
     */
    public ArrayList<Box> getBoxes() {
        return boxes.getBoxes();
    }

    /**
     * Get the environment gravity.
     * @return gravity The environment gravity.
     */
    public Gravity getGravity() {
        return this.gravity;
    }

    /**
     * Set the environment gravity.
     * @param gravity The environment gravity.
     */
    public void setGravity(Gravity gravity) {
        this.gravity = gravity;
    }

    /**
     * Add a bloom to this environment.
     * @param bloom The bloom to add.
     */
    public void addBloom(Bloom bloom) {
        this.blooms.add(bloom);
    }

    /**
     * Add a zone of force to this environment.
     * @param zone The zone of force to add.
     */
    public void addZone(Zone zone) {
        // Don't add a zone that already exists in this environment.
        if (!this.zones.contains(zone)) {
            this.zones.add(zone);
        }
    }

    /**
     * Remove a zone of force from this environment.
     * @param zone The zone of force to remove.
     */
    public void removeZone(Zone zone) {
        // Don't try to remove a zone that doesn't already exist in this environment.
        if (this.zones.contains(zone)) {
            this.zones.remove(zone);
        }
    }

    /**
     * Called before an update.
     */
    protected void onBeforeUpdate() {}

    /**
     * Called after an update.
     */
    protected void onAfterUpdate() {}
}
