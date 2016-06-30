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
	// List holding any pending NBPBloom instances to be processed.
	private ArrayList<NBPBloom> bloomList;

	/**
	 * Create a new instance of the NBPWorld class.
	 * @param gravity
	 */
	public NBPWorld(float gravity) {
		boxEntities = new ArrayList<NBPBox>();
		bloomList   = new ArrayList<NBPBloom>();
		this.worldGravity = gravity;
	}

	/**
	 * Update the box entities in our world.
	 */
	public void update() {
		// Process box movement.
		for (NBPBox box : boxEntities) {
			box.update();
		}
		// Apply any world blooms.
		for (NBPBloom bloom : bloomList) {
			// Go over all boxes.
			for (NBPBox box : boxEntities) {
				// Get the point of the bloom as a NBPPoint object.
				NBPPoint bloomPoint = new NBPPoint(bloom.getX(), bloom.getY());
				// Make sure this is a kinematic box.
				if(box.getType() == NBPBoxType.KINETIC) { 
					// && NBPMath.isPointInCircle(box.getCurrentOriginPoint(), bloomPoint, bloom.getRadius())) {
					// Get the distance between the bloom center and the center of our kinematic box.
					float distance = NBPMath.getDistanceBetweenPoints(bloomPoint, box.getCurrentOriginPoint());
					// Check to see if the box is even in the range of the bloom.
					if(distance <= bloom.getRadius()) {
						// TODO Although it is unlikely, distance could be 0.!!!!!!!!!!!!!!!
						// Our box was in the bloom, get angle difference between our bloom and the current box.
						float angleBetweenBloomAndBox = NBPMath.getAngleBetweenPoints(box.getCurrentOriginPoint(), bloomPoint);
						// TODO Recalculate force based on the distance between our box and bloom.
						float force = bloom.getForce() * (bloom.getRadius()/distance);
						box.applyVelocityInDirection(angleBetweenBloomAndBox, force);
					}
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
		// Remove any boxes which were marked for deletion during the collision
		// detection/resolution stage.
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
	}

	/**
	 * Add a Static/Kinematic box to the world.
	 * @param box
	 */
	public void addBox(NBPBox box) {
		if (!boxEntities.contains(box)) {
			boxEntities.add(box);
			box.setWrappingWorld(this);
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
