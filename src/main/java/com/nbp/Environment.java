package com.nbp;

import com.nbp.zone.Zone;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a physics environment.
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
            currentBox.onBeforeUpdate();
            // Update this box on the X axis.
            currentBox.updateAxisX(this.gravity);
            // Resolve collisions on the X axis.
            if (currentBox.getType() == BoxType.DYNAMIC) {
                // Get colliding boxes
                for (Box targetBox : boxes) {
                    // Are these boxes different and do they collide?
                    if ((currentBox != targetBox) && NBPMath.doBoxesCollide(currentBox, targetBox)) {
                        NBPMath.handleCollision(targetBox, currentBox, Axis.X);
                    }
                }
            }
            // Update this box on the Y axis.
            currentBox.updateAxisY(this.gravity);
            // Resolve collisions on the Y axis.
            if (currentBox.getType() == BoxType.DYNAMIC) {
                // Get colliding boxes
                for (Box targetBox : boxes) {
                    // Are these boxes different and do they collide?
                    if ((currentBox != targetBox) && NBPMath.doBoxesCollide(currentBox, targetBox)) {
                        NBPMath.handleCollision(targetBox, currentBox, Axis.Y);
                    }
                }
            }
            // Process the sensors attached to the current box.
            for (Sensor sensor : currentBox.getAttachedSensors()) {
                sensor.reviewIntersections(boxes);
            }
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
        this.onAfterUpdate();
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
