package com.dumbpug.main.gamedevicestesting.weapons;

import com.dumbpug.main.gamedevicestesting.C;
import com.dumbpug.main.gamedevicestesting.player.Player;
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
	private Player owner;
	
	/**
	 * Create a new grenade.
	 * @param owner
	 */
    public Rocket(Player owner) {
        super(owner.getCurrentOriginPoint().getX(), owner.getCurrentOriginPoint().getY(), C.ROCKET_SIZE, C.ROCKET_SIZE, NBPBoxType.KINETIC);
        // Set the owner.
        this.owner = owner;
        // Set various properties for the grenade.
        setName("ROCKET");
        // This rocket is NOT affected by gravity.
        this.setAffectedByGravity(false);
        // Set max velocity. 
        setMaxVelocityX(C.ROCKET_MAX_VELOCITY);
        setMaxVelocityY(C.ROCKET_MAX_VELOCITY);
        // Apply initial impulse in the players facing direction
        this.applyImpulse((float) Math.cos(owner.getAngleOfFocus()) * C.ROCKET_INITIAL_VELOCITY, (float) Math.sin(owner.getAngleOfFocus()) * C.ROCKET_INITIAL_VELOCITY);
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
	protected boolean onBloomPush(NBPBloom bloom, float angleOfForce, float force, float distance) {
		return true;
	}
}
