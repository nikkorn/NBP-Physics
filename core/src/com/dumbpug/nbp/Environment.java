package com.dumbpug.nbp;

import java.util.ArrayList;
import java.util.Iterator;
import com.dumbpug.nbp.point.IntersectionPoint;
import com.dumbpug.nbp.point.Point;
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
    private ArrayList<Box> boxes = new ArrayList<Box>();
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
        Iterator<Box> boxIterator = boxes.iterator();
        while (boxIterator.hasNext()) {
            Box box = boxIterator.next();
            if (box.isMarkedForDeletion()) {
                box.setDeleted();
                boxIterator.remove();
                // Call user specified behaviour on deletion.
                box.onDeletion();
            }
        }
        // Apply any environment blooms.
        for (Bloom bloom : blooms) {
            // Go over all boxes.
            for (Box box : boxes) {
                // Make sure this is a dynamic box.
                if (box.getType() == BoxType.DYNAMIC) {
                    // Apply the bloom to this box
                    box.applyBloom(bloom);
                }
            }
        }
        // Remove processed environment blooms.
        blooms.clear();
        // Apply zone forces to any intersecting boxes.
        for (Zone zone : zones) {
            // Go over all boxes.
            for (Box box : boxes) {
                // Make sure this is a dynamic box and that it actually intersects the zone.
                if ((box.getType() == BoxType.DYNAMIC) && zone.intersects(box)) {
                    // Allow the zone of force to influence the intersecting box.
                    zone.influence(box);
                }
            }
        }
        // Do collision detection and try to handle it.
        for (Box currentBox : boxes) {
            // Call any user-defined box pre-update logic.
            currentBox.onBeforeUpdate();
            // Only attempt to update positions and resolve collisions for dynamic boxes.
            if (currentBox.getType() == BoxType.DYNAMIC) {
                // Update the current box.
            	updateDynamicBox(currentBox);
            }
            // Process the sensors attached to the current box.
            for (Sensor sensor : currentBox.getAttachedSensors()) {
                sensor.reviewIntersections(boxes);
            }
            // Call any user-defined box post-update logic.
            currentBox.onAfterUpdate();
        }
        // Mark the end of the physics step.
        inPhysicsStep = false;
        // Any boxes that were added as part of this physics step should be added to our actual entity list now.
        if (pendingBoxes.size() > 0) {
            for (Box pendingBox : pendingBoxes) {
                this.addBox(pendingBox);
            }
            // Clear the pending list.
            pendingBoxes.clear();
        }
        // Call any user-defined post-update logic.
        this.onAfterUpdate();
    }
    
    /**
     * Update the specified box on every axis and handle and resulting collisions.
     * @param current The dynamic box to update.
     */
    private void updateDynamicBox(Box current) {
    	// Get the current x/y/z positions of the origin of the box.
    	Point preUpdateOrigin = current.getOrigin().clone();
    	
    	// Update the current dynamic box.
    	current.update(this.gravity);
    	
    	// Create a list to store all static boxes that intersect the current box.
    	ArrayList<Box> collidingBoxes = new ArrayList<Box>();
    	
    	// Get all static blocks that are colliding with the box.
    	for (Box box : this.boxes) {
    		// If this is another dynamic box then do nothing.
    		if (box.getType() == BoxType.DYNAMIC) {
    			continue;
    		}
    		// This is a static box, is it intersecting the box we are updating?
    		if (NBPMath.doBoxesCollide(current, box)) {
    			collidingBoxes.add(box);
    		}
    	}
    	
    	// There is nothing more to do if we are not colliding with any static boxes.
    	if (collidingBoxes.isEmpty()) {
    		return;
    	}
    	
    	// TODO Sort the list by which one happened first.
    	
    	// TODO Do sweep AABB test for first, and for all others IF they are still colliding.
    	// Check out http://noonat.github.io/intersect/
    	
    	// TODO We can easily be intersecting multiple static boxes!!!!!!
    	Box colliding = collidingBoxes.get(0);
    	
    	// TODO We would have already gotten the closest collision point for this box in choosing which collision to resolve first.
    	IntersectionPoint closestIntersection = NBPMath.getClosestIntersectionPoint(preUpdateOrigin, current, colliding);
    	
    	// TODO Do this properly!
    	NBPMath.resolveDynamicAndStaticBoxCollision(closestIntersection, current, colliding);
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
        return boxes;
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