package com.dumbpug.main.gamedevicestesting.weapons;

import com.dumbpug.main.gamedevicestesting.C;
import com.dumbpug.nbp.NBPBloom;
import com.dumbpug.nbp.NBPBox;
import com.dumbpug.nbp.NBPBoxType;
import com.dumbpug.nbp.NBPIntersectionPoint;
import com.dumbpug.nbp.NBPSensor;

/**
 * Represents a rocket fired from a laser mine.
 * @author nikolas.howard
 *
 */
public class LaserMineRocket extends NBPBox {
	// The laser mine which fired the rocket.   
	private LaserMine ownerMine;
	
	/**
	 * Create a new Laser Mine rocket.
	 * @param owner
	 */
    public LaserMineRocket(LaserMine ownerMine) {
        super(ownerMine.getCurrentOriginPoint().getX(), ownerMine.getCurrentOriginPoint().getY(), C.ROCKET_SIZE, C.ROCKET_SIZE, NBPBoxType.KINETIC);
        // Set the owner.
        this.ownerMine = ownerMine;
        // Set various properties for the grenade.
        setName("LASER_MINE_ROCKET");
        // This rocket is NOT affected by gravity.
        this.setAffectedByGravity(false);
        // Set max velocity. 
        setMaxVelocityX(C.ROCKET_MAX_VELOCITY);
        setMaxVelocityY(C.ROCKET_MAX_VELOCITY);
        // Apply initial impulse in the mines facing direction.
        switch(ownerMine.getFacingDirection()) {
		case DOWN:
			this.applyImpulse(0f, -C.ROCKET_INITIAL_VELOCITY);
			break;
		case LEFT:
			this.applyImpulse(-C.ROCKET_INITIAL_VELOCITY, -0f);
			break;
		case RIGHT:
			this.applyImpulse(C.ROCKET_INITIAL_VELOCITY, 0f);
			break;
		case UP:
			this.applyImpulse(0f, C.ROCKET_INITIAL_VELOCITY);
			break;
        }
    }
    
    @Override
    protected void onCollisonWithKineticBox(NBPBox collidingBox, NBPIntersectionPoint kinematicBoxOriginAtCollision) {
    	// Make sure that we are not just intersecting with the mine that fired this rocket.
    	if(collidingBox != ownerMine) {
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
