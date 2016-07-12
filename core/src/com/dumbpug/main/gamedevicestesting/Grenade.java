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
public class Grenade extends NBPBox {
	// The time the grenade was thrown.
	private long timeThrownInMillis;
	// Fuse time of the grenade.
	private long fuseTimeMillis;
	   
	/**
	 * Create a new grenade.
	 * @param owner
	 */
    public Grenade(PlayerBox owner, long fuseTimeMillis) {
        super(owner.getCurrentOriginPoint().getX(), owner.getCurrentOriginPoint().getY(), C.GRENADE_SIZE, C.GRENADE_SIZE, NBPBoxType.KINETIC);
        // Set the grenade fuse time.
        this.fuseTimeMillis = fuseTimeMillis;
        // Set the thrown time.
        this.timeThrownInMillis = System.currentTimeMillis();
        // Set various properties for the grenade.
        setName("GRENADE");
        setFriction(C.GRENADE_FRICTION);
        setRestitution(C.GRENADE_RESTITUTION);
        // Set max velocity. 
        setMaxVelocityX(C.GRENADE_MAX_VELOCITY);
        setMaxVelocityY(C.GRENADE_MAX_VELOCITY);
        // Apply initial impulse in the players facing direction
        this.applyImpulse(owner.facingRight ? C.GRENADE_INITIAL_VELOCITY : -C.GRENADE_INITIAL_VELOCITY, 0f);
    }
    
    @Override
    public void update() {
    	// Do physics step.
    	super.update();
    	// Has the fuse run out?
    	if ((System.currentTimeMillis() - timeThrownInMillis) >= fuseTimeMillis) {
    		// Explode! Add a world bloom.
    		NBPBloom grenadeExplosionBloom = new NBPBloom(this.getCurrentOriginPoint().getX(), 
    				this.getCurrentOriginPoint().getY(), C.GRENADE_EXPLOSION_RADIUS, C.GRENADE_EXPLOSION_FORCE);
    		this.getWrappingWorld().addBloom(grenadeExplosionBloom);
    		// Remove this grenade box on next update.
    		this.markForDeletion();
    	}
    }

	@Override
	protected void onCollisonWithKineticBox(NBPBox collidingBox, NBPIntersectionPoint kinematicBoxOriginAtCollision) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onCollisonWithStaticBox(NBPBox collidingBox, NBPIntersectionPoint originAtCollision) {
		// TODO Auto-generated method stub
		
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
