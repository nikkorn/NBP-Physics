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
        boolean collisionsResolved = false;
        while(!collisionsResolved) {
            collisionsResolved = true;
            for(NBPBox cbox : boxEntities) {
                for(NBPBox tbox : boxEntities) {
                    // Are these boxes different and do they collide?
                    if((cbox != tbox) && NBPMath.doBoxesCollide(cbox, tbox)) {
                        // We found a collision, we will need to re-go over after.
						// If this a Kinetic/Static collision then we need to re-asses collisions.
                        if((cbox.getType() == NBPBoxType.KINETIC && tbox.getType() == NBPBoxType.STATIC) ||
                                (tbox.getType() == NBPBoxType.KINETIC && cbox.getType() == NBPBoxType.STATIC)) {
                            collisionsResolved = false;
                        }
                        NBPMath.handleCollision(tbox, cbox);
                    }
                }
            }
        }
	}
	
	public void addBox(NBPBox box) {
		if(!boxEntities.contains(box)) {
			boxEntities.add(box);
			box.setWrappingWorld(this);
		}
	}
	
	public void removeBox(NBPBox box) {
		if(boxEntities.contains(box)) {
			boxEntities.remove(box);
			box.setWrappingWorld(null);
		}
	}
	
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
