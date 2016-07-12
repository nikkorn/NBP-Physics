package com.dumbpug.main.gamedevicestesting;

import com.dumbpug.nbp.NBPBloom;
import com.dumbpug.nbp.NBPBox;
import com.dumbpug.nbp.NBPBoxType;
import com.dumbpug.nbp.NBPIntersectionPoint;
import com.dumbpug.nbp.NBPSensor;

/**
 * Represents a grenade.
 * @author nikolas.howard
 *
 */
public class Rocket extends NBPBox {
	// Owner of the rocket.   
	private PlayerBox owner;
	
	/**
	 * Create a new grenade.
	 * @param owner
	 */
    public Rocket(PlayerBox owner) {
        super(owner.getCurrentOriginPoint().getX(), owner.getCurrentOriginPoint().getY(), 10, 10, NBPBoxType.KINETIC);
        // Set the owner.
        this.owner = owner;
        // Set various properties for the grenade.
        setName("rocket");
        // Set max velocity. 
        setMaxVelocityX(8);
        setMaxVelocityY(8);
        // Apply initial impulse in the players facing direction
        this.applyImpulse(owner.facingRight ? 8 : -8, 1f);
    }
    
    @Override
    protected void onCollisonWithKineticBox(NBPBox collidingBox, NBPIntersectionPoint kinematicBoxOriginAtCollision) {
    	// Make sure that we are not just intersecting with the owner.
    	if(!collidingBox.getName().equals(owner.getName())) {
    		// We have hit something! Explode! Add a world bloom.
    		NBPBloom rocketExplosionBloom = new NBPBloom(this.getCurrentOriginPoint().getX(), 
    				this.getCurrentOriginPoint().getY(), 60f, 6f);
    		this.getWrappingWorld().addBloom(rocketExplosionBloom);
    		// Remove this rocket box on next update.
    		this.markForDeletion();
    	}
    }

    @Override
	protected void onCollisonWithStaticBox(NBPBox collidingBox, NBPIntersectionPoint originAtCollision) {
    	// We have hit something! Explode! Add a world bloom.
		NBPBloom rocketExplosionBloom = new NBPBloom(originAtCollision.getX(), 
				originAtCollision.getY(), 60f, 6f);
		this.getWrappingWorld().addBloom(rocketExplosionBloom);
		// Remove this rocket box on next update.
		this.markForDeletion();
    }

	@Override
	protected void onSensorEntry(NBPSensor sensor, NBPBox enteredBox) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onSensorExit(NBPSensor sensor, NBPBox exitedBox) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onBeforeUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onAfterUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDeletion() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onBloomPush(NBPBloom bloom, float angleOfForce, float force, float distance) {
		// TODO Auto-generated method stub
		
	}
}
