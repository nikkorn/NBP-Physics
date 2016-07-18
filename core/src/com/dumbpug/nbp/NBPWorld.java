package com.dumbpug.nbp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Represents our physical world.
 * @author Nikolas Howard
 *
 */
public class NBPWorld {
	// The world gravity.
	private float worldGravity;
	// The box entities that are in our physics world.
	private ArrayList<NBPBox> boxEntities;
	// The box entities waiting to be added to our world (added during physics update)
	private ArrayList<NBPBox> pendingBoxEntities;
	// List holding any pending NBPBloom instances to be processed.
	private ArrayList<NBPBloom> bloomList;
	// Are we currently processing a physics step.
	private boolean inPhysicsStep = false;

	/**
	 * Create a new instance of the NBPWorld class.
	 * @param gravity
	 */
	public NBPWorld(float gravity) {
		boxEntities        = new ArrayList<NBPBox>();
		pendingBoxEntities = new ArrayList<NBPBox>();
		bloomList          = new ArrayList<NBPBloom>();
		this.worldGravity  = gravity;
	}

	/**
	 * Update the box entities in our world.
	 */
	public void update() {
		// Mark the start of the physics step.
		inPhysicsStep = true;
		// Remove any boxes which were marked for deletion.
		Iterator<NBPBox> boxIterator = boxEntities.iterator();
		while (boxIterator.hasNext()) {
			NBPBox cbox = boxIterator.next();
			if (cbox.isMarkedForDeletion()) {
				cbox.setDeleted();
				boxIterator.remove();
				// Call user specified behaviour on deletion.
				cbox.onDeletion();
			}
		}
		// Process box movement.
		for (NBPBox box : boxEntities) {
			box.update();
		}
		// Apply any world blooms.
		for (NBPBloom bloom : bloomList) {
			// Go over all boxes.
			for (NBPBox box : boxEntities) {
				// Make sure this is a kinematic box.
				if(box.getType() == NBPBoxType.KINETIC) { 
					// Apply the bloom to this box
					box.applyBloom(bloom);
				}
			}
		}
		// Remove processed world blooms.
		bloomList.clear();
		// Do collision detection and try to handle it.
		for (NBPBox cbox : boxEntities) {
			// Process the sensors attached to the current box.
			for (NBPSensor sensor : cbox.getAttachedSensors()) {
				sensor.reviewIntersections(boxEntities);
			}
			// Only do collision resolution for kinematic bodies.
			if (cbox.getType() == NBPBoxType.KINETIC) {
				ArrayList<NBPBox> collidingB = new ArrayList<NBPBox>();
				// Get colliding boxes
				for (NBPBox tbox : boxEntities) {
					// Are these boxes different and do they collide?
					if ((cbox != tbox) && NBPMath.doBoxesCollide(cbox, tbox)) {
						// We found a collision.
						collidingB.add(tbox);
					}
				}
				// Sort colliding boxes by the size of their intersection with
				// our moving box.
				Collections.sort(collidingB, new Comparator<NBPBox>() {
					@Override
					public int compare(NBPBox b1, NBPBox b2) {
						float intersectionAreaB1 = NBPMath.getIntersectionArea(cbox, b1);
						float intersectionAreaB2 = NBPMath.getIntersectionArea(cbox, b2);
						if(intersectionAreaB1 > intersectionAreaB2) {
							return -1;
						} else if(intersectionAreaB1 < intersectionAreaB2) {
							return 1;
						} else {
							return 0;
						}
					}
				});
				// Do the actual collision handling.
				for (NBPBox tbox : collidingB) {
					if (NBPMath.doBoxesCollide(cbox, tbox)) {
						NBPMath.handleCollision(tbox, cbox);
					}
				}
			}
		}
		// Mark the end of the physics step.
		inPhysicsStep = false;
		// Any boxes that were added as part of this physics step should be added to our actual entity list now.
		if(pendingBoxEntities.size() > 0) {
			for(NBPBox pendingBox : pendingBoxEntities) {
				this.addBox(pendingBox);
			}
			// Clear the pending list.
			pendingBoxEntities.clear();
		}
	}

	/**
	 * Add a Static/Kinematic box to the world.
	 * @param box
	 */
	public void addBox(NBPBox box) {
		// If this addition is taking place during a physics update, then it should be queued for later addition.
		if(inPhysicsStep) {
			pendingBoxEntities.add(box);
		} else {
			if (!boxEntities.contains(box)) {
				boxEntities.add(box);
				box.setWrappingWorld(this);
			}
		}
	}

	/**
	 * Remove a Static/Kinematic box from the world.
	 * @param box
	 */
	public void removeBox(NBPBox box) {
		if (boxEntities.contains(box)) {
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

	/**
	 * Get the world gravity.
	 * @return gravity
	 */
	public float getWorldGravity() {
		return worldGravity;
	}

	/**
	 * Set the world gravity.
	 * @param worldGravity
	 */
	public void setWorldGravity(float worldGravity) {
		this.worldGravity = worldGravity;
	}
	
	/**
	 * Add a bloom to this world.
	 * @param bloom
	 */
	public void addBloom(NBPBloom bloom) {
		this.bloomList.add(bloom);
	}
}
