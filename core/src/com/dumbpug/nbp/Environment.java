package com.dumbpug.nbp;

import java.util.ArrayList;
import java.util.HashSet;

import com.dumbpug.nbp.projection.BoxProjection;
import com.dumbpug.nbp.projection.Projection;
import com.dumbpug.nbp.projection.ProjectionType;
import com.dumbpug.nbp.spatialgrid.SpatialGrid;
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
     * The spatial grid to use in broad phase collision detection.
     */
    private SpatialGrid<Projection> spatialGrid;
    /**
     * The box entities that are in this environment.
     */
    private Boxes boxes;
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
     * @param worldCellSize The cell size to use for the  broad phase spatial grid.
     * @param dimension The dimension of the environment.
     */
    public Environment(Dimension dimension, float worldCellSize) {
        // Set the environment dimension.
        this.dimension = dimension;
        // Set the spatial grid to use.
        this.spatialGrid = new SpatialGrid<Projection>(dimension, worldCellSize);
        // Create the boxes collection.
        this.boxes = new Boxes(this.spatialGrid);
    }

    /**
     * Create a new instance of the Environment class with gravity.
     * @param dimension The dimension of the environment.
     * @param worldCellSize The cell size to use for the  broad phase spatial grid.
     * @param gravity The environment gravity.
     */
    public Environment(Dimension dimension, float worldCellSize, Gravity gravity) {
        this(dimension, worldCellSize);
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
        
        // We need to update the position of every dynamic box projection in the spatial grid.
        for (Box dynamicBox : boxes.getDynamicBoxes()) {
            // Update the dynamic box projection.
        	this.spatialGrid.update(dynamicBox.getProjection());
        }
       
        
        // 1. Get projections of all dynamic boxes AND their sensors.
        // 2. Use Sweep And Prune to find possible intersections between static boxes and the projections of all dynamic boxes and sensors.
        // 3. Update each dynamic box (inc sensors) and report any collisions with static boxes (provided by SAP) as part of its update.
        // 4. Check each dynamic box/sensor against possible intersections and report.
        
        
        
        // Update all of the dynamic boxes in the world.
        for (Box currentBox : boxes.getDynamicBoxes()) {
            // Call any user-defined box pre-update logic.
            currentBox.onBeforeUpdate();
            // Update the current box.
        	updateDynamicBox(currentBox);
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
     * Update the specified dynamic box on every axis and handle any resulting collisions.
     * @param dynamicBox The dynamic box to update.
     */
    private void updateDynamicBox(Box dynamicBox) {
    	// Get the set of all collision candidates for the dynamic box. 
    	HashSet<Projection> candidates = this.spatialGrid.getCollisionCandidates(dynamicBox.getProjection());
    	
    	// Create a list to store all static boxes that the dynamic box could collide with.
    	ArrayList<Box> staticBoxesOverlappingProjection = new ArrayList<Box>();
    	
    	for (Projection projection : candidates) {
    		// We only care about projections that are for a box, not sensors.
    		if (projection.getProjectionType() == ProjectionType.BOX) {
    			// Get the box that the box projection represents.
    			Box projectedBox = ((BoxProjection)projection).getProjectedBox();
    			// At this point of the update we only care about static boxes.
    			if (projectedBox.getType() == BoxType.STATIC) {
    				staticBoxesOverlappingProjection.add(projectedBox);
    			}
    		}
    	}
    	
    	// Update the dynamic box on the X axis and resolve any collisions.
    	updateDynamicBoxOnAxis(dynamicBox, staticBoxesOverlappingProjection, Axis.X);
    	
    	// Update the dynamic box on the Y axis and resolve any collisions.
    	updateDynamicBoxOnAxis(dynamicBox, staticBoxesOverlappingProjection, Axis.Y);
    	
    	// Update the dynamic box on the Z axis and resolve any collisions if we are in 3D space.
    	if (this.dimension == Dimension.THREE_DIMENSIONS) {
    		updateDynamicBoxOnAxis(dynamicBox, staticBoxesOverlappingProjection, Axis.Z);
    	}
    }
    
    /**
     * Update a dynamic box and resolve any collisons between it and any static boxes in
     * reponse to updating the position of the dynamic box on the specified axis.
     * @param dynamicBox The dynamic box to update and resolve any collisions for.
     * @param staticBoxes The static boxes that the dynamic box may collide with.
     * @param axis The axis on which to update the dynamic box and resolve collisions
     */
    private void updateDynamicBoxOnAxis(Box dynamicBox, ArrayList<Box> staticBoxes, Axis axis) {
    	// Update the dynamic box on the axis.
    	dynamicBox.updateOnAxis(axis, this.gravity);
    	
    	// Keep track of which colliding static box is closest.
    	Box closestCollidingBox = null;
    	
    	// Find the earliest collision (if any) that happened with any static boxes on the axis.
    	// If any happened at the same time then either just pick one or find the greatest are of overlap (using other axis).
    	for (Box staticBox : staticBoxes) {
    		if (closestCollidingBox == null && dynamicBox.intersects(staticBox)) {
    			closestCollidingBox = staticBox;
    		} else if (dynamicBox.intersects(staticBox)) {
    			
    			// Based on the direction that the dynamic box is moving on the axis we will find the closest intersection.
    			if (dynamicBox.getVelocity(axis) > 0) {
    				
    				// Moving in a positive direction means that we would intersect static boxes at the edge defined
    				// by just their position, not their position plus their length on the current axis.
    				float closestEdgeAxisPosition = closestCollidingBox.getPosition(axis);
    				float currentEdgeAxisPosition = staticBox.getPosition(axis);
    				
    				// Is the current static boxes edge closer to the dynamic box that the current one?
    				if (currentEdgeAxisPosition < closestEdgeAxisPosition) {
    					// The current static box would have collided with the dynamic box earliest.
    					closestCollidingBox = staticBox;
    				} else if (currentEdgeAxisPosition == closestEdgeAxisPosition) {
    					// TODO The dynamic box would have collided with the current and closest boxes at the same time.
    				}
    			} else if (dynamicBox.getVelocity(axis) < 0) {
    				
    				// Moving in a negative direction means that we would intersect static boxes at the edge defined
    				// by their position plus their length on the current axis.
    				float closestEdgeAxisPosition = closestCollidingBox.getPosition(axis) + closestCollidingBox.getLength(axis);
    				float currentEdgeAxisPosition = staticBox.getPosition(axis) + staticBox.getLength(axis);
    				
    				// Is the current static boxes edge closer to the dynamic box that the current one?
    				if (currentEdgeAxisPosition > closestEdgeAxisPosition) {
    					// The current static box would have collided with the dynamic box earliest.
    					closestCollidingBox = staticBox;
    				} else if (currentEdgeAxisPosition == closestEdgeAxisPosition) {
    					// TODO The dynamic box would have collided with the current and closest boxes at the same time.
    				}
    			} else {
    				// The dynamic box is not moving on the current axis.
    			}
    		}
    	}
    	
    	// Call Utilities.handleCollision() passing the dynamic box and the earliest static collision.
    	if (closestCollidingBox != null) {
    		Utilities.handleCollision(dynamicBox, closestCollidingBox, axis);
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
