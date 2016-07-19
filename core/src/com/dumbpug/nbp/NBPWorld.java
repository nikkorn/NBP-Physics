package com.dumbpug.nbp;

import java.util.ArrayList;
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
			cbox.onBeforeUpdate();	
			// Update this box on the X axis.
			cbox.updateAxisX();
			if (cbox.getType() == NBPBoxType.KINETIC) {
				// Get colliding boxes
				for (NBPBox tbox : boxEntities) {
					// Are these boxes different and do they collide?
					if ((cbox != tbox) && NBPMath.doBoxesCollide(cbox, tbox)) {
						NBPMath.handleCollision(tbox, cbox, NBPCollisionAxis.X);
					}
				}
			}
			// Update this box on the Y axis.
			cbox.updateAxisY();
			if (cbox.getType() == NBPBoxType.KINETIC) {
				// Get colliding boxes
				for (NBPBox tbox : boxEntities) {
					// Are these boxes different and do they collide?
					if ((cbox != tbox) && NBPMath.doBoxesCollide(cbox, tbox)) {
						NBPMath.handleCollision(tbox, cbox, NBPCollisionAxis.Y);
					}
				}
			}
			// Process the sensors attached to the current box.
			for (NBPSensor sensor : cbox.getAttachedSensors()) {
				sensor.reviewIntersections(boxEntities);
			}
			cbox.onAfterUpdate();
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
