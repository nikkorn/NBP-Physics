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
     * The box entities that are in this environment.
     */
    private ArrayList<Box> boxEntities;
    /**
     * The box entities waiting to be added to this environment (added during physics update).
     */
    private ArrayList<Box> pendingBoxEntities;
    /**
     * List holding any pending Bloom instances to be processed.
     */
    private ArrayList<Bloom> bloomList;
    /**
     * List holding any zones of force in the environment.
     */
    private ArrayList<Zone> zoneList;
    /**
     * Are we currently processing a physics step.
     */
    private boolean inPhysicsStep = false;

    /**
     * Create a new instance of the Environment class with gravity.
     * @param gravity The environment gravity.
     */
    public Environment(Gravity gravity) {
        boxEntities        = new ArrayList<Box>();
        pendingBoxEntities = new ArrayList<Box>();
        bloomList          = new ArrayList<Bloom>();
        zoneList           = new ArrayList<Zone>();
        this.gravity       = gravity;
    }

    /**
     * Create a new instance of the Environment class.
     */
    public Environment() {}

    /**
     * Update the box entities in this environment.
     */
    public void update() {
        this.onBeforeUpdate();
        // Mark the start of the physics step.
        inPhysicsStep = true;
        // Remove any boxes which were marked for deletion.
        Iterator<Box> boxIterator = boxEntities.iterator();
        while (boxIterator.hasNext()) {
            Box cbox = boxIterator.next();
            if (cbox.isMarkedForDeletion()) {
                cbox.setDeleted();
                boxIterator.remove();
                // Call user specified behaviour on deletion.
                cbox.onDeletion();
            }
        }
        // Apply any environment blooms.
        for (Bloom bloom : bloomList) {
            // Go over all boxes.
            for (Box box : boxEntities) {
                // Make sure this is a dynamic box.
                if (box.getType() == BoxType.DYNAMIC) {
                    // Apply the bloom to this box
                    box.applyBloom(bloom);
                }
            }
        }
        // Remove processed environment blooms.
        bloomList.clear();
        // Apply zone forces to any intersecting boxes.
        for (Zone zone : zoneList) {
            // Go over all boxes.
            for (Box box : boxEntities) {
                // Make sure this is a dynamic box and that it actually intersects the zone.
                if ((box.getType() == BoxType.DYNAMIC) && zone.intersects(box)) {
                    // Allow the zone of force to influence the intersecting box.
                    zone.influence(box);
                }
            }
        }
        // Do collision detection and try to handle it.
        for (Box currentBox : boxEntities) {
            currentBox.onBeforeUpdate();
            // Update this box on the X axis.
            currentBox.updateAxisX(this.gravity);
            // Resolve collisions on the X axis.
            if (currentBox.getType() == BoxType.DYNAMIC) {
                // Get colliding boxes
                for (Box targetBox : boxEntities) {
                    // Are these boxes different and do they collide?
                    if ((currentBox != targetBox) && NBPMath.doBoxesCollide(currentBox, targetBox)) {
                        NBPMath.handleCollision(targetBox, currentBox, CollisionAxis.X);
                    }
                }
            }
            // Update this box on the Y axis.
            currentBox.updateAxisY(this.gravity);
            // Resolve collisions on the Y axis.
            if (currentBox.getType() == BoxType.DYNAMIC) {
                // Get colliding boxes
                for (Box targetBox : boxEntities) {
                    // Are these boxes different and do they collide?
                    if ((currentBox != targetBox) && NBPMath.doBoxesCollide(currentBox, targetBox)) {
                        NBPMath.handleCollision(targetBox, currentBox, CollisionAxis.Y);
                    }
                }
            }
            // Process the sensors attached to the current box.
            for (Sensor sensor : currentBox.getAttachedSensors()) {
                sensor.reviewIntersections(boxEntities);
            }
            currentBox.onAfterUpdate();
        }
        // Mark the end of the physics step.
        inPhysicsStep = false;
        // Any boxes that were added as part of this physics step should be added to our actual entity list now.
        if (pendingBoxEntities.size() > 0) {
            for (Box pendingBox : pendingBoxEntities) {
                this.addBox(pendingBox);
            }
            // Clear the pending list.
            pendingBoxEntities.clear();
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
            pendingBoxEntities.add(box);
        } else {
            if (!boxEntities.contains(box)) {
                boxEntities.add(box);
            }
        }
    }

    /**
     * Remove a Static/Dynamic box from the environment.
     * @param box The box to remove.
     */
    public void removeBox(Box box) {
        if (boxEntities.contains(box)) {
            boxEntities.remove(box);
        }
    }

    /**
     * Get all boxes in the environment.
     * @return The list of boxes in the environment.
     */
    public ArrayList<Box> getBoxes() {
        return boxEntities;
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
        this.bloomList.add(bloom);
    }

    /**
     * Add a zone of force to this environment.
     * @param zone The zone of force to add.
     */
    public void addZone(Zone zone) {
        // Don't add a zone that already exists in this environment.
        if (!this.zoneList.contains(zone)) {
            this.zoneList.add(zone);
        }
    }

    /**
     * Remove a zone of force from this environment.
     * @param zone The zone of force to remove.
     */
    public void removeZone(Zone zone) {
        // Don't try to remove a zone that doesn't already exist in this environment.
        if (this.zoneList.contains(zone)) {
            this.zoneList.remove(zone);
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
